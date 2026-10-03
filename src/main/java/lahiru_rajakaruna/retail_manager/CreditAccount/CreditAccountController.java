package lahiru_rajakaruna.retail_manager.CreditAccount;


import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.CreateCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.PatchCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.ResponseCreditAccountDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/credit-accounts")
public class CreditAccountController {

    private final CreditAccountService creditAccountService;

    public CreditAccountController(CreditAccountService creditAccountService) {
        this.creditAccountService = creditAccountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCreditAccountDTO> getCreditAccount(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditAccountService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseCreditAccountDTO>> getCreditAccountsByShop(
            @RequestParam UUID shopId,
            @RequestParam(required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditAccountService.findByShopId(shopId, search));
    }

    @PostMapping
    public ResponseEntity<ResponseCreditAccountDTO> createCreditAccount(@RequestBody CreateCreditAccountDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(creditAccountService.createCreditAccount(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseCreditAccountDTO> patchCreditAccount(@PathVariable UUID id,
                                                                       @RequestBody PatchCreditAccountDTO updates) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(creditAccountService.patchById(id, updates));
    }
}
