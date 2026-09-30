package lahiru_rajakaruna.retail_manager.Shop;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.CreateShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.PatchShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.ShopResponseDTO;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

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

    @Transactional
    public ShopResponseDTO createShop(CreateShopDTO dto) {
        boolean isNameNullOrBlankOrEmpty = dto.getName() == null || dto.getName().isBlank() || dto.getName().isEmpty();

        if (isNameNullOrBlankOrEmpty) {
            throw new IllegalArgumentException("Shop name cannot be empty or only contain whitespaces");
        }

        Tenant tenant = tenantRepo.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Could not find user with ID: %s".formatted(dto.getOwnerId())));

        Shop shop = ShopMapper.convertToShop(dto);
        Shop savedShop = shopRepo.save(shop);
        tenant.setShop(savedShop);
        tenantRepo.save(tenant);
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

    @Transactional
    public ShopResponseDTO patchShopById(UUID id, PatchShopDTO updates) {
        boolean isIdNull = id == null;
        if (isIdNull) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        Shop shop = findShopByIdOrThrow(id);

        if (updates.getName() != null) {
            shop.setName(updates.getName());
        }
        if (updates.getActiveState() != null) {
            shop.setActiveState(updates.getActiveState());
        }

        Shop savedShop = shopRepo.saveAndFlush(shop);
        return ShopMapper.convertToDTO(savedShop);
    }
}
