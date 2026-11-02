package lahiru_rajakaruna.retail_manager.ProductTests;

import lahiru_rajakaruna.retail_manager.Product.EMessurmentUnit;
import lahiru_rajakaruna.retail_manager.Product.IProductRepository;
import lahiru_rajakaruna.retail_manager.Product.ProductService;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


//TODO: ADD TEST BODIES
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProductService.class})
public class ProductServiceTests {
    @Autowired
    private ProductService productService;

    @MockitoBean
    private IProductRepository productRepo;

    @MockitoBean
    private IShopRepository shopRepo;

    public ProductServiceTests() {
    }

    //    CREATE PRODUCT
    @Test
    public void createProductShouldThrowWhenParameterAreNull() {
        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(null, "Product 1", BigDecimal.valueOf(100L), BigDecimal.valueOf(230L), EMessurmentUnit.G, BigDecimal.valueOf(1));
                  })
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), null, BigDecimal.valueOf(100L), BigDecimal.valueOf(230L), EMessurmentUnit.G, BigDecimal.valueOf(1));
                  })
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", null, BigDecimal.valueOf(230L), EMessurmentUnit.G, BigDecimal.valueOf(1));
                  })
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(100L), null, EMessurmentUnit.G, BigDecimal.valueOf(1));
                  })
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(100L), BigDecimal.valueOf(230L), null, BigDecimal.valueOf(1));
                  })
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(100L), BigDecimal.valueOf(230L), EMessurmentUnit.G, null);
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createProductShouldThrowWhenPriceIsNegative() {
        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(-30L), BigDecimal.valueOf(500L), EMessurmentUnit.G, BigDecimal.valueOf(10L));
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createProductShouldThrowWhenQuantityIsNegative() {
        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(30L), BigDecimal.valueOf(-500L), EMessurmentUnit.G, BigDecimal.valueOf(10L));
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createProductShouldThrowWhenSaleUnitIsNegative() {
        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(30L), BigDecimal.valueOf(500L), EMessurmentUnit.G, BigDecimal.valueOf(-10L));
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createProductShouldThrowWhenNoMatchingShopFound() {
        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> {
                      productService.createProduct(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(30L), BigDecimal.valueOf(500L), EMessurmentUnit.G, BigDecimal.valueOf(-10L));
                  })
                  .isInstanceOf(RuntimeException.class);
    }


    //    FIND BY ID
    public void findByIdShouldThrowWhenIdIsNull() {
    }

    public void findByIdShouldThrowWhenNoMatchingEntityFound() {
    }

    public void findByIdShouldReturnResponseProductDTOWhenMatched() {
    }

    //    FIND BY SHOP ID
    public void findByShopIdShouldThrowWhenIdIsNull() {
    }

    public void findByShopIdShouldReturnAnEmptyListWhenNoMatchingEntities() {
    }

    public void findByShopIdShouldReturnListOfResponseProductDTOsWhenMatched() {
    }

    //    UPDATE PRODUCT NAME
    public void updateProductNameShouldThrowWhenParametersAreNull() {
    }

    public void updateProductNameShouldThrowWhenNameParameterIsEmpty() {
    }

    public void updateProductNameShouldThrowWhenNoMatchingEntityFound() {
    }

    public void updateProductNameShouldHaveSavedUpdatedEntity() {
    }

    public void updateProductNameShouldReturnResponseProductDTOContainingUpdatedEntityOnSuccessfulOperation() {
    }

    //    UPDATE PRODUCT PRICE
    public void updateProductPriceShouldThrowWhenParametersAreNull() {
    }

    public void updateProductPriceShouldThrowWhenPriceIsNegative() {
    }

    public void updateProductPriceShouldThrowWhenNoMatchingEntity() {
    }

    public void updateProductPriceShouldHaveSavedUpdateEntity() {
    }

    public void updateProductPriceShouldReturnResponseProductDTOContainingUpdatedEntityOnSuccessfulOperation() {
    }

    //   UPDATE PRODUCT QUANTITY
    public void updateProductQuantityShouldThrowWhenParametersAreNull() {
    }

    public void updateProductQuantityShouldThrowWhenQuantityIsNegative() {
    }

    public void updateProductQuantityShouldThrowWhenNoMatchingEntity() {
    }

    public void updateProductQuantityShouldHaveSavedUpdateEntity() {
    }

    public void updateProductQuantityShouldReturnResponseProductDTOContainingUpdatedEntityOnSuccessfulOperation() {
    }

    //   UPDATE PRODUCT UNIT
    public void updateProductUnitShouldThrowWhenParametersAreNull() {
    }

    public void updateProductUnitShouldThrowWhenNoMatchingEntity() {
    }

    public void updateProductUnitShouldHaveSavedUpdateEntity() {
    }

    public void updateProductUnitShouldReturnResponseProductDTOContainingUpdatedEntityOnSuccessfulOperation() {
    }


    //   ACTIVATE PRODUCT BY ID
    public void activateProductByIdShouldThrowWhenParametersAreNull() {
    }

    public void activateProductByIdShouldThrowWhenNoMatchingEntity() {
    }

    public void activateProductByIdShouldHaveSavedUpdateEntity() {
    }

    public void activateProductByIdShouldReturnResponseProductDTOContainingUpdatedEntityOnSuccessfulOperation() {
    }


    //   DEACTIVATE PRODUCT BY ID
    public void deactivateProductByIdShouldThrowWhenParametersAreNull() {
    }

    public void deactivateProductByIdShouldThrowWhenNoMatchingEntity() {
    }

    public void deactivateProductByIdShouldHaveSavedUpdateEntity() {
    }

    public void deactivateProductByIdShouldReturnResponseProductDTOContainingUpdatedEntityOnSuccessfulOperation() {
    }
}
