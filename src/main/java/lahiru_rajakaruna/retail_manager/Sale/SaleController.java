/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import lahiru_rajakaruna.retail_manager.Sale.DTOs.CloseSaleDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.CreateSaleDTO;
import lahiru_rajakaruna.retail_manager.Sale.DTOs.SaleResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSale(@RequestParam UUID id) {
        SaleResponseDTO sale = saleService.findSaleById(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(sale);
    }

    @GetMapping()
    public ResponseEntity<List<SaleResponseDTO>> getAllSalesOfTheShop(@RequestParam UUID shopId) {
        List<SaleResponseDTO> sales = saleService.findSalesByShopId(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(sales);
    }

    @PostMapping()
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody CreateSaleDTO sale) {
        SaleResponseDTO createdSale = saleService.createSale(sale);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(createdSale);
    }

    @PatchMapping("/{saleId}/close")
    public ResponseEntity<SaleResponseDTO> closeSale(@PathVariable UUID saleId,
            @RequestBody(required = false) CloseSaleDTO closeData) {
        SaleResponseDTO dto = saleService.closeSaleById(saleId, closeData);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(dto);
    }

}
