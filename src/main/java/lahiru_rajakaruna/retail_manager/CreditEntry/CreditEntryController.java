package lahiru_rajakaruna.retail_manager.CreditEntry;


import lahiru_rajakaruna.retail_manager.CreditEntry.DTOs.ResponseCreditEntryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Read-only: credit entries are created by the sale flow and their received amounts
 * change only through credit payments, so there are no POST/PATCH endpoints here.
 */
@RestController
@RequestMapping("/api/v1/credit/{creditAccountId}/credit-entries")
public class CreditEntryController {

    private final CreditEntryService creditEntryService;

    public CreditEntryController(CreditEntryService creditEntryService) {
        this.creditEntryService = creditEntryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCreditEntryDTO> getCreditEntry(@PathVariable UUID creditAccountId, @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditEntryService.findById(creditAccountId, id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseCreditEntryDTO>> getCreditEntriesByAccount(
            @PathVariable UUID creditAccountId,
            @RequestParam(defaultValue = "false") boolean outstandingOnly) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditEntryService.findByCreditAccountId(creditAccountId, outstandingOnly));
    }

    @GetMapping("/by-sale/{saleId}")
    public ResponseEntity<ResponseCreditEntryDTO> getCreditEntryBySale(@PathVariable UUID creditAccountId, @PathVariable UUID saleId) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditEntryService.findBySaleId(creditAccountId, saleId));
    }
}