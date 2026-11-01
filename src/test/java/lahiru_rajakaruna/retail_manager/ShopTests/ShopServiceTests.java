package lahiru_rajakaruna.retail_manager.ShopTests;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.CreateShopDTO;
import lahiru_rajakaruna.retail_manager.Shop.DTOs.ShopResponseDTO;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Shop.ShopService;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ShopService.class})
public class ShopServiceTests {

    @MockitoBean
    private IShopRepository shopRepo;
    @MockitoBean
    private ITenantRepository tenantRepo;

    @Autowired
    private ShopService shopService;

    public ShopServiceTests() {
    }

    @Test
    public void findShopByIdShouldThrowWhenIdIsNull() {
        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> {
                      shopService.findById(null);
                  })
                  .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findShopByIdShouldThrowWhenNoMatchingEntity() {
        Mockito.when(shopRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> {
                      shopService.findById(UUID.randomUUID());
                  })
                  .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createShopShouldThrowWhenRequiredDataIsNull() {
        CreateShopDTO data_1 = new CreateShopDTO(null, UUID.randomUUID());
        Assertions.assertThatThrownBy(() -> {
                      shopService.createShop(data_1);
                  })
                  .isInstanceOf(RuntimeException.class);

        CreateShopDTO data_2 = new CreateShopDTO("TEST SHOP", null);
        Assertions.assertThatThrownBy(() -> {
                      shopService.createShop(data_2);
                  })
                  .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createShopShouldThrowWhenItCannotFindOperatingTenantProfile() {
        Mockito.when(tenantRepo.findById(Mockito.any(UUID.class)))
               .thenReturn(Optional.empty());

        CreateShopDTO data = new CreateShopDTO("Test Shop", UUID.randomUUID());

        Assertions.assertThatThrownBy(() -> {
                      shopService.createShop(data);
                  })
                  .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createShopShouldReturnSavedShopDTOOnSuccessfulOperation() {
        UUID tenantID = UUID.randomUUID();
        Tenant fakeTenant = new Tenant();
        fakeTenant.setId(tenantID);

        UUID shopID = UUID.randomUUID();
        Shop fakeShop = new Shop("TEST SHOP", EActiveState.ACTIVE);
        fakeShop.setId(shopID);

        Mockito.when(tenantRepo.findById(Mockito.eq(tenantID)))
               .thenReturn(Optional.of(fakeTenant));

        Mockito.when(shopRepo.save(Mockito.any(Shop.class)))
               .thenReturn(fakeShop);

        Object result = shopService.createShop(new CreateShopDTO("TEST SHOP", tenantID));

        Assertions.assertThat(result)
                  .isNotNull();
        Assertions.assertThat(result)
                  .isInstanceOf(ShopResponseDTO.class);

    }

//    TODO: TEST CREATE TENANT TRANSACTIONAL INTEGRITY

    public void patchShopByIdShouldThrowWhenIdIsNull() {
    }
    public void patchShopByIdShouldUpdateTheEntityWithOnePropertyPatch() {
    }
    public void patchShopByIdShouldUpdateTheEntityWithMultiPropertyPatch() {
    }
    public void patchShopByIdShouldReturnDTOOnSuccessfulOperation() {
    }
//    TODO: TEST FOR PATCH SHOP TRANSACTIONAL INTEGRITY
}
