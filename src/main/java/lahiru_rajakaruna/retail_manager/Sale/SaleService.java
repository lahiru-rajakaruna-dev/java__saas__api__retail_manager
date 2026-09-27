/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.springframework.stereotype.Service;

/**
 * @author bl4z3
 */
@Service
public class SaleService {

	private final ISaleRepository saleRepo;

	private final IShopRepository shopRepo;

	public SaleService(ISaleRepository saleRepo, IShopRepository shopRepo) {
		this.saleRepo = saleRepo;
		this.shopRepo = shopRepo;
		checkInternalComponentsPresence();
	}

	private void checkInternalComponentsPresence() {
		Objects.requireNonNull(saleRepo, "Sale Repository Not Found");
		Objects.requireNonNull(shopRepo, "Shop Repository Not Found");
	}

	public SaleDTO createSale(SaleDTO dto) {
		if (dto.getShopId().isEmpty()) {
			throw new IllegalArgumentException(
				"Must provide a shop for the sale");
		}

		dto.setTotal(BigDecimal.ZERO);
		dto.setSaleState(ESaleState.OPEN);

		Shop shop = shopRepo.findById(dto.getShopId().get())
			.orElseThrow(
				() -> new RuntimeException(
					"Could not find shop with ID: %s"
						.formatted(dto.getShopId().get())));

		Sale sale = SaleDTO.convertToEntity(dto, shop);
		Sale savedSale = saleRepo.saveAndFlush(sale);
		return SaleDTO.convertToDTO(savedSale);
	}

	public SaleDTO findById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Sale sale = findSaleOrThrow(id);
		return SaleDTO.convertToDTO(sale);
	}

	public List<SaleDTO> findByShopId(UUID shopId) {
		checkInternalComponentsPresence();

		if (shopId == null) {
			throw new IllegalArgumentException(
				"Shop ID parameter is null");
		}

		return saleRepo.findAllByShopId(shopId).stream().map(
			SaleDTO::convertToDTO).toList();
	}

	public SaleDTO updateSaleTotal(UUID id, BigDecimal total) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}
		if (total == null) {
			throw new IllegalArgumentException(
				"Total parameter is null");
		}
		if (total.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(
				"Sale total cannot be negative");
		}

		Sale sale = findSaleOrThrow(id);

		if (sale.getSaleState().equals(ESaleState.CLOSED)) {
			throw new IllegalStateException(
				"Cannot update the total of a closed sale");
		}

		sale.setTotal(total);
		Sale updatedSale = saleRepo.saveAndFlush(sale);
		return SaleDTO.convertToDTO(updatedSale);
	}

	public SaleDTO closeSaleById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Sale sale = findSaleOrThrow(id);

		if (sale.getSaleState().equals(ESaleState.OPEN)) {
			sale.setSaleState(ESaleState.CLOSED);
		}

		Sale updatedSale = saleRepo.saveAndFlush(sale);
		return SaleDTO.convertToDTO(updatedSale);
	}

	private Sale findSaleOrThrow(UUID id) {
		return saleRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find sale with ID: %s", id)));
	}

}
