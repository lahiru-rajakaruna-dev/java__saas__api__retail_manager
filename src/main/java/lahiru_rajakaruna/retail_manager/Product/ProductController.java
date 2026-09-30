/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import lahiru_rajakaruna.retail_manager.Product.DTOs.CreateProductDTO;
import lahiru_rajakaruna.retail_manager.Product.DTOs.PatchProductDTO;
import lahiru_rajakaruna.retail_manager.Product.DTOs.ResponseProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author bl4z3
 */

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> getProductDetails(@PathVariable UUID id) {
        ResponseProductDTO product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(product);
    }

    @GetMapping
    public ResponseEntity<List<ResponseProductDTO>> getProductsByShop(@RequestParam UUID shopId) {
        List<ResponseProductDTO> products = productService.findByShopId(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(products);
    }

    @PostMapping
    public ResponseEntity<ResponseProductDTO> createProduct(@RequestBody CreateProductDTO productData) {
        ResponseProductDTO product = productService.createProduct(productData);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(product);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> patchProduct(@PathVariable UUID id,
                                                           @RequestBody PatchProductDTO updates) {
        ResponseProductDTO product = productService.patchById(id, updates);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(product);
    }
}
