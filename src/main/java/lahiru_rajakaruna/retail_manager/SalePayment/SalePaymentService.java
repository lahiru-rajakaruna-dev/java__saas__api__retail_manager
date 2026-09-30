package lahiru_rajakaruna.retail_manager.SalePayment;


import lahiru_rajakaruna.retail_manager.Common.ESaleState;
import lahiru_rajakaruna.retail_manager.Sale.ISaleRepository;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.CreateSalePaymentDTO;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.PatchSalePaymentDTO;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.ResponseSalePaymentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class SalePaymentService {

    private final ISalePaymentRepository salePaymentRepo;
    private final ISaleRepository saleRepo;

    public SalePaymentService(ISalePaymentRepository salePaymentRepo, ISaleRepository saleRepo) {
        this.salePaymentRepo = salePaymentRepo;
        this.saleRepo = saleRepo;
        Objects.requireNonNull(salePaymentRepo, "SalePayment Repository Not Found");
        Objects.requireNonNull(saleRepo, "Sale Repository Not Found");
    }

    @Transactional
    public ResponseSalePaymentDTO createSalePayment(CreateSalePaymentDTO dto) {
        requireNonNull(dto, "Payment data not provided");
        requireNonNull(dto.getShopId(), "Must provide a shop for the payment");
        requireNonNull(dto.getSaleId(), "Must provide a sale for the payment");
        requireNonNull(dto.getAmount(), "Must provide an amount for the payment");
        requirePositive(dto.getAmount());

        Sale sale = findSaleOrThrow(dto.getSaleId());

        if (!sale.getShop()
                 .getId()
                 .equals(dto.getShopId())) {
            throw new IllegalArgumentException("Payment shop must match the shop of the sale");
        }

        requireOpenSale(sale);

        if (salePaymentRepo.existsBySaleId(sale.getId())) {
            throw new IllegalStateException("Sale already has a payment");
        }

        requireNotExceedingTotal(dto.getAmount(), sale);

        SalePayment payment = SalePaymentMapper.convertToSalePayment(dto, sale);
        SalePayment saved = salePaymentRepo.saveAndFlush(payment);
        return SalePaymentMapper.convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public ResponseSalePaymentDTO findById(UUID id) {
        requireNonNull(id, "ID parameter is null");
        return SalePaymentMapper.convertToDTO(findPaymentOrThrow(id));
    }

    @Transactional(readOnly = true)
    public ResponseSalePaymentDTO findBySaleId(UUID saleId) {
        requireNonNull(saleId, "Sale ID parameter is null");
        SalePayment payment = salePaymentRepo.findBySaleId(saleId)
                                             .orElseThrow(() -> new RuntimeException("Could not find payment for sale with ID: %s".formatted(saleId)));
        return SalePaymentMapper.convertToDTO(payment);
    }

    @Transactional
    public ResponseSalePaymentDTO patchById(UUID id, PatchSalePaymentDTO updates) {
        requireNonNull(id, "ID parameter is null");
        requireNonNull(updates, "Updates not provided");

        SalePayment payment = findPaymentOrThrow(id);
        Sale sale = payment.getSale();
        requireOpenSale(sale);

        if (updates.getAmount() != null) {
            requirePositive(updates.getAmount());
            requireNotExceedingTotal(updates.getAmount(), sale);
            payment.setAmount(updates.getAmount());
        }

        SalePayment saved = salePaymentRepo.saveAndFlush(payment);
        return SalePaymentMapper.convertToDTO(saved);
    }

    @Transactional
    public void deleteById(UUID id) {
        requireNonNull(id, "ID parameter is null");
        SalePayment payment = findPaymentOrThrow(id);
        requireOpenSale(payment.getSale());
        salePaymentRepo.delete(payment);
    }

    private void requireOpenSale(Sale sale) {
        if (!ESaleState.OPEN.equals(sale.getSaleState())) {
            throw new IllegalStateException("Cannot modify the payment of a closed sale");
        }
    }

    private void requirePositive(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
    }

    private void requireNotExceedingTotal(BigDecimal amount, Sale sale) {
        if (amount.compareTo(sale.getTotal()) > 0) {
            throw new IllegalArgumentException("Payment amount cannot exceed the sale total");
        }
    }

    private void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private Sale findSaleOrThrow(UUID id) {
        return saleRepo.findById(id)
                       .orElseThrow(() -> new RuntimeException("Could not find sale with ID: %s".formatted(id)));
    }

    private SalePayment findPaymentOrThrow(UUID id) {
        return salePaymentRepo.findById(id)
                              .orElseThrow(() -> new RuntimeException("Could not find sale payment with ID: %s".formatted(id)));
    }
}
