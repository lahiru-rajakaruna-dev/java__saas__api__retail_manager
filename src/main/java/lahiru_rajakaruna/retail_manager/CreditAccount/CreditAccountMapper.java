package lahiru_rajakaruna.retail_manager.CreditAccount;

import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.CreateCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.ResponseCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

public class CreditAccountMapper {

    private CreditAccountMapper() {
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static CreditAccount convertToCreditAccount(CreateCreditAccountDTO dto, Shop shop) {
        if (dto == null) {
            throw new IllegalArgumentException("Cannot Convert: DTO cannot be null");
        }
        if (shop == null || shop.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Shop cannot be null");
        }
        if (isNullOrBlank(dto.getName())) {
            throw new IllegalArgumentException("Cannot Convert: Name cannot be null or blank");
        }
        if (dto.getName()
               .length() >= 30) {
            throw new IllegalArgumentException("Cannot Convert: Name must be less than 30 characters");
        }
        if (isNullOrBlank(dto.getPhone())) {
            throw new IllegalArgumentException("Cannot Convert: Phone cannot be null or blank");
        }

        CreditAccount account = new CreditAccount();
        account.setShop(shop);
        account.setName(dto.getName());
        account.setPhone(dto.getPhone());
        return account;
    }

    public static ResponseCreditAccountDTO convertToDTO(CreditAccount entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot Convert: CreditAccount cannot be null");
        }
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Id cannot be null");
        }
        if (entity.getShop() == null || entity.getShop()
                                              .getId() == null) {
            throw new IllegalArgumentException("Cannot Convert: Shop or ShopId cannot be null");
        }
        if (isNullOrBlank(entity.getName())) {
            throw new IllegalArgumentException("Cannot Convert: Invalid name");
        }
        if (isNullOrBlank(entity.getPhone())) {
            throw new IllegalArgumentException("Cannot Convert: Invalid phone");
        }

        ResponseCreditAccountDTO dto = new ResponseCreditAccountDTO();
        dto.setId(entity.getId());
        dto.setShopId(entity.getShop()
                            .getId());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        return dto;
    }
}