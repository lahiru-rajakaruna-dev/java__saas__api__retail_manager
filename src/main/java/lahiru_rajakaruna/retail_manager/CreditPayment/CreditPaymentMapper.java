package lahiru_rajakaruna.retail_manager.CreditPayment;

import lahiru_rajakaruna.retail_manager.CreditAccount.CreditAccount;
import lahiru_rajakaruna.retail_manager.CreditEntryPayment.CreditEntryPayment;
import lahiru_rajakaruna.retail_manager.CreditEntryPayment.CreditEntryPaymentMapper;
import lahiru_rajakaruna.retail_manager.CreditPayment.DTOs.CreateCreditPaymentDTO;
import lahiru_rajakaruna.retail_manager.CreditPayment.DTOs.ResponseCreditPaymentDTO;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

import java.math.BigDecimal;
import java.util.List;

public class CreditPaymentMapper {

    private CreditPaymentMapper() {
    }

    private static boolean isValueNullOrNotPositive(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static CreditPayment convertToCreditPayment(CreateCreditPaymentDTO dto, Shop shop,
            CreditAccount account) {
        if (dto == null) {
            throw new IllegalArgumentException("Cannot Convert: DTO cannot be null");
        }
        if (shop == null || shop.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Shop cannot be null");
        }
        if (account == null || account.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Credit account cannot be null");
        }
        if (isValueNullOrNotPositive(dto.getAmount())) {
            throw new IllegalArgumentException("Cannot Convert: Amount must be greater than zero");
        }

        CreditPayment payment = new CreditPayment();
        payment.setShop(shop);
        payment.setCreditAccount(account);
        payment.setAmount(dto.getAmount());
        return payment;
    }

    public static ResponseCreditPaymentDTO convertToDTO(CreditPayment entity,
            List<CreditEntryPayment> allocations) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot Convert: CreditPayment cannot be null");
        }
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Id cannot be null");
        }
        if (entity.getShop() == null || entity.getShop()
                                              .getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Shop or ShopId cannot be null");
        }
        if (entity.getCreditAccount() == null || entity.getCreditAccount()
                                                       .getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Credit account or its id cannot be null");
        }
        if (isValueNullOrNotPositive(entity.getAmount())) {
            throw new IllegalArgumentException("Cannot Convert: Invalid amount");
        }
        // Invariant: a payment must be allocated to at least one credit entry
        if (allocations == null || allocations.isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Payment must have at least one allocation");
        }

        ResponseCreditPaymentDTO dto = new ResponseCreditPaymentDTO();
        dto.setId(entity.getId());
        dto.setShopId(entity.getShop()
                            .getId());
        dto.setCreditAccountId(entity.getCreditAccount()
                                     .getId());
        dto.setAmount(entity.getAmount());
        dto.setAllocations(allocations.stream()
                                      .map(CreditEntryPaymentMapper::convertToDTO)
                                      .toList());
        return dto;
    }
}
