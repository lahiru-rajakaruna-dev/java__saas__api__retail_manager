package lahiru_rajakaruna.retail_manager.ProductTests;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Product.DTOs.PatchProductDTO;
import lahiru_rajakaruna.retail_manager.Product.DTOs.ResponseProductDTO;
import lahiru_rajakaruna.retail_manager.Product.EMessurmentUnit;
import lahiru_rajakaruna.retail_manager.Product.IProductRepository;
import lahiru_rajakaruna.retail_manager.Product.Product;
import lahiru_rajakaruna.retail_manager.Product.ProductService;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProductService.class})
public class ProductServiceTests {

    private static final String NAME = "Product 1";
    private static final BigDecimal PRICE = BigDecimal.valueOf(100L);
    private static final BigDecimal QUANTITY = BigDecimal.valueOf(230L);
    private static final EMessurmentUnit UNIT = EMessurmentUnit.G;

    @Autowired
    private ProductService productService;

    @MockitoBean
    private IProductRepository productRepo;

    @MockitoBean
    private IShopRepository shopRepo;

    public ProductServiceTests() {
    }

    // ---------- helpers ----------

    private Shop newShop() {
        Shop shop = new Shop("Shop 1", EActiveState.ACTIVE);
        shop.setId(UUID.randomUUID());
        return shop;
    }

    private Product newProduct(Shop shop) {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setShop(shop);
        product.setName(NAME);
        product.setPrice(PRICE);
        product.setQuantity(QUANTITY);
        product.setUnit(UNIT);
        product.setActiveState(EActiveState.INACTIVE);
        return product;
    }

    /**
     * Builds a patch DTO through the setters, so the tests keep compiling if PatchProductDTO gains fields.
     * Null arguments are skipped because the price/quantity setters reject null.
     */
    private PatchProductDTO patch(String name, BigDecimal price, BigDecimal quantity, EMessurmentUnit unit,
            EActiveState state) {
        PatchProductDTO dto = new PatchProductDTO();
        if (name != null) dto.setName(name);
        if (price != null) dto.setPrice(price);
        if (quantity != null) dto.setQuantity(quantity);
        if (unit != null) dto.setUnit(unit);
        if (state != null) dto.setState(state);
        return dto;
    }

