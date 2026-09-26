/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.springframework.stereotype.Service;

/**
 *
 * @author bl4z3
 */
@Service
public class ProductService {

	private final IProductRepository productRepo;
	private final IShopRepository shopRepo;

	public ProductService(IProductRepository productRepo,
			      IShopRepository shopRepo) {
		this.productRepo = productRepo;
		this.shopRepo = shopRepo;
		checkInternalComponentsPresence();
	}

	private void checkInternalComponentsPresence() {
		Objects.requireNonNull(productRepo,
				       "Product Repository Not Found");
	}

	public ProductDTO createProduct(ProductDTO dto) {
		if (dto.getName() == null || dto.getName().isEmpty()) {
			throw new IllegalArgumentException(
				"Must provide a name for the product");
		}
		if (dto.getShopId() == null) {
			throw new IllegalArgumentException(
				"Must provide a shop for the product");
		}
		if (dto.getPrice() == null) {
			throw new IllegalArgumentException(
				"Must provide a price for the product");
		}
		if (dto.getQuantity() == null) {
			throw new IllegalArgumentException(
				"Must provide a quantity for the product");
		}
		if (dto.getUnit() == null) {
			throw new IllegalArgumentException(
				"Must provide a measurement unit for the product");
		}

		Shop shop = shopRepo.findById(dto.getShopId().get())
			.orElseThrow(
				() -> new RuntimeException(
					String.format(
						"Could not find shop with ID: %s",
						dto.getShopId().get())));

		Product product = ProductDTO.convertToEntity(dto, shop);
		Product savedProduct = productRepo.saveAndFlush(product);
		return ProductDTO.convertToDTO(savedProduct);
	}

	public ProductDTO findById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Product product = findProductOrThrow(id);
		return ProductDTO.convertToDTO(product);
	}

	public List<ProductDTO> findByShopId(UUID shopId) {
		checkInternalComponentsPresence();

		if (shopId == null) {
			throw new IllegalArgumentException(
				"Shop ID parameter is null");
		}

		return productRepo.findAllByShopId(shopId)
			.stream()
			.map(ProductDTO::convertToDTO)
			.toList();
	}

	public ProductDTO updateProductName(UUID id, String name) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException(
				"Name parameter is null or empty");
		}

		Product product = findProductOrThrow(id);
		product.setName(name);

		Product updatedProduct = productRepo.saveAndFlush(product);
		return ProductDTO.convertToDTO(updatedProduct);
	}

	public ProductDTO updateProductPrice(UUID id, BigDecimal price) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}
		if (price == null) {
			throw new IllegalArgumentException(
				"Price parameter is null");
		}
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(
				"Price cannot be negative");
		}

		Product product = findProductOrThrow(id);
		product.setPrice(price);

		Product updatedProduct = productRepo.saveAndFlush(product);
		return ProductDTO.convertToDTO(updatedProduct);
	}

	public ProductDTO updateProductQuantity(UUID id, BigDecimal quantity) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}
		if (quantity == null) {
			throw new IllegalArgumentException(
				"Quantity parameter is null");
		}
		if (quantity.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(
				"Quantity cannot be negative");
		}

		Product product = findProductOrThrow(id);
		product.setQuantity(quantity);

		Product updatedProduct = productRepo.saveAndFlush(product);
		return ProductDTO.convertToDTO(updatedProduct);
	}

	public ProductDTO updateProductUnit(UUID id, MessurementUnit unit) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}
		if (unit == null) {
			throw new IllegalArgumentException(
				"Unit parameter is null");
		}

		Product product = findProductOrThrow(id);
		product.setUnit(unit);

		Product updatedProduct = productRepo.saveAndFlush(product);
		return ProductDTO.convertToDTO(updatedProduct);
	}

	public ProductDTO activateProductById(UUID id) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Product product = findProductOrThrow(id);
		product.setActive(true);

		Product updatedProduct = productRepo.saveAndFlush(product);
		return ProductDTO.convertToDTO(updatedProduct);
	}

	public ProductDTO deactivateProductById(UUID id) {
		checkInternalComponentsPresence();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Product product = findProductOrThrow(id);
		product.setActive(false);

		Product updatedProduct = productRepo.saveAndFlush(product);
		return ProductDTO.convertToDTO(updatedProduct);
	}

	private Product findProductOrThrow(UUID id) {
		return productRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find product with ID: %s",
			id)));
	}
}
