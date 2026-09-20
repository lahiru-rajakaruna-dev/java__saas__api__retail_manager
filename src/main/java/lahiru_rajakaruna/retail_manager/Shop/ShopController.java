package lahiru_rajakaruna.retail_manager.Shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/shops")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping()
    public ShopDTO findShopById(@RequestParam UUID id) throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("ID not provided");
        }

        return shopService.findById(id);
    }

    @PostMapping()
    public ShopDTO createShop(@RequestBody ShopDTO shop)
    throws RuntimeException {
        if (shop.getName().isEmpty()) {
            throw new RuntimeException("Must provide a shop name");
        }

        shop.setActive(false);
        return shopService.createShop(shop);
    }

    @PatchMapping()
    public ShopDTO patchShop(@RequestParam UUID id, @RequestBody ShopDTO shop)
    throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("ID is not provided");
        }

        if (shop.getName().isPresent()) {
            shopService.updateShopName(id, shop.getName().get());
        }

        if (shop.isActive()) {
            return shopService.activateShopById(id);
        } else {
            return shopService.deactivateShopById(id);
        }
    }
}
