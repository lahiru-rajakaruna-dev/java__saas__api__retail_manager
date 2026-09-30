package lahiru_rajakaruna.retail_manager.SaleItem;


import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.CreateSaleItemDTO;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.PatchSaleItemDTO;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.SaleItemResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sales/{saleId}/items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    @GetMapping
    public ResponseEntity<List<SaleItemResponseDTO>> getItems(@PathVariable UUID saleId) {
        List<SaleItemResponseDTO> items = saleItemService.getItemsBySaleId(saleId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(items);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<SaleItemResponseDTO> getItem(@PathVariable UUID saleId, @PathVariable UUID itemId) {
        SaleItemResponseDTO item = saleItemService.getItemById(saleId, itemId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(item);
    }

    @PostMapping
    public ResponseEntity<SaleItemResponseDTO> addItem(@PathVariable UUID saleId,
                                                       @RequestBody CreateSaleItemDTO dto) {
        SaleItemResponseDTO created = saleItemService.addItem(saleId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(created);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<SaleItemResponseDTO> patchItem(@PathVariable UUID saleId, @PathVariable UUID itemId,
                                                         @RequestBody PatchSaleItemDTO updates) {
        SaleItemResponseDTO updated = saleItemService.patchItem(saleId, itemId, updates);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(updated);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable UUID saleId, @PathVariable UUID itemId) {
        saleItemService.removeItem(saleId, itemId);
        return ResponseEntity.noContent()
                             .build();
    }
}
