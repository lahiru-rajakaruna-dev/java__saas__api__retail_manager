/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Product.DTOs.PatchProductDTO;
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
    public ResponseProductDTO createProduct(UUID shopID, String productName, BigDecimal unitPrice, BigDecimal quantity, EMessurmentUnit unit, BigDecimal saleUnit) {
        requireNonNull(productName, "Must provide a name for the product");
        requireNonNull(shopID,
                       "Must provide a shop for the product");
        requireNonNull(unitPrice,
                       "Must provide a price for the product");
        requireNonNull(quantity,
                       "Must provide a quantity for the product");
        requireNonNull(unit,
                       "Must provide a measurement unit for the product");
        requireNonNull(saleUnit,
                       "Must provide a sale unit for the product");
        requireNonNegative(unitPrice, "Must provided a valid price");
        requireNonNegative(quantity, "Must provided a valid quantity");
        requireNonNegative(saleUnit, "Must provided a valid sale unit");


        Shop shop = shopRepo.findById(shopID)
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            String.format(
                                                    "Could not find shop with ID: %s",
                                                    shopID
                                            )
                                    )
                            );

        Product product = new Product(shop, productName, unitPrice, quantity, unit, EActiveState.INACTIVE);
        Product savedProduct = productRepo.saveAndFlush(product);
        return ProductMapper.convertToDTO(savedProduct);
    }

    @Transactional
    public ResponseProductDTO findById(UUID id) {
        requireNonNull(id, "ID parameter is null");
        Product product = findProductOrThrow(id);
        return ProductMapper.convertToDTO(product);
    }

    @Transactional
    public List<ResponseProductDTO> findByShopId(UUID shopId) {
        requireNonNull(shopId, "Shop ID parameter is null");
        return productRepo.findAllByShopId(shopId)
                          .stream()
                          .map(ProductMapper::convertToDTO)
                          .toList();
    }

    @Transactional
    public ResponseProductDTO patchById(UUID id, PatchProductDTO updates) {
        requireNonNull(id, "ID parameter is null");
        boolean entityModified = false;
        Product product = findProductOrThrow(id);

        if (updates.getName() != null) {
            entityModified = true;
            updateProductName(product, updates.getName());
        }
        if (updates.getPrice() != null) {
            entityModified = true;
            updateProductPrice(product, updates.getPrice());
        }
        if (updates.getQuantity() != null) {
            entityModified = true;
            updateProductQuantity(product, updates.getQuantity());
        }
        if (updates.getUnit() != null) {
            entityModified = true;
            updateProductUnit(product, updates.getUnit());
        }

        if (updates.getState() != null) {
            entityModified = true;
            if (EActiveState.ACTIVE.equals(updates.getState())) {
                activateProductById(product);
            } else if (EActiveState.INACTIVE.equals(updates.getState())) {
                deactivateProductById(product);
            } else {
                throw new IllegalArgumentException("Illegal product state");
            }
        }

        Product savedProduct = null;
        if (entityModified) {
            savedProduct = productRepo.saveAndFlush(product);
        }
        return ProductMapper.convertToDTO(savedProduct != null ? savedProduct : product);
    }

    private Product updateProductName(Product product, String name) {
        requireNonNull(product, "Product parameter is null");
        requireNonNull(name, "Name parameter is null");
        requireNonEmptyAndNonBlank(name, "Invalid name");
        product.setName(name);
        return product;
    }

    private Product updateProductPrice(Product product, BigDecimal price) {
        requireNonNull(product, "Product parameter is null");
        requireNonNull(price, "Price parameter is null");
        requireNonNegative(price, "Price cannot be negative");
        product.setPrice(price);
        return product;
    }

    private Product updateProductQuantity(Product product, BigDecimal quantity) {
        requireNonNull(product, "Product parameter is null");
        requireNonNull(quantity, "Quantity parameter is null");
        requireNonNegative(quantity, "Quantity cannot be negative");
        product.setQuantity(quantity);
        return product;
    }

    private Product updateProductUnit(Product product, EMessurmentUnit unit) {
        requireNonNull(product, "Product parameter is null");
        requireNonNull(unit, "Unit parameter is null");
        product.setUnit(unit);
        return product;
    }

    private Product activateProductById(Product product) {
        requireNonNull(product, "Product parameter is null");
        product.setActiveState(EActiveState.ACTIVE);
        return product;
    }

    private Product deactivateProductById(Product product) {
        requireNonNull(product, "Product parameter is null");
        product.setActiveState(EActiveState.INACTIVE);
        return product;
    }

    private void requireNonNegative(BigDecimal value, String message) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(message);
        }
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

// TODO: MAKE THE INIDIVIDUAL PROPERTY UPDATE METHODS PRIVATE
// TODO: MAKE THE PATCHBYID THE ENTRY POINT FOR ALL UPDATES
// TODO: PUT THE TRANSACTION ON THE PATCHBYID