    private void stubSaveToReturnArgument() {
        Mockito.when(productRepo.saveAndFlush(Mockito.any(Product.class)))
               .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void stubFindProduct(Product product) {
        Mockito.when(productRepo.findById(product.getId()))
               .thenReturn(Optional.of(product));
    }

    // ---------- CREATE PRODUCT ----------

    private static final BigDecimal SALE_UNIT = BigDecimal.ONE;

    private void stubSaveAssigningId() {
        Mockito.when(productRepo.saveAndFlush(Mockito.any(Product.class)))
               .thenAnswer(invocation -> {
                   Product p = invocation.getArgument(0);
                   p.setId(UUID.randomUUID());
                   return p;
               });
    }

    @Test
    public void createProductShouldThrowWhenParametersAreNull() {
        UUID shopId = UUID.randomUUID();

        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(null, NAME, PRICE, QUANTITY, UNIT, SALE_UNIT))
                  .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shopId, null, PRICE, QUANTITY, UNIT, SALE_UNIT))
                  .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shopId, NAME, null, QUANTITY, UNIT, SALE_UNIT))
                  .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shopId, NAME, PRICE, null, UNIT, SALE_UNIT))
                  .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shopId, NAME, PRICE, QUANTITY, null, SALE_UNIT))
                  .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shopId, NAME, PRICE, QUANTITY, UNIT, null))
                  .isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void createProductShouldThrowWhenNoMatchingShopFound() {
        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(UUID.randomUUID(), NAME, PRICE, QUANTITY, UNIT,
                                                             SALE_UNIT))
                  .isInstanceOf(RuntimeException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void createProductShouldThrowWhenPriceIsNegative() {
        Shop shop = newShop();
        Mockito.when(shopRepo.findById(shop.getId()))
               .thenReturn(Optional.of(shop));

        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shop.getId(), NAME, BigDecimal.valueOf(-30L), QUANTITY,
                                                             UNIT, SALE_UNIT))
                  .isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void createProductShouldThrowWhenQuantityIsNegative() {
        Shop shop = newShop();
        Mockito.when(shopRepo.findById(shop.getId()))
               .thenReturn(Optional.of(shop));

        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shop.getId(), NAME, PRICE, BigDecimal.valueOf(-500L),
                                                             UNIT, SALE_UNIT))
                  .isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void createProductShouldThrowWhenSaleUnitIsNegative() {
        Shop shop = newShop();
        Mockito.when(shopRepo.findById(shop.getId()))
               .thenReturn(Optional.of(shop));

        Assertions.assertThatThrownBy(
                          () -> productService.createProduct(shop.getId(), NAME, PRICE, QUANTITY, UNIT,
                                                             BigDecimal.valueOf(-10L)))
                  .isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void createProductShouldSaveAnInactiveProductBelongingToTheShop() {
        Shop shop = newShop();
        Mockito.when(shopRepo.findById(shop.getId()))
               .thenReturn(Optional.of(shop));
        stubSaveAssigningId();

        productService.createProduct(shop.getId(), NAME, PRICE, QUANTITY, UNIT, SALE_UNIT);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepo, Mockito.times(1))
               .saveAndFlush(captor.capture());

        Product saved = captor.getValue();
        Assertions.assertThat(saved.getShop())
                  .isSameAs(shop);
        Assertions.assertThat(saved.getName())
                  .isEqualTo(NAME);
        Assertions.assertThat(saved.getPrice())
                  .isEqualByComparingTo(PRICE);
        Assertions.assertThat(saved.getQuantity())
                  .isEqualByComparingTo(QUANTITY);
        Assertions.assertThat(saved.getUnit())
                  .isEqualTo(UNIT);
        Assertions.assertThat(saved.getActiveState())
                  .isEqualTo(EActiveState.INACTIVE);
    }

    @Test
    public void createProductShouldReturnResponseDTOMatchingTheSavedEntity() {
        Shop shop = newShop();
        Mockito.when(shopRepo.findById(shop.getId()))
               .thenReturn(Optional.of(shop));
        stubSaveAssigningId();

        ResponseProductDTO result = productService.createProduct(shop.getId(), NAME, PRICE, QUANTITY, UNIT,
                                                                 SALE_UNIT);

        Assertions.assertThat(result.getId())
                  .isNotNull();
        Assertions.assertThat(result.getShopId())
                  .isEqualTo(shop.getId());
        Assertions.assertThat(result.getName())
                  .isEqualTo(NAME);
        Assertions.assertThat(result.getPrice())
                  .isEqualByComparingTo(PRICE);
        Assertions.assertThat(result.getQuantity())
                  .isEqualByComparingTo(QUANTITY);
        Assertions.assertThat(result.getUnit())
                  .isEqualTo(UNIT);
        Assertions.assertThat(result.getState())
                  .isEqualTo(EActiveState.INACTIVE);
    }

    // ---------- FIND BY ID ----------

    @Test
    public void findByIdShouldThrowWhenIdIsNull() {
        Assertions.assertThatThrownBy(() -> productService.findById(null))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByIdShouldThrowWhenNoMatchingEntityFound() {
        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> productService.findById(UUID.randomUUID()))
                  .isInstanceOf(RuntimeException.class)
                  .hasMessageContaining("Could not find product");
    }

    @Test
    public void findByIdShouldReturnResponseProductDTOWhenMatched() {
        Product product = newProduct(newShop());
        stubFindProduct(product);

        ResponseProductDTO result = productService.findById(product.getId());

        Assertions.assertThat(result.getId())
                  .isEqualTo(product.getId());
        Assertions.assertThat(result.getShopId())
                  .isEqualTo(product.getShop()
                                    .getId());
        Assertions.assertThat(result.getName())
                  .isEqualTo(NAME);
    }

    // ---------- FIND BY SHOP ID ----------

    @Test
    public void findByShopIdShouldThrowWhenIdIsNull() {
        Assertions.assertThatThrownBy(() -> productService.findByShopId(null))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByShopIdShouldReturnAnEmptyListWhenNoMatchingEntities() {
        Mockito.when(productRepo.findAllByShopId(Mockito.any(UUID.class)))
               .thenReturn(List.of());

        List<ResponseProductDTO> result = productService.findByShopId(UUID.randomUUID());

        Assertions.assertThat(result)
                  .isEmpty();
    }

    @Test
    public void findByShopIdShouldReturnListOfResponseProductDTOsWhenMatched() {
        Shop shop = newShop();
        Product first = newProduct(shop);
        Product second = newProduct(shop);
        second.setName("Product 2");
        Mockito.when(productRepo.findAllByShopId(shop.getId()))
               .thenReturn(List.of(first, second));

        List<ResponseProductDTO> result = productService.findByShopId(shop.getId());

        Assertions.assertThat(result)
                  .hasSize(2);
        Assertions.assertThat(result)
                  .extracting(ResponseProductDTO::getName)
                  .containsExactly(NAME, "Product 2");
        Assertions.assertThat(result)
                  .allSatisfy(dto -> Assertions.assertThat(dto.getShopId())
                                               .isEqualTo(shop.getId()));
    }

    // ---------- PATCH BY ID ----------
    // The update*/activate/deactivate methods are private delegates of patchById,
    // so every field-level behaviour is exercised through it.

    private ArgumentCaptor<Product> verifySavedOnce() {
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepo, Mockito.times(1))
               .saveAndFlush(captor.capture());
        return captor;
    }

    @Test
    public void patchByIdShouldThrowWhenIdIsNull() {
        Assertions.assertThatThrownBy(() -> productService.patchById(null, new PatchProductDTO()))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void patchByIdShouldThrowWhenNoMatchingEntityFound() {
        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> productService.patchById(UUID.randomUUID(), new PatchProductDTO()))
                  .isInstanceOf(RuntimeException.class)
                  .hasMessageContaining("Could not find product");
    }

    @Test
    public void patchByIdShouldThrowWhenNoMatchingEntityFoundForAFieldUpdate() {
        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                          () -> productService.patchById(UUID.randomUUID(), patch("New name", null, null, null, null)))
                  .isInstanceOf(RuntimeException.class)
                  .hasMessageContaining("Could not find product");

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void patchByIdShouldNotSaveAnythingWhenNoFieldsAreProvided() {
        Product product = newProduct(newShop());
        stubFindProduct(product);

        ResponseProductDTO result = productService.patchById(product.getId(), new PatchProductDTO());

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
        Assertions.assertThat(result.getName())
                  .isEqualTo(NAME);
    }

    // ----- name -----

    @Test
    public void patchByIdShouldThrowWhenNameIsEmptyOrBlank() {
        UUID id = UUID.randomUUID();

        Assertions.assertThatThrownBy(() -> productService.patchById(id, patch("", null, null, null, null)))
                  .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> productService.patchById(id, patch("   ", null, null, null, null)))
                  .isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void patchByIdShouldSaveAndReturnTheUpdatedName() {
        Product product = newProduct(newShop());
        stubFindProduct(product);
        stubSaveToReturnArgument();

        ResponseProductDTO result = productService.patchById(product.getId(), patch("New name", null, null, null, null));

        Assertions.assertThat(verifySavedOnce()
                                      .getValue()
                                      .getName())
                  .isEqualTo("New name");
        Assertions.assertThat(result.getId())
                  .isEqualTo(product.getId());
        Assertions.assertThat(result.getName())
                  .isEqualTo("New name");
    }

    // ----- price -----

    @Test
    public void patchByIdShouldThrowWhenPriceIsNegative() {
        // all-args constructor on purpose: the DTO setter would reject the value before the service sees it
        PatchProductDTO updates = new PatchProductDTO(null, BigDecimal.valueOf(-1L), null, null, null);

        Assertions.assertThatThrownBy(() -> productService.patchById(UUID.randomUUID(), updates))
                  .isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void patchByIdShouldSaveAndReturnTheUpdatedPrice() {
        Product product = newProduct(newShop());
        stubFindProduct(product);
        stubSaveToReturnArgument();
        BigDecimal newPrice = BigDecimal.valueOf(999L);

        ResponseProductDTO result = productService.patchById(product.getId(), patch(null, newPrice, null, null, null));

        Assertions.assertThat(verifySavedOnce()
                                      .getValue()
                                      .getPrice())
                  .isEqualByComparingTo(newPrice);
        Assertions.assertThat(result.getPrice())
                  .isEqualByComparingTo(newPrice);
    }

    // ----- quantity -----

    @Test
    public void patchByIdShouldThrowWhenQuantityIsNegative() {
        PatchProductDTO updates = new PatchProductDTO(null, null, BigDecimal.valueOf(-1L), null, null);

        Assertions.assertThatThrownBy(() -> productService.patchById(UUID.randomUUID(), updates))
                  .isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(productRepo, Mockito.never())
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void patchByIdShouldSaveAndReturnTheUpdatedQuantity() {
        Product product = newProduct(newShop());
        stubFindProduct(product);
        stubSaveToReturnArgument();
        BigDecimal newQuantity = BigDecimal.valueOf(5L);

        ResponseProductDTO result = productService.patchById(product.getId(),
                                                             patch(null, null, newQuantity, null, null));

        Assertions.assertThat(verifySavedOnce()
                                      .getValue()
                                      .getQuantity())
                  .isEqualByComparingTo(newQuantity);
        Assertions.assertThat(result.getQuantity())
                  .isEqualByComparingTo(newQuantity);
    }

    // ----- unit -----

    @Test
    public void patchByIdShouldSaveAndReturnTheUpdatedUnit() {
        Product product = newProduct(newShop());
        stubFindProduct(product);
        stubSaveToReturnArgument();

        ResponseProductDTO result = productService.patchById(product.getId(),
                                                             patch(null, null, null, EMessurmentUnit.KG, null));

        Assertions.assertThat(verifySavedOnce()
                                      .getValue()
                                      .getUnit())
                  .isEqualTo(EMessurmentUnit.KG);
        Assertions.assertThat(result.getUnit())
                  .isEqualTo(EMessurmentUnit.KG);
    }

    // ----- state (activate / deactivate) -----

    @Test
    public void patchByIdShouldActivateAnInactiveProduct() {
        Product product = newProduct(newShop());
        product.setActiveState(EActiveState.INACTIVE);
        stubFindProduct(product);
        stubSaveToReturnArgument();

        ResponseProductDTO result = productService.patchById(product.getId(),
                                                             patch(null, null, null, null, EActiveState.ACTIVE));

        Assertions.assertThat(verifySavedOnce()
                                      .getValue()
                                      .getActiveState())
                  .isEqualTo(EActiveState.ACTIVE);
        Assertions.assertThat(result.getState())
                  .isEqualTo(EActiveState.ACTIVE);
    }

    @Test
    public void patchByIdShouldDeactivateAnActiveProduct() {
        Product product = newProduct(newShop());
        product.setActiveState(EActiveState.ACTIVE);
        stubFindProduct(product);
        stubSaveToReturnArgument();

        ResponseProductDTO result = productService.patchById(product.getId(),
                                                             patch(null, null, null, null, EActiveState.INACTIVE));

        Assertions.assertThat(verifySavedOnce()
                                      .getValue()
                                      .getActiveState())
                  .isEqualTo(EActiveState.INACTIVE);
        Assertions.assertThat(result.getState())
                  .isEqualTo(EActiveState.INACTIVE);
    }

    // ----- several fields at once -----

    @Test
    public void patchByIdShouldApplyEveryProvidedField() {
        Product product = newProduct(newShop());
        stubFindProduct(product);
        stubSaveToReturnArgument();
        PatchProductDTO updates = patch("Renamed", BigDecimal.valueOf(55L), BigDecimal.valueOf(7L),
                                        EMessurmentUnit.KG, EActiveState.ACTIVE);

        ResponseProductDTO result = productService.patchById(product.getId(), updates);

        Assertions.assertThat(result.getName())
                  .isEqualTo("Renamed");
        Assertions.assertThat(result.getPrice())
                  .isEqualByComparingTo(BigDecimal.valueOf(55L));
        Assertions.assertThat(result.getQuantity())
                  .isEqualByComparingTo(BigDecimal.valueOf(7L));
        Assertions.assertThat(result.getUnit())
                  .isEqualTo(EMessurmentUnit.KG);
        Assertions.assertThat(result.getState())
                  .isEqualTo(EActiveState.ACTIVE);
    }

    @Test
    public void patchByIdShouldOnlyChangeTheProvidedFields() {
        Product product = newProduct(newShop());
        stubFindProduct(product);
        stubSaveToReturnArgument();

        ResponseProductDTO result = productService.patchById(product.getId(),
                                                             patch(null, BigDecimal.valueOf(55L), null, null, null));

        Assertions.assertThat(result.getPrice())
                  .isEqualByComparingTo(BigDecimal.valueOf(55L));
        Assertions.assertThat(result.getName())
                  .isEqualTo(NAME);
        Assertions.assertThat(result.getQuantity())
                  .isEqualByComparingTo(QUANTITY);
        Assertions.assertThat(result.getUnit())
                  .isEqualTo(UNIT);
        Assertions.assertThat(result.getState())
                  .isEqualTo(EActiveState.INACTIVE);
    }
}