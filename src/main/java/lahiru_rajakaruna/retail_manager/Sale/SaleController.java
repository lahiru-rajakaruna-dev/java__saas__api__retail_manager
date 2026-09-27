/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import java.util.List;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
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
	public ResponseEntity<SaleDTO> findSaleById(@RequestParam UUID id) {
		SaleDTO sale = saleService.findById(id);
		return ResponseEntity.ok(sale);
	}

	@GetMapping("/by-shop")
	public ResponseEntity<List<SaleDTO>> findSalesByShop(
		@RequestParam UUID shopId) {
		List<SaleDTO> sales = saleService.findByShopId(shopId);
		return ResponseEntity.ok(sales);
	}

	@PostMapping()
	public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO sale) {
		SaleDTO createdSale = saleService.createSale(sale);
		return ResponseEntity.ok(createdSale);
	}

	@PatchMapping()
	public ResponseEntity<SaleDTO> patchSale(@RequestParam UUID id,
						 @RequestBody SaleDTO saleUpdates) {
		if (saleUpdates.getTotal().isPresent()) {
			saleService.updateSaleTotal(id,
						    saleUpdates
							    .getTotal()
							    .get());
		}

		if (saleUpdates.getSaleState().isPresent()) {
			if (saleUpdates.getSaleState().get().equals(
				ESaleState.CLOSED)) {
				saleService.closeSaleById(
					id);
			}
		}

		return ResponseEntity.ok(saleService.findById(id));
	}

}
