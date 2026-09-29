package lahiru_rajakaruna.retail_manager.Shop;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.CreateShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.ShopResponseDTO;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;

@Service
public class ShopService {

	private final IShopRepository shopRepo;
	private final ITenantRepository tenantRepo;

	public ShopService(IShopRepository shopRepo, ITenantRepository tenantRepo) {
		this.shopRepo = shopRepo;
		this.tenantRepo = tenantRepo;
		checkInternalComponentsPresence();
	}

	private void checkInternalComponentsPresence() {
		Objects.requireNonNull(shopRepo, "Shop Repository Not Found");
	}

	private Shop findShopByIdOrThrow(UUID id) {
		return shopRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Could not find shop with ID: %s".formatted(id)));
	}

	public ShopResponseDTO createShop(CreateShopDTO dto) {
		boolean isNameNullOrBlankOrEmpty = dto.getName() == null || dto.getName().isBlank() || dto.getName().isEmpty();

		if (isNameNullOrBlankOrEmpty) {
			throw new IllegalArgumentException("Shop name cannot be empty or only contain whitespaces");
		}

		Tenant tenant = tenantRepo.findById(dto.getOwnerId())
				.orElseThrow(() -> new RuntimeException("Could not find user with ID: %s".formatted(dto.getOwnerId())));

		Shop shop = ShopMapper.convertToShop(dto, tenant);
		Shop savedShop = shopRepo.saveAndFlush(shop);
		return ShopMapper.convertToDTO(savedShop);
	}

	public ShopResponseDTO findById(UUID id) {
		boolean isIdNull = id == null;
		if (isIdNull) {
			throw new IllegalArgumentException("ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		return ShopMapper.convertToDTO(shop);
	}

	public ShopResponseDTO updateShopName(UUID id, String name) {

		if (id == null) {
			throw new IllegalArgumentException("ID parameter is null");
		}
		if (name == null) {
			throw new IllegalArgumentException("Name parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setName(name);

		Shop updatedShop = shopRepo.saveAndFlush(shop);
		return ShopResponseDTO.convertToDTO(updatedShop);
	}

	public ShopResponseDTO activateShopById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException("ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setActiveState(EActiveState.ACTIVE);
		Shop updatedShop = shopRepo.saveAndFlush(shop);

		return ShopResponseDTO.convertToDTO(updatedShop);
	}

	public ShopResponseDTO deactivateShopById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException("ID parameter is null");
		}

		Shop shop = findShopByIdOrThrow(id);
		shop.setActiveState(EActiveState.INACTIVE);
		Shop updatedShop = shopRepo.saveAndFlush(shop);

		return ShopResponseDTO.convertToDTO(updatedShop);
	}
}
