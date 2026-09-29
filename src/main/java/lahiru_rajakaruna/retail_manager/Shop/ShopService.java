package lahiru_rajakaruna.retail_manager.Shop;

import java.util.Objects;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

	private final IShopRepository shopRepo;
	private final ITenantRepository tenantRepo;

	public ShopService(IShopRepository shopRepo,
			   ITenantRepository tenantRepo) {
		this.shopRepo = shopRepo;
		this.tenantRepo = tenantRepo;
		checkInternalComponentsPresence();
	}

	private void checkInternalComponentsPresence() {
		Objects.requireNonNull(shopRepo, "Shop Repository Not Found");
	}

	private Shop findShopByIdOrThrow(UUID id) {
		return shopRepo.findById(id).orElseThrow(
			() -> new RuntimeException(
				"Could not find shop with ID: %s".formatted(id)));
	}

	public ShopResponseDTO createShop(ShopResponseDTO dto) {

		if (dto.getName().isEmpty()) {
			throw new IllegalArgumentException(
				"Must provide a name for the shop");
		}

		Tenant tenant = tenantRepo.findById(dto.getOwnerId())
			.orElseThrow(() -> new RuntimeException(
			"Could not find user with ID: %s".formatted(dto
				.getOwnerId())));

		Shop shop = ShopResponseDTO.convertToEntity(dto, tenant);
		Shop savedShop = shopRepo.saveAndFlush(shop);
		return ShopResponseDTO.convertToDTO(savedShop);
	}

	public ShopResponseDTO findById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		return ShopResponseDTO.convertToDTO(shop);
	}

	public ShopResponseDTO updateShopName(UUID id, String name) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}
		if (name == null) {
			throw new IllegalArgumentException(
				"Name parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setName(name);

		Shop updatedShop = shopRepo.saveAndFlush(shop);
		return ShopResponseDTO.convertToDTO(updatedShop);
	}

	public ShopResponseDTO activateShopById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setActiveState(EActiveState.ACTIVE);
		Shop updatedShop = shopRepo.saveAndFlush(shop);

		return ShopResponseDTO.convertToDTO(updatedShop);
	}

	public ShopResponseDTO deactivateShopById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setActiveState(EActiveState.INACTIVE);
		Shop updatedShop = shopRepo.saveAndFlush(shop);

		return ShopResponseDTO.convertToDTO(updatedShop);
	}
}
