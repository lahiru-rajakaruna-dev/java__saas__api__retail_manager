package lahiru_rajakaruna.retail_manager.Shop;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class ShopService {
    private final IShopRepository shopRepo;

    public ShopService(IShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    private void checkInternalComponentsPresence() {
        Objects.requireNonNull(shopRepo, "Shop Repository Not Found");
    }

    public ShopDTO createShop(ShopDTO dto) {
        checkInternalComponentsPresence();

        if (dto.getName().isEmpty()) {
            throw new IllegalArgumentException(
                    "Must provide a name for the shop");
        }

        Shop shop = ShopDTO.convertToEntity(dto);
        Shop savedShop = shopRepo.saveAndFlush(shop);
        return ShopDTO.convertToDTO(savedShop);
    }

    public ShopDTO findById(UUID id) {
        checkInternalComponentsPresence();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }


        Shop shop = shopRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s",
                                    id)));

        return ShopDTO.convertToDTO(shop);
    }

    public ShopDTO updateShopName(UUID id, String name) {
        checkInternalComponentsPresence();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name parameter is null");
        }

        Shop shop = shopRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s",
                                    id)));
        shop.setName(name);

        Shop updatedShop = shopRepo.saveAndFlush(shop);
        return ShopDTO.convertToDTO(updatedShop);
    }

    public ShopDTO activateShopById(UUID id) {
        checkInternalComponentsPresence();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        Shop shop = shopRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s",
                                    id

                                                                                 )));

        shop.setActive(true);
        Shop updatedShop = shopRepo.saveAndFlush(shop);

        return ShopDTO.convertToDTO(updatedShop);
    }

    public ShopDTO deactivateShopById(UUID id) {
        checkInternalComponentsPresence();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        Shop shop = shopRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s",
                                    id

                                                                                 )));

        shop.setActive(false);
        Shop updatedShop = shopRepo.saveAndFlush(shop);

        return ShopDTO.convertToDTO(updatedShop);
    }
}