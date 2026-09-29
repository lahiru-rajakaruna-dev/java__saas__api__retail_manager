/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.CreateSaleDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.SaleResponseDTO;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

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

	public SaleResponseDTO createSale(CreateSaleDTO dto) {
		boolean isShopIdNull = dto.getShopId() == null;
		if (isShopIdNull) {
			throw new IllegalArgumentException("Must provide a shop for the sale");
		}

		Shop shop = shopRepo.findById(dto.getShopId())
				.orElseThrow(() -> new RuntimeException("Could not find shop with ID: %s".formatted(dto.getShopId())));

		Sale newSale = SaleMapper.getNewSale(shop);
		Sale savedSale = saleRepo.saveAndFlush(newSale);
		return SaleMapper.convertToDTO(savedSale);
	}

	public SaleResponseDTO findSaleById(UUID id) {
		boolean isIdNull = id == null;
		if (isIdNull) {
			throw new IllegalArgumentException("ID parameter is null");
		}

		Sale sale = findSaleOrThrow(id);
		return SaleMapper.convertToDTO(sale);
	}

	public List<SaleResponseDTO> findSalesByShopId(UUID shopId) {
		boolean isShopIdNull = shopId == null;
		if (isShopIdNull) {
			throw new IllegalArgumentException("Shop ID parameter is null");
		}

		List<SaleResponseDTO> sales = saleRepo.findAllByShopId(shopId).stream().map(SaleMapper::convertToDTO).toList();
		return sales;
	}

	public SaleResponseDTO updateSaleTotal(UUID id, BigDecimal total) {
		boolean isIdNull = id == null;
		boolean isTotalNull = total == null;
		boolean isTotalLessThanZero = !isTotalNull && total.compareTo(BigDecimal.ZERO) < 0;

		if (isIdNull) {
			throw new IllegalArgumentException("ID parameter is null");
		}
		if (isTotalNull) {
			throw new IllegalArgumentException("Total parameter is null");
		}
		if (isTotalLessThanZero) {
			throw new IllegalArgumentException("Sale total cannot be negative");
		}

		Sale sale = findSaleOrThrow(id);

		if (sale.getSaleState().equals(ESaleState.CLOSED)) {
			throw new IllegalStateException("Cannot update the total of a closed sale");
		}

		sale.setTotal(total);
		Sale updatedSale = saleRepo.saveAndFlush(sale);
		return SaleMapper.convertToDTO(updatedSale);
	}

	public SaleResponseDTO closeSaleById(UUID id) {

		if (id == null) {
			throw new IllegalArgumentException("ID parameter is null");
		}

		Sale sale = findSaleOrThrow(id);

		if (sale.getSaleState().equals(ESaleState.OPEN)) {
			sale.setSaleState(ESaleState.CLOSED);
		}

		Sale updatedSale = saleRepo.saveAndFlush(sale);
		return SaleResponseDTO.convertToDTO(updatedSale);
	}

	private Sale findSaleOrThrow(UUID id) {
		return saleRepo.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Could not find sale with ID: %s", id)));
	}

}
