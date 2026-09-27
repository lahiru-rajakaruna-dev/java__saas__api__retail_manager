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
	}

	private void checkInternalComponentsPresence() {
		Objects.requireNonNull(shopRepo, "Shop Repository Not Found");
	}

	private Shop findShopByIdOrThrow(UUID id) {
		return shopRepo.findById(id).orElseThrow(
			() -> new RuntimeException(
				"Could not find shop with ID: %s".formatted(id)));
	}

	public ShopDTO createShop(ShopDTO dto) {
		checkInternalComponentsPresence();

		if (dto.getName().isEmpty()) {
			throw new IllegalArgumentException(
				"Must provide a name for the shop");
		}

		Tenant tenant = tenantRepo.findById(dto.getOwnerId())
			.orElseThrow(() -> new RuntimeException(
			"Could not find user with ID: %s".formatted(dto
				.getOwnerId())));

		Shop shop = ShopDTO.convertToEntity(dto, tenant);
		Shop savedShop = shopRepo.saveAndFlush(shop);
		return ShopDTO.convertToDTO(savedShop);
	}

	public ShopDTO findById(UUID id) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		return ShopDTO.convertToDTO(shop);
	}

	public ShopDTO updateShopName(UUID id, String name) {
		checkInternalComponentsPresence();

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
		return ShopDTO.convertToDTO(updatedShop);
	}

	public ShopDTO activateShopById(UUID id) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setActiveState(EActiveState.ACTIVE);
		Shop updatedShop = shopRepo.saveAndFlush(shop);

		return ShopDTO.convertToDTO(updatedShop);
	}

	public ShopDTO deactivateShopById(UUID id) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setActiveState(EActiveState.INACTIVE);
		Shop updatedShop = shopRepo.saveAndFlush(shop);

		return ShopDTO.convertToDTO(updatedShop);
	}
}
