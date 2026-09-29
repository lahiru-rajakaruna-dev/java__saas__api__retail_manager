/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lahiru_rajakaruna.retail_manager.Sale.DTOs.CreateSaleDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.PatchSaleDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.SaleResponseDTO;

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
	public ResponseEntity<SaleResponseDTO> getSale(@RequestParam UUID id) {
		SaleResponseDTO sale = saleService.findSaleById(id);
		return ResponseEntity.status(HttpStatus.OK).body(sale);
	}

	@GetMapping()
	public ResponseEntity<List<SaleResponseDTO>> getAllSalesOfTheShop(@RequestParam UUID shopId) {
		List<SaleResponseDTO> sales = saleService.findSalesByShopId(shopId);
		return ResponseEntity.status(HttpStatus.OK).body(sales);
	}

	@PostMapping()
	public ResponseEntity<SaleResponseDTO> createSale(@RequestBody CreateSaleDTO sale) {
		SaleResponseDTO createdSale = saleService.createSale(sale);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
	}

	@PatchMapping()
	public ResponseEntity<SaleResponseDTO> patchSale(@RequestParam UUID id, @RequestBody PatchSaleDTO saleUpdates) {
		SaleResponseDTO dto = saleService.patchSaleById(id, saleUpdates);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(dto);
	}

}
