/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author bl4z3
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

	private UUID id;

	private UUID shopId;

	private BigDecimal total;

	private Boolean isClosed;

	public static Sale convertToEntity(SaleDTO dto, Shop shop) {
		if (shop == null) {
			throw new IllegalArgumentException("Shop not provided");
		}

		Sale sale = new Sale();

		if (dto.getTotal().isPresent()) {
			sale.setTotal(dto.getTotal().get());
		}
		if (dto.getIsClosed().isPresent()) {
			sale.setClosed(Boolean.TRUE.equals(dto.getIsClosed().get()));
		}
		sale.setShop(shop);
		return sale;
	}

	public static SaleDTO convertToDTO(Sale sale) {
		if (sale.getShop() == null) {
			throw new NullPointerException("Shop not found");

		}

		SaleDTO dto = new SaleDTO();

		dto.setId(sale.getId());
		dto.setShopId(sale.getShop().getId());
		dto.setTotal(sale.getTotal());
		dto.setIsClosed(sale.isClosed());
		return dto;
	}

	public Optional<UUID> getId() {
		return Optional.ofNullable(id);
	}

	public Optional<UUID> getShopId() {
		return Optional.ofNullable(shopId);
	}

	public Optional<BigDecimal> getTotal() {
		return Optional.ofNullable(total);
	}

	public Optional<Boolean> getIsClosed() {
		return Optional.ofNullable(isClosed);
	}

}
