package lahiru_rajakaruna.retail_manager.Shop;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.CreateShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.ShopResponseDTO;

public class ShopMapper {

    private ShopMapper() {
    }

    public static ShopResponseDTO convertToDTO(Shop entity) {
        boolean isIdNull = entity.getId() == null;
        boolean isShopNameNullOrBlankOrEmpty = entity.getName() == null || entity.getName().isBlank()
                || entity.getName().isEmpty();
        boolean isShopStateNull = entity.getActiveState() == null;

        if (isIdNull) {
            throw new RuntimeException("Cannot Convert: ID is null");
        }
        if (isShopNameNullOrBlankOrEmpty) {
            throw new RuntimeException("Cannot Convert: Invalid Shop Name");
        }
        if (isShopStateNull) {
            throw new RuntimeException("Cannot Convert: Invalid shop state");
        }

        ShopResponseDTO dto = new ShopResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setActiveState(entity.getActiveState());
        return dto;
    }

    public static Shop convertToShop(CreateShopDTO dto) {
        boolean isNameNull = dto.getName() == null || dto.getName().isBlank() || dto.getName().isEmpty();

        if (isNameNull) {
            throw new RuntimeException("Cannot Convert: Invalid shop name(null, empty or blank).");
        }

        Shop shop = new Shop();
        shop.setName(dto.getName());
        shop.setActiveState(EActiveState.INACTIVE);
        return shop;
    }

}
