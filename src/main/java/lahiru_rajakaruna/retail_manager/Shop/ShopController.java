package lahiru_rajakaruna.retail_manager.Shop;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lahiru_rajakaruna.retail_manager.Shop.DTOs.CreateShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.PatchShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.ShopResponseDTO;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {

	private final ShopService shopService;

	public ShopController(ShopService shopService) {
		this.shopService = shopService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ShopResponseDTO> findShopById(@PathVariable UUID id) {
		ShopResponseDTO shop = shopService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(shop);
	}

	@PostMapping()
	public ResponseEntity<ShopResponseDTO> createShop(@RequestBody CreateShopDTO shopCreateData) {
		ShopResponseDTO dto = shopService.createShop(shopCreateData);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ShopResponseDTO> patchShop(@PathVariable UUID id, @RequestBody PatchShopDTO patchShopData) {
		ShopResponseDTO dto = shopService.patchShopById(id, patchShopData);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(dto);
	}
}
