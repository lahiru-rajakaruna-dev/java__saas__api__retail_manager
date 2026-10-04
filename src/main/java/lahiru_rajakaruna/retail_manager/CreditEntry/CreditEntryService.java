package lahiru_rajakaruna.retail_manager.CreditEntry;


import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.CreditAccount.CreditAccount;
import lahiru_rajakaruna.retail_manager.CreditAccount.ICreditAccountRepository;
import lahiru_rajakaruna.retail_manager.CreditEntry.DTOs.CreateCreditEntryDTO;
import lahiru_rajakaruna.retail_manager.CreditEntry.DTOs.ResponseCreditEntryDTO;
import lahiru_rajakaruna.retail_manager.Sale.ISaleRepository;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CreditEntryService {

    private final ICreditEntryRepository creditEntryRepo;
    private final ICreditAccountRepository creditAccountRepo;
    private final ISaleRepository saleRepo;

    public CreditEntryService(ICreditEntryRepository creditEntryRepo,
            ICreditAccountRepository creditAccountRepo,
            ISaleRepository saleRepo) {
        this.creditEntryRepo = creditEntryRepo;
        this.creditAccountRepo = creditAccountRepo;
        this.saleRepo = saleRepo;
        checkInternalComponentsPresence();
    }

    private void checkInternalComponentsPresence() {
        Objects.requireNonNull(creditEntryRepo, "Credit Entry Repository Not Found");
        Objects.requireNonNull(creditAccountRepo, "Credit Account Repository Not Found");
        Objects.requireNonNull(saleRepo, "Sale Repository Not Found");
    }

    /**
     * Called by the sale-finalization flow when a sale has an unpaid balance.
     * Joins the caller's transaction so the sale and its credit entry commit together.
     */
    @Transactional
    public ResponseCreditEntryDTO createCreditEntryForSale(CreateCreditEntryDTO dto) {
        requireNonNull(dto, "Credit entry data is null");
        requireNonNull(dto.getSaleId(), "Must provide a sale for the credit entry");
        requireNonNull(dto.getCreditAccountId(), "Must provide a credit account for the credit entry");
        requireNonNull(dto.getAmount(), "Must provide an amount for the credit entry");

        Sale sale = saleRepo.findById(dto.getSaleId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Could not find sale with ID: %s".formatted(dto.getSaleId())));

        CreditAccount account = creditAccountRepo.findById(dto.getCreditAccountId())
                                                 .orElseThrow(() -> new RuntimeException(
                                                         "Could not find credit account with ID: %s".formatted(
                                                                 dto.getCreditAccountId())));

        if (!sale.getShop()
                 .getId()
                 .equals(account.getShop()
                                .getId())) {
            throw new IllegalArgumentException("Sale and credit account must belong to the same shop");
        }
        if (creditEntryRepo.existsBySale_Id(sale.getId())) {
            throw new IllegalStateException("A credit entry already exists for this sale");
        }
        if (dto.getAmount()
               .compareTo(sale.getTotal()) > 0) {
            throw new IllegalArgumentException("Credit amount cannot exceed the sale total");
        }

        CreditEntry entry = CreditEntryMapper.convertToCreditEntry(dto, sale.getShop(), sale, account);
        CreditEntry saved = creditEntryRepo.saveAndFlush(entry);
        return CreditEntryMapper.convertToDTO(saved);
    }

    @Transactional
    public ResponseCreditEntryDTO findById(UUID creditAccountId, UUID id) {
        requireNonNull(id, "ID parameter is null");
        CreditEntry entry = findCreditEntryOrThrow(id);
        assertThatCreditEntryBelongToCreditAccount(creditAccountId, entry);
        return CreditEntryMapper.convertToDTO(entry);
    }

    @Transactional
    public ResponseCreditEntryDTO findBySaleId(UUID creditAccountId, UUID saleId) {
        requireNonNull(saleId, "Sale ID parameter is null");
        CreditEntry entry = creditEntryRepo.findBySale_Id(saleId)
                                           .orElseThrow(() -> new RuntimeException(
                                                   "Could not find credit entry for sale with ID: %s".formatted(
                                                           saleId)));
        assertThatCreditEntryBelongToCreditAccount(creditAccountId, entry);
        return CreditEntryMapper.convertToDTO(entry);
    }

    /**
     * Entries of an account, oldest first.
     */
    @Transactional
    public List<ResponseCreditEntryDTO> findByCreditAccountId(UUID creditAccountId, boolean outstandingOnly) {
        requireNonNull(creditAccountId, "Credit account ID parameter is null");

        List<CreditEntry> entries = outstandingOnly
                ? creditEntryRepo.findOutstandingEntriesByCreditAccountId(creditAccountId)
                : creditEntryRepo.findAllByCreditAccount_IdOrderByCreatedAtAscIdAsc(creditAccountId);

        return entries.stream()
                      .map(CreditEntryMapper::convertToDTO)
                      .toList();
    }

    @Transactional
    public List<CreditEntry> lockOutstandingEntries(UUID creditAccountId) {
        requireNonNull(creditAccountId, "Credit account ID parameter is null");
        return creditEntryRepo.findOutstandingEntriesByCreditAccountIdOrderedByCreatedAt(creditAccountId);
    }

    /**
     * Applies part of a payment to an entry. Caller must hold the lock from lockOutstandingEntries.
     */
    @Transactional
    public CreditEntry applyAllocation(CreditEntry entry, BigDecimal allocation) {
        requireNonNull(entry, "Credit entry is null");
        requireNonNull(allocation, "Allocation is null");

        if (allocation.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Allocation must be greater than zero");
        }
        if (allocation.compareTo(entry.getOutstandingAmount()) > 0) {
            throw new IllegalArgumentException("Allocation cannot exceed the outstanding amount of the credit entry");
        }

        entry.setTotalReceivedAmount(entry.getTotalReceivedAmount()
                                          .add(allocation));

        if (entry.getOriginalAmount()
                 .compareTo(entry.getTotalReceivedAmount()) == 0) {
            entry.setState(ECreditEntryState.CLEARED);
        }
        
        return creditEntryRepo.saveAndFlush(entry);
    }

    private CreditEntry findCreditEntryOrThrow(UUID id) {
        return creditEntryRepo.findById(id)
                              .orElseThrow(() -> new RuntimeException(
                                      "Could not find credit entry with ID: %s".formatted(id)));
    }

    private void assertThatCreditEntryBelongToCreditAccount(UUID creditAccountId, CreditEntry entry) {
        if (!entry.getCreditAccount()
                  .getId()
                  .equals(creditAccountId)) {
            throw new RuntimeException(
                    "Credit entry: %s does not belong to credit account: %s".formatted(entry.getId(), creditAccountId));
        }
    }

    private void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
