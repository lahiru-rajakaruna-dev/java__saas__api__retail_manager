/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Product.DTOs.CreateProductDTO;
import lahiru_rajakaruna.retail_manager.Product.DTOs.ResponseProductDTO;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author bl4z3
 */
@Service
public class ProductService {

    private final IProductRepository productRepo;
    private final IShopRepository shopRepo;

    public ProductService(IProductRepository productRepo,
                          IShopRepository shopRepo) {
        this.productRepo = productRepo;
        this.shopRepo = shopRepo;
        checkInternalComponentsPresence();
    }

    private void checkInternalComponentsPresence() {
        Objects.requireNonNull(productRepo,
                "Product Repository Not Found");
    }


    @Transactional
    public ResponseProductDTO createProduct(CreateProductDTO dto) {
        requireNonNull(dto.getName(), "Must provide a name for the product");
        requireNonNull(dto.getShopId(),
                "Must provide a shop for the product");
        requireNonNull(dto.getPrice(),
                "Must provide a price for the product");
        requireNonNull(dto.getQuantity(),
                "Must provide a quantity for the product");
        requireNonNull(dto.getUnit(),
                "Must provide a measurement unit for the product");

        Shop shop = shopRepo.findById(dto.getShopId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            String.format(
                                                    "Could not find shop with ID: %s",
                                                    dto.getShopId()
                                            )
                                    )
                            );

        Product product = ProductMapper.convertToProduct(dto, shop);
        Product savedProduct = productRepo.saveAndFlush(product);
        return ProductMapper.convertToDTO(savedProduct);
    }

    public ResponseProductDTO findById(UUID id) {
        requireNonNull(id, "ID parameter is null");
        Product product = findProductOrThrow(id);
        return ProductMapper.convertToDTO(product);
    }

    public List<ResponseProductDTO> findByShopId(UUID shopId) {
        requireNonNull(shopId, "Shop ID parameter is null");
        return productRepo.findAllByShopId(shopId)
                          .stream()
                          .map(ProductMapper::convertToDTO)
                          .toList();
    }

    @Transactional
    public ResponseProductDTO updateProductName(UUID id, String name) {
        requireNonNull(id, "ID parameter is null");
        requireNonNull(name, "Name parameter is null");
        requireNonEmptyAndNonBlank(name, "Invalid name");

        Product product = findProductOrThrow(id);
        product.setName(name);

        Product updatedProduct = productRepo.saveAndFlush(product);
        return ProductMapper.convertToDTO(updatedProduct);
    }

    public CreateProductDTO updateProductPrice(UUID id, BigDecimal price) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }
        if (price == null) {
            throw new IllegalArgumentException(
                    "Price parameter is null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative");
        }

        Product product = findProductOrThrow(id);
        product.setPrice(price);

        Product updatedProduct = productRepo.saveAndFlush(product);
        return CreateProductDTO.convertToDTO(updatedProduct);
    }

    public CreateProductDTO updateProductQuantity(UUID id, BigDecimal quantity) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException(
                    "Quantity parameter is null");
        }
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative");
        }

        Product product = findProductOrThrow(id);
        product.setQuantity(quantity);

        Product updatedProduct = productRepo.saveAndFlush(product);
        return CreateProductDTO.convertToDTO(updatedProduct);
    }

    public CreateProductDTO updateProductUnit(UUID id, EMessurmentUnit unit) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }
        if (unit == null) {
            throw new IllegalArgumentException(
                    "Unit parameter is null");
        }

        Product product = findProductOrThrow(id);
        product.setUnit(unit);

        Product updatedProduct = productRepo.saveAndFlush(product);
        return CreateProductDTO.convertToDTO(updatedProduct);
    }

    public CreateProductDTO activateProductById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }

        Product product = findProductOrThrow(id);
        product.setActiveState(EActiveState.ACTIVE);

        Product updatedProduct = productRepo.saveAndFlush(product);
        return CreateProductDTO.convertToDTO(updatedProduct);
    }

    public CreateProductDTO deactivateProductById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }

        Product product = findProductOrThrow(id);
        product.setActiveState(EActiveState.INACTIVE);

        Product updatedProduct = productRepo.saveAndFlush(product);
        return CreateProductDTO.convertToDTO(updatedProduct);
    }

    private void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireNonEmptyAndNonBlank(String value, String message) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private Product findProductOrThrow(UUID id) {
        return productRepo.findById(id)
                          .orElseThrow(() -> new RuntimeException(String.format(
                                  "Could not find product with ID: %s",
                                  id)));
    }
}
