package lahiru_rajakaruna.retail_manager.CreditEntry;


import lahiru_rajakaruna.retail_manager.CreditAccount.CreditAccount;
import lahiru_rajakaruna.retail_manager.CreditEntry.DTOs.CreateCreditEntryDTO;
import lahiru_rajakaruna.retail_manager.CreditEntry.DTOs.ResponseCreditEntryDTO;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

import java.math.BigDecimal;
import java.util.function.Function;

public class CreditEntryMapper {

    private CreditEntryMapper() {
    }

    private static <T> void requireNonNullProperty(T o, Function<T, ?> property, String message) {
        if (o == null || property.apply(o) == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requirePositive(BigDecimal value, String message) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static CreditEntry convertToCreditEntry(CreateCreditEntryDTO dto, Shop shop, Sale sale,
                                                   CreditAccount account) {
        requireNonNull(dto, "Cannot Convert: DTO cannot be null");

        requireNonNullProperty(dto, CreateCreditEntryDTO::getAmount, "Cannot Convert: Amount must be greater than zero");
        requirePositive(dto.getAmount(), "Cannot Convert: Amount must be greater than zero");

        requireNonNullProperty(shop, Shop::getId, "Cannot Convert: Shop cannot be null");
        requireNonNullProperty(sale, Sale::getId, "Cannot Convert: Sale cannot be null");
        requireNonNullProperty(account, CreditAccount::getId, "Cannot Convert: Credit account cannot be null");

        CreditEntry entry = new CreditEntry();
        entry.setShop(shop);
        entry.setSale(sale);
        entry.setCreditAccount(account);
        entry.setOriginalAmount(dto.getAmount());
        entry.setTotalReceivedAmount(BigDecimal.ZERO);
        return entry;
    }

    public static ResponseCreditEntryDTO convertToDTO(CreditEntry entity) {
        requireNonNull(entity, "Cannot Convert: CreditEntry cannot be null");
        requireNonNull(entity.getId(), "Cannot Convert: Id cannot be null");

        requireNonNullProperty(entity.getShop(), Shop::getId, "Cannot Convert: Shop or ShopId cannot be null");
        requireNonNullProperty(entity.getSale(), Sale::getId, "Cannot Convert: Sale or SaleId cannot be null");
        requireNonNullProperty(entity.getCreditAccount(), CreditAccount::getId, "Cannot Convert: Credit account or its id cannot be null");

        requireNonNull(entity.getOriginalAmount(), "Cannot Convert: Original amount cannot be null");
        requirePositive(entity.getOriginalAmount(), "Cannot Convert: Invalid original amount");

        if (entity.getTotalReceivedAmount() == null
                || entity.getTotalReceivedAmount()
                         .compareTo(BigDecimal.ZERO) < 0
                || entity.getTotalReceivedAmount()
                         .compareTo(entity.getOriginalAmount()) > 0) {
            throw new IllegalArgumentException("Cannot Convert: Invalid total received amount");
        }

        ResponseCreditEntryDTO dto = new ResponseCreditEntryDTO();
        dto.setId(entity.getId());
        dto.setShopId(entity.getShop()
                            .getId());
        dto.setSaleId(entity.getSale()
                            .getId());
        dto.setCreditAccountId(entity.getCreditAccount()
                                     .getId());
        dto.setOriginalAmount(entity.getOriginalAmount());
        dto.setTotalReceivedAmount(entity.getTotalReceivedAmount());
        dto.setOutstandingAmount(entity.getOutstandingAmount());
        return dto;
    }
}
