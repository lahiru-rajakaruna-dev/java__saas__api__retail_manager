package lahiru_rajakaruna.retail_manager.Shop;

import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {

	private final ShopService shopService;

	public ShopController(ShopService shopService) {
		this.shopService = shopService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ShopDTO> findShopById(@PathVariable UUID id) {
		return ResponseEntity.ok(shopService.findById(id));
	}

	@PostMapping()
	public ResponseEntity<ShopDTO> createShop(@RequestBody ShopDTO shop) {
		return ResponseEntity.ok(shopService.createShop(shop));
	}

	@PatchMapping()
	public ResponseEntity<ShopDTO> patchShop(@RequestParam UUID id,
						 @RequestBody ShopDTO shop) {
		if (shop.getName().isPresent()) {
			shopService.updateShopName(
				id,
				shop.getName()
					.get());
		}

		if (shop.getActiveState().isPresent()) {
			if (shop.getActiveState().get().equals(
				EActiveState.ACTIVE)) {
				shopService
					.activateShopById(id);
			} else {
				shopService
					.deactivateShopById(
						id);
			}
		}

		return ResponseEntity.ok(shopService.findById(id));
	}
}
