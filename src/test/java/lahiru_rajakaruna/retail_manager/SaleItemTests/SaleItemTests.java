package lahiru_rajakaruna.retail_manager.SaleItemTests;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Common.ESaleState;
import lahiru_rajakaruna.retail_manager.Product.EMessurmentUnit;
import lahiru_rajakaruna.retail_manager.Product.IProductRepository;
import lahiru_rajakaruna.retail_manager.Product.Product;
import lahiru_rajakaruna.retail_manager.Sale.ISaleRepository;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.SaleItemResponseDTO;
import lahiru_rajakaruna.retail_manager.SaleItem.ISaleItemRepository;
import lahiru_rajakaruna.retail_manager.SaleItem.SaleItem;
import lahiru_rajakaruna.retail_manager.SaleItem.SaleItemService;
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
@ContextConfiguration(classes = {SaleItemService.class})
public class SaleItemTests {
    private final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(100L);
    private final BigDecimal PRODUCT_QUANTITY = BigDecimal.valueOf(500L);
    private final EMessurmentUnit PRODUCT_MESSURMENTUNIT = EMessurmentUnit.PIECES;
    private final BigDecimal SALE_ITEM_QUANTITY = BigDecimal.valueOf(5L);
    private final BigDecimal SALE_ITEM_DISCOUNT = BigDecimal.valueOf(10L);

    @Autowired
    private SaleItemService saleItemService;
    @MockitoBean
    private IShopRepository shopRepo;
    @MockitoBean
    private ISaleRepository saleRepo;
    @MockitoBean
    private IProductRepository productRepo;
    @MockitoBean
    private ISaleItemRepository saleItemRepo;

    public SaleItemTests() {
    }
    //    ======= HELPERS =======

    private Product newProduct(Shop shop) {
        return new Product(shop,
                           "Product 1",
                           PRODUCT_PRICE,
                           PRODUCT_QUANTITY,
                           PRODUCT_MESSURMENTUNIT,
                           EActiveState.ACTIVE);
    }

    private Shop newShop() {
        return new Shop("Test Shop", EActiveState.ACTIVE);
    }

    private Sale newSale(Shop shop) {
        return new Sale(shop, BigDecimal.ZERO, ESaleState.OPEN);
    }

    private SaleItem newSaleItem(
            Shop shop, Sale sale, Product product, BigDecimal quantity,
            BigDecimal discount
                                ) {
        BigDecimal total = product.getPrice()
                                  .multiply(quantity)
                                  .subtract(discount);
        return new SaleItem(shop, sale, product, quantity, discount, total);
    }

