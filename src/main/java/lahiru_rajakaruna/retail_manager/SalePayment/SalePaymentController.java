package lahiru_rajakaruna.retail_manager.SalePayment;


import jakarta.validation.Valid;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.CreateSalePaymentDTO;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.PatchSalePaymentDTO;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.ResponseSalePaymentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sale-payments")
public class SalePaymentController {

    private final SalePaymentService salePaymentService;

    public SalePaymentController(SalePaymentService salePaymentService) {
        this.salePaymentService = salePaymentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSalePaymentDTO> getPayment(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(salePaymentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseSalePaymentDTO> getPaymentOfSale(@RequestParam UUID saleId) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(salePaymentService.findBySaleId(saleId));
    }

    @PostMapping
    public ResponseEntity<ResponseSalePaymentDTO> createPayment(@Valid @RequestBody CreateSalePaymentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(salePaymentService.createSalePayment(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseSalePaymentDTO> patchPayment(@PathVariable UUID id,
                                                               @Valid @RequestBody PatchSalePaymentDTO updates) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(salePaymentService.patchById(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        salePaymentService.deleteById(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
