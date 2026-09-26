/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author bl4z3
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProductDTO {

	private UUID id;
	private UUID shopId;
	private String name;
	private BigDecimal price;
	private BigDecimal quantity;
	private MessurementUnit unit;
	private boolean isActive;

	public Optional<UUID> getShopId() {
		return Optional.ofNullable(shopId);
	}

	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}

	public Optional<BigDecimal> getPrice() {
		return Optional.ofNullable(price);
	}

	public Optional<BigDecimal> getQuantity() {
		return Optional.ofNullable(quantity);
	}

	public Optional<MessurementUnit> getUnit() {
		return Optional.ofNullable(unit);
	}

	public Optional<Boolean> getActiveState() {
		return Optional.ofNullable(isActive);
	}

	public Optional<UUID> getId() {
		return Optional.ofNullable(id);
	}

	public static Product convertToEntity(ProductDTO dto, Shop shop) {
		if (shop == null) {
			throw new IllegalArgumentException(
				"Shop is not provided");
		}

		Product product = new Product();

		if (dto.getId().
			isPresent()) {
			product.setId(dto.getId().
				get());
		}
		if (dto.getName().
			isPresent()) {
			product.setName(dto.getName().
				get());
		}
		if (dto.getPrice().
			isPresent()) {
			product.setPrice(dto.getPrice().
				get());
		}
		if (dto.getQuantity().
			isPresent()) {
			product.setQuantity(dto.getQuantity().
				get());
		}
		if (dto.getUnit().isPresent()) {
			product.setUnit(dto.getUnit().get());
		}
		if (dto.getActiveState().isPresent()) {
			product.setActive(dto.getActiveState().get());
		}
		product.setShop(shop);

		return product;
	}

	public static ProductDTO convertToDTO(Product entity) {
		ProductDTO dto = new ProductDTO();

		if (entity.getShop() == null) {
			throw new NullPointerException("Shop not found");
		}

		dto.setId(entity.getId());
		dto.setShopId(
			entity.getShop().
				getId()
		);
		dto.setName(entity.getName());
		dto.setPrice(entity.getPrice());
		dto.setQuantity(entity.getQuantity());
		dto.setActive(entity.isActive());

		return dto;
	}
}