    @Test
    public void addItemShouldThrowIfAnyParameterIsNull() {
        Assertions.assertThatThrownBy(() -> saleItemService.addItem(null,
                                                                    UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    PRODUCT_QUANTITY,
                                                                    BigDecimal.ZERO))
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> saleItemService.addItem(UUID.randomUUID(),
                                                                    null,
                                                                    UUID.randomUUID(),
                                                                    PRODUCT_QUANTITY,
                                                                    BigDecimal.ZERO))
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> saleItemService.addItem(UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    null,
                                                                    PRODUCT_QUANTITY,
                                                                    BigDecimal.ZERO))
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> saleItemService.addItem(UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    null,
                                                                    BigDecimal.ZERO))
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> saleItemService.addItem(UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    PRODUCT_QUANTITY,
                                                                    null))
                  .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void addItemShouldCallFindShopOnce() {

        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());

        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());

        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));


        saleItemService.addItem(fakeSale.getId(),
                                fakeShop.getId(),
                                fakeProduct.getId(),
                                BigDecimal.valueOf(4L),
                                BigDecimal.ZERO);

        Mockito.verify(shopRepo, Mockito.times(1))
               .findById(Mockito.any(UUID.class));
    }

    @Test
    public void addItemShouldCallFindProductOnce() {

        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());

        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());

        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));

        saleItemService.addItem(fakeSale.getId(),
                                fakeShop.getId(),
                                fakeProduct.getId(),
                                BigDecimal.valueOf(4L),
                                BigDecimal.ZERO);

        Mockito.verify(productRepo, Mockito.times(1))
               .findById(Mockito.any(UUID.class));
    }

    @Test
    public void addItemShouldCallFindSaleOnce() {
        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());


        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());

        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));

        saleItemService.addItem(fakeSale.getId(),
                                fakeShop.getId(),
                                fakeProduct.getId(),
                                BigDecimal.valueOf(4L),
                                BigDecimal.ZERO);

        Mockito.verify(saleRepo, Mockito.times(1))
               .findById(Mockito.any(UUID.class));
    }

    @Test
    public void addItemShouldCallSaveAndFlushOnce() {
        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());


        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());

        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));

        saleItemService.addItem(fakeSale.getId(),
                                fakeShop.getId(),
                                fakeProduct.getId(),
                                BigDecimal.valueOf(4L),
                                BigDecimal.ZERO);

        Mockito.verify(saleItemRepo, Mockito.times(1))
               .saveAndFlush(Mockito.any(SaleItem.class));
    }

    @Test
    public void addItemShouldHaveSaveAValidSaleItem() {
        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());

        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());


        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));

        saleItemService.addItem(fakeSale.getId(),
                                fakeShop.getId(),
                                fakeProduct.getId(),
                                BigDecimal.valueOf(4L),
                                BigDecimal.ZERO);

        ArgumentCaptor<SaleItem> argumentCaptor = ArgumentCaptor.forClass(
                SaleItem.class);

        Mockito.verify(saleItemRepo, Mockito.times(1))
               .saveAndFlush(argumentCaptor.capture());

        Object argument = argumentCaptor.getValue();

        Assertions.assertThat(argument)
                  .isNotNull();
        Assertions.assertThat(argument)
                  .isInstanceOf(SaleItem.class);
    }

    @Test
    public void addItemShouldReturnValidDTORepresentingTheProvidedData() {
        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());

        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());

        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));


        Object result = saleItemService.addItem(fakeSale.getId(),
                                                fakeShop.getId(),
                                                fakeProduct.getId(),
                                                BigDecimal.valueOf(4L),
                                                BigDecimal.ZERO);


        Assertions.assertThat(result)
                  .isNotNull();
        Assertions.assertThat(result)
                  .isInstanceOf(SaleItemResponseDTO.class);
        Assertions.assertThat(((SaleItemResponseDTO) result).getPrice())
                  .isEqualByComparingTo(fakeProduct.getPrice());
    }

    @Test
    public void addItemShouldThrowIfAnyProvidedNumericalValueIsNegative() {
        Assertions.assertThatThrownBy(() -> saleItemService.addItem(UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    BigDecimal.valueOf(
                                                                            -10),
                                                                    BigDecimal.ZERO))
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> saleItemService.addItem(UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    UUID.randomUUID(),
                                                                    PRODUCT_QUANTITY,
                                                                    BigDecimal.valueOf(
                                                                            -100)))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void addItemShouldRecalculateTheGrossSalePriceOnSuccessfulOperation() {
        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());

        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());

        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        Mockito.when(saleItemRepo.getSaleItemsBySaleId(Mockito.eq(fakeSale.getId())))
               .thenReturn(List.of(fakeSaleItem));

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));

        Object result = saleItemService.addItem(fakeSale.getId(),
                                                fakeShop.getId(),
                                                fakeProduct.getId(),
                                                SALE_ITEM_QUANTITY,
                                                SALE_ITEM_DISCOUNT);

        ArgumentCaptor<Sale> argumentCaptor = ArgumentCaptor.forClass(Sale.class);

        Mockito.verify(saleRepo, Mockito.times(1))
               .saveAndFlush(argumentCaptor.capture());

        Object savedEntity = argumentCaptor.getValue();
        Assertions.assertThat(savedEntity)
                  .isNotNull();
        Assertions.assertThat(savedEntity)
                  .isInstanceOf(Sale.class);

        Sale savedSale = (Sale) savedEntity;
        Assertions.assertThat(savedSale.getTotal())
                  .isEqualByComparingTo(fakeProduct.getPrice()
                                                   .multiply(SALE_ITEM_QUANTITY)
                                                   .subtract(SALE_ITEM_DISCOUNT));
    }

    @Test
    public void addItemShouldThrowWhenAddingToAlreadyClosedSale() {
        Shop fakeShop = newShop();
        fakeShop.setId(UUID.randomUUID());

        Sale fakeSale = newSale(fakeShop);
        fakeSale.setId(UUID.randomUUID());
        fakeSale.setSaleState(ESaleState.CLOSED);

        Product fakeProduct = newProduct(fakeShop);
        fakeProduct.setId(UUID.randomUUID());


        SaleItem fakeSaleItem = newSaleItem(fakeShop,
                                            fakeSale,
                                            fakeProduct,
                                            SALE_ITEM_QUANTITY,
                                            SALE_ITEM_DISCOUNT);
        fakeSaleItem.setId(UUID.randomUUID());

        Mockito.when(saleItemRepo.saveAndFlush(Mockito.any(SaleItem.class)))
               .thenReturn(fakeSaleItem);

        BigDecimal quantity = BigDecimal.valueOf(4L);
        BigDecimal discount = BigDecimal.valueOf(5L);

        Mockito.when(productRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeProduct));

        Mockito.when(saleRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeSale));

        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.of(fakeShop));

        Assertions.assertThatThrownBy(() -> saleItemService.addItem(fakeSale.getId(),
                                                                    fakeShop.getId(),
                                                                    fakeProduct.getId(),
                                                                    quantity,
                                                                    discount))
                  .isInstanceOf(IllegalStateException.class);
    }

//    ============= PATH ITEM ==========
//    ============= GET ITEM ===========
//    ============= REMOVE ITEM ========
}
