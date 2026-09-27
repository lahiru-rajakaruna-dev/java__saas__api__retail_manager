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

//	FIXME: ADD PATH VARIABLE INSTEAD OF QUERY PARAMETER
	@GetMapping()
	public ResponseEntity<ShopDTO> findShopById(@RequestParam UUID id)
		throws RuntimeException {
		if (id == null) {
			throw new RuntimeException("ID not provided");
		}

		return ResponseEntity.ok(shopService.findById(id));
	}

	@PostMapping()
	public ResponseEntity<ShopDTO> createShop(@RequestBody ShopDTO shop)
		throws RuntimeException {
		if (shop.getName().isEmpty()) {
			throw new RuntimeException("Must provide a shop name");
		}
		return ResponseEntity.ok(shopService.createShop(shop));
	}

	@PatchMapping()
	public ResponseEntity<ShopDTO> patchShop(@RequestParam UUID id,
						 @RequestBody ShopDTO shop)
		throws RuntimeException {
		if (id == null) {
			throw new RuntimeException("ID is not provided");
		}

		if (shop.getName().isPresent()) {
			return ResponseEntity.ok(shopService.updateShopName(
				id,
				shop.getName()
					.get()));
		}

		if (shop.getActiveState().isPresent()) {
			if (shop.getActiveState().get().equals(
				EActiveState.ACTIVE)) {
				return ResponseEntity.ok(shopService
					.activateShopById(id));
			} else {
				return ResponseEntity.ok(shopService
					.deactivateShopById(
						id));
			}
		}

		throw new RuntimeException("Invalid request");
	}
}
