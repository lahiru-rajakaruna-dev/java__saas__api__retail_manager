package lahiru_rajakaruna.retail_manager.CreditEntryPayment;

import lahiru_rajakaruna.retail_manager.CreditEntry.CreditEntry;
import lahiru_rajakaruna.retail_manager.CreditEntryPayment.DTOs.ResponseCreditEntryPaymentDTO;
import lahiru_rajakaruna.retail_manager.CreditPayment.CreditPayment;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

import java.math.BigDecimal;

public class CreditEntryPaymentMapper {

    private CreditEntryPaymentMapper() {
    }

    private static boolean isValueNullOrNotPositive(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static CreditEntryPayment convertToCreditEntryPayment(Shop shop, CreditEntry creditEntry,
            CreditPayment creditPayment,
            BigDecimal allocation) {
        if (shop == null || shop.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Shop cannot be null");
        }
        if (creditEntry == null || creditEntry.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Credit entry cannot be null");
        }
        if (creditPayment == null || creditPayment.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Credit payment cannot be null");
        }
        if (isValueNullOrNotPositive(allocation)) {
            throw new IllegalArgumentException("Cannot Convert: Allocation must be greater than zero");
        }

        CreditEntryPayment entity = new CreditEntryPayment();
        entity.setShop(shop);
        entity.setCreditEntry(creditEntry);
        entity.setCreditPayment(creditPayment);
        entity.setAllocation(allocation);
        return entity;
    }

    public static ResponseCreditEntryPaymentDTO convertToDTO(CreditEntryPayment entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot Convert: CreditEntryPayment cannot be null");
        }
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Id cannot be null");
        }
        if (entity.getShop() == null || entity.getShop()
                                              .getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Shop or ShopId cannot be null");
        }
        if (entity.getCreditEntry() == null || entity.getCreditEntry()
                                                     .getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Credit entry or its id cannot be null");
        }
        if (entity.getCreditPayment() == null || entity.getCreditPayment()
                                                       .getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Credit payment or its id cannot be null");
        }
        if (isValueNullOrNotPositive(entity.getAllocation())) {
            throw new IllegalArgumentException("Cannot Convert: Invalid allocation");
        }

        ResponseCreditEntryPaymentDTO dto = new ResponseCreditEntryPaymentDTO();
        dto.setId(entity.getId());
        dto.setShopId(entity.getShop()
                            .getId());
        dto.setCreditEntryId(entity.getCreditEntry()
                                   .getId());
        dto.setCreditPaymentId(entity.getCreditPayment()
                                     .getId());
        dto.setAllocation(entity.getAllocation());
        return dto;
    }
}
