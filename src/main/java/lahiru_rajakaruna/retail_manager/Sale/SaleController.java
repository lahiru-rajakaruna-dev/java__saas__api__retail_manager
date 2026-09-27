/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bl4z3
 */
@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

	private final SaleService saleService;

	public SaleController(SaleService saleService) {
		this.saleService = saleService;
	}

	@GetMapping()
	public ResponseEntity<SaleDTO> findSaleById(@RequestParam UUID id) throws RuntimeException {
		if (id == null) {
			throw new RuntimeException("ID not provided");
		}

		SaleDTO sale = saleService.findById(id);
		return ResponseEntity.ok(sale);
	}

	@GetMapping("/by-shop")
	public ResponseEntity<List<SaleDTO>> findSalesByShop(@RequestParam UUID shopId) throws RuntimeException {
		if (shopId == null) {
			throw new RuntimeException("Shop ID not provided");
		}

		List<SaleDTO> sales = saleService.findByShopId(shopId);
		return ResponseEntity.ok(sales);
	}

	@PostMapping()
	public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO sale) throws RuntimeException {
		if (sale.getShopId() == null) {
			throw new RuntimeException("Must provide a shop for the sale");
		}

		sale.setTotal(BigDecimal.ZERO);
		sale.setIsClosed(false);
		SaleDTO createdSale = saleService.createSale(sale);
		return ResponseEntity.ok(createdSale);
	}

	@PatchMapping()
	public ResponseEntity<SaleDTO> patchSale(@RequestParam UUID id, @RequestBody SaleDTO sale) throws RuntimeException {
		if (id == null) {
			throw new RuntimeException("ID is not provided");
		}

		if (sale.getTotal().isPresent()) {
			SaleDTO updatedSale = saleService.updateSaleTotal(id, sale.getTotal().get());
			return ResponseEntity.ok(updatedSale);
		}

		if (sale.getIsClosed().isPresent()) {
			if (Boolean.TRUE.equals(sale.getIsClosed().get())) {
				SaleDTO closedSale = saleService.closeSaleById(id);
				return ResponseEntity.ok(closedSale);
			}
		}

		throw new RuntimeException("Invalid Request");

	}

}
