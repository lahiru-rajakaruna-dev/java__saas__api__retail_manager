package lahiru_rajakaruna.retail_manager.CreditPayment;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.CreditAccount.CreditAccount;
import lahiru_rajakaruna.retail_manager.CreditAccount.ICreditAccountRepository;
import lahiru_rajakaruna.retail_manager.CreditEntry.CreditEntry;
import lahiru_rajakaruna.retail_manager.CreditEntry.CreditEntryService;
import lahiru_rajakaruna.retail_manager.CreditEntryPayment.CreditEntryPayment;
import lahiru_rajakaruna.retail_manager.CreditEntryPayment.CreditEntryPaymentMapper;
import lahiru_rajakaruna.retail_manager.CreditEntryPayment.ICreditEntryPaymentRepository;
import lahiru_rajakaruna.retail_manager.CreditPayment.DTOs.CreateCreditPaymentDTO;
import lahiru_rajakaruna.retail_manager.CreditPayment.DTOs.ResponseCreditPaymentDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CreditPaymentService {

    private final ICreditPaymentRepository creditPaymentRepo;
    private final ICreditEntryPaymentRepository creditEntryPaymentRepo;
    private final ICreditAccountRepository creditAccountRepo;
    private final CreditEntryService creditEntryService;

    public CreditPaymentService(ICreditPaymentRepository creditPaymentRepo,
            ICreditEntryPaymentRepository creditEntryPaymentRepo,
            ICreditAccountRepository creditAccountRepo,
            CreditEntryService creditEntryService) {
        this.creditPaymentRepo = creditPaymentRepo;
        this.creditEntryPaymentRepo = creditEntryPaymentRepo;
        this.creditAccountRepo = creditAccountRepo;
        this.creditEntryService = creditEntryService;
        checkInternalComponentsPresence();
    }

    private void checkInternalComponentsPresence() {
        Objects.requireNonNull(creditPaymentRepo, "Credit Payment Repository Not Found");
        Objects.requireNonNull(creditEntryPaymentRepo, "Credit Entry Payment Repository Not Found");
        Objects.requireNonNull(creditAccountRepo, "Credit Account Repository Not Found");
        Objects.requireNonNull(creditEntryService, "Credit Entry Service Not Found");
    }

    @Transactional
    public ResponseCreditPaymentDTO createPayment(UUID creditAccountId, CreateCreditPaymentDTO dto) {
        requireNonNull(creditAccountId, "Credit account ID parameter is null");
        requireNonNull(dto, "Credit payment data is null");
        requireNonNull(dto.getAmount(), "Must provide an amount for the credit payment");

        CreditAccount account = findCreditAccountOrThrow(creditAccountId);

        List<CreditEntry> outstandingEntries = creditEntryService.lockOutstandingEntries(creditAccountId);

        BigDecimal totalOutstanding = outstandingEntries.stream()
                                                        .map(CreditEntry::getOutstandingAmount)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalOutstanding.signum() == 0) {
            throw new IllegalStateException("This credit account has no outstanding credit");
        }
        if (dto.getAmount()
               .compareTo(totalOutstanding) > 0) {
            throw new IllegalArgumentException(
                    "Payment amount exceeds the total outstanding credit (%s)".formatted(totalOutstanding));
        }

        CreditPayment payment = CreditPaymentMapper.convertToCreditPayment(dto, account.getShop(), account);
        CreditPayment savedPayment = creditPaymentRepo.saveAndFlush(payment);

        BigDecimal availableToAllocate = dto.getAmount();
        List<CreditEntryPayment> allocatedCreditEntries = new ArrayList<>();

        for (CreditEntry entry : outstandingEntries) {
            if (availableToAllocate.signum() == 0) {
                break;
            }

            BigDecimal allocation = availableToAllocate.min(entry.getOutstandingAmount());
            CreditEntry updatedEntry = creditEntryService.applyAllocation(entry, allocation);

            CreditEntryPayment entryPayment = CreditEntryPaymentMapper.convertToCreditEntryPayment(
                    account.getShop(), updatedEntry, savedPayment, allocation);
            CreditEntryPayment addedCreditEntryRecord = creditEntryPaymentRepo.saveAndFlush(entryPayment);
            allocatedCreditEntries.add(addedCreditEntryRecord);

            availableToAllocate = availableToAllocate.subtract(allocation);
        }

        // Invariant: payment amount == SUM(allocations)
        if (availableToAllocate.signum() != 0) {
            throw new IllegalStateException("Payment could not be fully allocated");
        }

        return CreditPaymentMapper.convertToDTO(savedPayment, allocatedCreditEntries);
    }

    @Transactional
    public ResponseCreditPaymentDTO findById(UUID creditAccountId, UUID id) {
        requireNonNull(creditAccountId, "Credit account ID parameter is null");
        requireNonNull(id, "ID parameter is null");

        CreditPayment payment = findPaymentInAccountOrThrow(creditAccountId, id);
        List<CreditEntryPayment> allocations =
                creditEntryPaymentRepo.findAllByCreditPayment_IdOrderByCreatedAtAscIdAsc(payment.getId());
        return CreditPaymentMapper.convertToDTO(payment, allocations);
    }

    @Transactional
    public List<ResponseCreditPaymentDTO> findByCreditAccountId(UUID creditAccountId) {
        requireNonNull(creditAccountId, "Credit account ID parameter is null");
        findCreditAccountOrThrow(creditAccountId);

        List<CreditPayment> payments =
                creditPaymentRepo.findAllByCreditAccount_IdOrderByCreatedAtDescIdDesc(creditAccountId);

        if (payments.isEmpty()) {
            return Collections.emptyList();
        }

        /*
         * TODO: YOU DONT UNDERSTAND THIS SHIT. DECIPHER IT
         * */
        // One query for all allocations instead of one per payment
        Map<UUID, List<CreditEntryPayment>> allocationsByPaymentId =
                creditEntryPaymentRepo.findAllByCreditPayment_IdIn(payments.stream()
                                                                           .map(CreditPayment::getId)
                                                                           .toList())
                                      .stream()
                                      .sorted(java.util.Comparator.comparing(CreditEntryPayment::getCreatedAt)
                                                                  .thenComparing(CreditEntryPayment::getId))
                                      .collect(Collectors.groupingBy(ep -> ep.getCreditPayment()
                                                                             .getId()));

        return payments.stream()
                       .map(p -> CreditPaymentMapper.convertToDTO(
                               p, allocationsByPaymentId.getOrDefault(p.getId(), Collections.emptyList())))
                       .toList();
    }

    private CreditAccount findCreditAccountOrThrow(UUID id) {
        return creditAccountRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                        "Could not find credit account with ID: %s".formatted(id)));
    }

    private CreditPayment findPaymentInAccountOrThrow(UUID creditAccountId, UUID id) {
        CreditPayment payment = creditPaymentRepo.findById(id)
                                                 .orElseThrow(() -> new RuntimeException(
                                                         "Could not find credit payment with ID: %s".formatted(id)));

        if (!payment.getCreditAccount()
                    .getId()
                    .equals(creditAccountId)) {
            throw new RuntimeException(
                    "Credit payment %s does not belong to credit account %s".formatted(id, creditAccountId));
        }
        return payment;
    }

    private void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
