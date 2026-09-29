package lahiru_rajakaruna.retail_manager.Sale;

import java.math.BigDecimal;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.SaleResponseDTO;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

public class SaleMapper {
	private SaleMapper() {
	}

	public static SaleResponseDTO convertToDTO(Sale sale) {

		boolean isShopNull = sale.getShop() == null;
		boolean isShopIdNull = !isShopNull && sale.getShop().getId() == null;
		boolean isTotalNull = sale.getTotal() == null;
		boolean isSaleStateNull = sale.getSaleState() == null;

		if (isShopNull || isShopIdNull) {
			throw new IllegalArgumentException("Cannot Convert: Shop cannot be null");
		}
		if (isTotalNull) {
			throw new IllegalArgumentException("Cannot Convert: Total cannot be null");
		}
		if (isSaleStateNull) {
			throw new IllegalArgumentException("Cannot Convert: Sale state cannot be null");
		}

		SaleResponseDTO dto = new SaleResponseDTO();

		dto.setId(sale.getId());
		dto.setShopId(sale.getShop().getId());
		dto.setTotal(sale.getTotal());
		dto.setSaleState(sale.getSaleState());

		return dto;
	}

	public static Sale getNewSale(Shop shop) {
		boolean isShopNull = shop == null;

		Sale sale = new Sale();
		sale.setShop(shop);
		sale.setTotal(new BigDecimal(0));
		sale.setSaleState(ESaleState.OPEN);
		return sale;
	}

}
