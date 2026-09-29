package lahiru_rajakaruna.retail_manager.Shop;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.CreateShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.ShopResponseDTO;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;

public class ShopMapper {

	private ShopMapper() {
	}

	public static ShopResponseDTO convertToDTO(Shop entity) {
		boolean isShopNameNullOrBlankOrEmpty = entity.getName() == null || entity.getName().isBlank()
				|| entity.getName().isEmpty();
		boolean isShopOwnerIdNull = entity.getOwner().getId() == null;
		boolean isShopStateNull = entity.getActiveState() == null;

		if (isShopNameNullOrBlankOrEmpty) {
			throw new RuntimeException("Cannot Convert: Invalid Shop Name");
		}
		if (isShopOwnerIdNull) {
			throw new RuntimeException("Cannot Convert: Could not find owner id");
		}
		if (isShopStateNull) {
			throw new RuntimeException("Cannot Convert: Invalid shop state");
		}

		ShopResponseDTO dto = new ShopResponseDTO();
		dto.setName(entity.getName());
		dto.setOwnerId(entity.getOwner().getId());
		dto.setActiveState(entity.getActiveState());
		return dto;
	}

	public static Shop convertToShop(CreateShopDTO dto, Tenant owner) {
		boolean isNameNull = dto.getName() == null || dto.getName().isBlank() || dto.getName().isEmpty();

		if (isNameNull) {
			throw new RuntimeException("Cannot Convert: Invalid shop name(null, empty or blank).");
		}

		Shop shop = new Shop();
		shop.setName(dto.getName());
		shop.setOwner(owner);
		shop.setActiveState(EActiveState.INACTIVE);
		return shop;
	}

}
