/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
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

	private ESaleState saleState;

	public static Sale convertToEntity(SaleDTO dto, Shop shop) {
		if (shop == null) {
			throw new IllegalArgumentException("Shop not provided");
		}
		if (dto.getTotal().isEmpty()) {
			throw new IllegalArgumentException(
				"Cannot Convert: Total not provided");
		}
		if (dto.getSaleState().isEmpty()) {
			throw new IllegalArgumentException(
				"Cannot Canvert: Sale state not provided");
		}

		Sale sale = new Sale();
		sale.setTotal(dto.getTotal().get());
		sale.setSaleState(dto.getSaleState().get());
		sale.setShop(shop);
		return sale;
	}

	public static SaleDTO convertToDTO(Sale sale) {
		if (sale.getShop() == null) {
			throw new NullPointerException(
				"Cannot Convert: Shop not found");

		}
		if (sale.getId() == null) {
			throw new NullPointerException(
				"Cannot Convert: ID not found");
		}
		if (sale.getTotal() == null) {
			throw new NullPointerException(
				"Cannot Convert: Total not found");
		}
		if (sale.getSaleState() == null) {
			throw new NullPointerException(
				"Cannot Convert: Sale state not found");
		}

		SaleDTO dto = new SaleDTO();
		dto.setId(sale.getId());
		dto.setShopId(sale.getShop().getId());
		dto.setTotal(sale.getTotal());
		dto.setSaleState(sale.getSaleState());
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

	public Optional<ESaleState> getSaleState() {
		return Optional.ofNullable(saleState);
	}

}
