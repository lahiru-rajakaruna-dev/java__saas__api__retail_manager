/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bl4z3
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getProductDetails(
		@PathVariable UUID id) throws RuntimeException {
		if (id == null) {
			throw new RuntimeException("ID is not provided");
		}

		ProductDTO product = this.productService.findById(id);
		return ResponseEntity.ok(product);
	}

	@GetMapping
	public ResponseEntity<List<ProductDTO>> getProductsByShop(
		@RequestParam UUID shopId) throws RuntimeException {
		if (shopId == null) {
			throw new RuntimeException("ID is not provided");
		}

		List<ProductDTO> products = this.productService.findByShopId(
			shopId);
		return ResponseEntity.ok(products);
	}

	@PostMapping
	public ResponseEntity<ProductDTO> createProduct(
		@RequestBody ProductDTO productData) throws RuntimeException {
		ProductDTO product = productService.createProduct(productData);
		return ResponseEntity.ok(product);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ProductDTO> patchProduct(
		@RequestBody ProductDTO updates, @PathVariable UUID id)
		throws RuntimeException {
		if (id == null) {
			throw new RuntimeException("ID is not provided");
		}

		if (updates.getName().isPresent()) {
			productService.updateProductName(id, updates.getName()
							 .get());
		}
		if (updates.getPrice().isPresent()) {
			productService.updateProductPrice(id, updates.getPrice()
							  .get());
		}
		if (updates.getQuantity().isPresent()) {
			productService.updateProductQuantity(id, updates
							     .getQuantity()
							     .get());
		}
		if (updates.getUnit().isPresent()) {
			productService.updateProductUnit(id, updates.getUnit()
							 .get());
		}
		if (updates.getActiveState().isPresent()) {
			if (updates.getActiveState().get()) {
				productService.activateProductById(id);
			} else {
				productService.deactivateProductById(id);
			}
		}

		return ResponseEntity.ok(productService.findById(id));

	}

}
