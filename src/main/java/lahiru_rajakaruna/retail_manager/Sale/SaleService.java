/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.Common.ESaleState;
import lahiru_rajakaruna.retail_manager.CreditAccount.CreditAccountService;
import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.ResponseCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.CreditEntry.CreditEntryService;
import lahiru_rajakaruna.retail_manager.CreditEntry.DTOs.CreateCreditEntryDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.CloseSaleDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.CreateSaleDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.SaleResponseDTO;
import lahiru_rajakaruna.retail_manager.SalePayment.ISalePaymentRepository;
import lahiru_rajakaruna.retail_manager.SalePayment.SalePayment;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author bl4z3
 */
@Service
public class SaleService {

    private final ISaleRepository saleRepo;
    private final IShopRepository shopRepo;
    private final ISalePaymentRepository salePaymentRepo;
    private final CreditAccountService creditAccountService;
    private final CreditEntryService creditEntryService;

    public SaleService(ISaleRepository saleRepo, IShopRepository shopRepo, ISalePaymentRepository salePaymentRepo,
            CreditAccountService creditAccountService, CreditEntryService creditEntryService) {
        this.saleRepo = saleRepo;
        this.shopRepo = shopRepo;
        this.salePaymentRepo = salePaymentRepo;
        this.creditAccountService = creditAccountService;
        this.creditEntryService = creditEntryService;
        checkInternalComponentsPresence();
    }

    private void checkInternalComponentsPresence() {
        Objects.requireNonNull(saleRepo, "Sale Repository Not Found");
        Objects.requireNonNull(shopRepo, "Shop Repository Not Found");
    }

    @Transactional
    public SaleResponseDTO createSale(CreateSaleDTO dto) {
        boolean isShopIdNull = dto.getShopId() == null;
        if (isShopIdNull) {
            throw new IllegalArgumentException("Must provide a shop for the sale");
        }

        Shop shop = shopRepo.findById(dto.getShopId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Could not find shop with ID: %s".formatted(dto.getShopId())));

        Sale newSale = SaleMapper.getNewSale(shop);
        Sale savedSale = saleRepo.saveAndFlush(newSale);
        return SaleMapper.convertToDTO(savedSale);
    }

    @Transactional
    public SaleResponseDTO findSaleById(UUID id) {
        boolean isIdNull = id == null;
        if (isIdNull) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        Sale sale = findSaleOrThrow(id);
        return SaleMapper.convertToDTO(sale);
    }

    @Transactional
    public List<SaleResponseDTO> findSalesByShopId(UUID shopId) {
        boolean isShopIdNull = shopId == null;
        if (isShopIdNull) {
            throw new IllegalArgumentException("Shop ID parameter is null");
        }

        List<SaleResponseDTO> sales = saleRepo.findAllByShopId(shopId)
                                              .stream()
                                              .map(SaleMapper::convertToDTO)
                                              .toList();
        return sales;
    }

    @Transactional
    public SaleResponseDTO updateSaleTotal(UUID id, BigDecimal total) {
        boolean isIdNull = id == null;
        boolean isTotalNull = total == null;
        boolean isTotalLessThanZero = !isTotalNull && total.compareTo(BigDecimal.ZERO) < 0;

        if (isIdNull) {
            throw new IllegalArgumentException("ID parameter is null");
        }
        if (isTotalNull) {
            throw new IllegalArgumentException("Total parameter is null");
        }
        if (isTotalLessThanZero) {
            throw new IllegalArgumentException("Sale total cannot be negative");
        }

        Sale sale = findSaleOrThrow(id);

        if (sale.getSaleState()
                .equals(ESaleState.CLOSED)) {
            throw new IllegalStateException("Cannot update the total of a closed sale");
        }

        sale.setTotal(total);
        Sale updatedSale = saleRepo.saveAndFlush(sale);
        return SaleMapper.convertToDTO(updatedSale);
    }

    @Transactional
    public SaleResponseDTO closeSaleById(UUID id, CloseSaleDTO closeData) {
        requireNonNull(id, "ID parameter is null");
        requireNonNull(closeData.getPayment(), "Payment data not provided");

        Sale sale = findSaleOrThrow(id);

        if (sale.getSaleState()
                .equals(ESaleState.CLOSED)) {
            throw new RuntimeException("Sale %s is closed".formatted(id));
        }

        SalePayment payment = salePaymentRepo.findBySaleId(sale.getId())
                                             .orElse(null);

        BigDecimal outstandingBalance = calculateOutstandingBalance(sale, payment);

        if (outstandingBalance.compareTo(BigDecimal.ZERO) > 0) {
            requireNonNull(closeData.getCustomerDetails(), "Customer details are not provided");
            ResponseCreditAccountDTO account = creditAccountService.findByPhoneOrCreate(sale.getShop()
                                                                                            .getId(),
                                                                                        closeData.getCustomerDetails()
                                                                                                 .getPhone(),
                                                                                        closeData.getCustomerDetails()
                                                                                                 .getName());
            CreateCreditEntryDTO creditEntryData = new CreateCreditEntryDTO(id, account.getId(), outstandingBalance);
            creditEntryService.createCreditEntryForSale(creditEntryData);
        }

        sale.setSaleState(ESaleState.CLOSED);
        Sale updatedSale = saleRepo.saveAndFlush(sale);
        return SaleMapper.convertToDTO(updatedSale);
    }

    private BigDecimal calculateOutstandingBalance(Sale sale, SalePayment payment) {
        if (payment == null) {
            return sale.getTotal();
        } else if (payment.getAmount()
                          .compareTo(sale.getTotal()) < 0) {
            return sale.getTotal()
                       .subtract(payment.getAmount());
        } else {
            return BigDecimal.ZERO;
        }

    }

    private Sale findSaleOrThrow(UUID id) {
        return saleRepo.findById(id)
                       .orElseThrow(() -> new RuntimeException(String.format("Could not find sale with ID: %s", id)));
    }

    private void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

}
