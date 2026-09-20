package lahiru_rajakaruna.retail_manager.Shop;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShopService {
    private final IShopRepository shopRepo;

    public ShopService(IShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    public ShopDTO createShop(ShopDTO shop) {
        if (shop.getName().isEmpty()) {
            throw new RuntimeException("Must provide a name for the shop");
        }

        return ShopDTO.convertToDTO(shopRepo.saveAndFlush(new Shop(
                shop.getName()
                    .get(),
                false
        )));
    }

    public ShopDTO findById(UUID id) {
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        return ShopDTO.convertToDTO(shopRepo.findById(id)
                                            .orElseThrow(() -> new RuntimeException(
                                                    String.format(
                                                            "Could not find shop with ID: %s",
                                                            id.toString()
                                                                 ))));
    }

    public ShopDTO updateShopName(UUID id, ShopDTO updates) {
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        Shop shop = shopRepo.findById(id).orElseThrow(() -> {
            return new RuntimeException(String.format(
                    "Could not find shop with ID: %s",
                    id.toString()
                                                     ));
        });

        if (updates.getName().isPresent()) {
            shop.setName(updates.getName().get());
        }

        return ShopDTO.convertToDTO(shopRepo.saveAndFlush(shop));
    }

    public ShopDTO activateShopById(UUID id) {
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        Shop shop = shopRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s",
                                    id.toString()

                                                                                 )));

        shop.setActive(true);
        return ShopDTO.convertToDTO(shopRepo.saveAndFlush(shop));
    }

    public ShopDTO deactivateShopById(UUID id) {
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        Shop shop = shopRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s",
                                    id.toString()

                                                                                 )));

        shop.setActive(false);
        return ShopDTO.convertToDTO(shopRepo.saveAndFlush(shop));
    }

}