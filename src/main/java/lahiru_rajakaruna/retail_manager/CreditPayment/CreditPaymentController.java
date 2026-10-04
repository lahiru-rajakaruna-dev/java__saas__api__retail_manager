package lahiru_rajakaruna.retail_manager.CreditPayment;

import lahiru_rajakaruna.retail_manager.CreditPayment.DTOs.CreateCreditPaymentDTO;
import lahiru_rajakaruna.retail_manager.CreditPayment.DTOs.ResponseCreditPaymentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Payments are an immutable ledger: they can be recorded and read, but never patched or deleted.
 */
@RestController
@RequestMapping("/api/v1/credit/{creditAccountId}/credit-payments")
public class CreditPaymentController {

    private final CreditPaymentService creditPaymentService;

    public CreditPaymentController(CreditPaymentService creditPaymentService) {
        this.creditPaymentService = creditPaymentService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseCreditPaymentDTO>> getCreditPayments(@PathVariable UUID creditAccountId) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditPaymentService.findByCreditAccountId(creditAccountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCreditPaymentDTO> getCreditPayment(@PathVariable UUID creditAccountId,
                                                                     @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditPaymentService.findById(creditAccountId, id));
    }

    @PostMapping
    public ResponseEntity<ResponseCreditPaymentDTO> createCreditPayment(@PathVariable UUID creditAccountId,
                                                                        @RequestBody CreateCreditPaymentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(creditPaymentService.createPayment(creditAccountId, dto));
    }
}
