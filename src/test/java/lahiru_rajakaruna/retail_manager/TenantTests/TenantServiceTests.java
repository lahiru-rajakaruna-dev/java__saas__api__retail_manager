package lahiru_rajakaruna.retail_manager.TenantTests;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateTenantDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.PatchDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lahiru_rajakaruna.retail_manager.Tenant.TenantService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TenantService.class, BCryptPasswordEncoder.class, IShopRepository.class})
public class TenantServiceTests {

    public final String dummyPhone = "+9471 00 00 0000";
    public final String dummyPassword = "dummy-password";
    public final CreateTenantDTO createTenantDTO = new CreateTenantDTO(dummyPhone, dummyPassword);

    @MockitoSpyBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private ITenantRepository tenantRepo;

    @MockitoBean
    private IShopRepository shopRepo;

    @Autowired
    private TenantService tenantService;

    public TenantServiceTests() {
    }

    @Test
    public void createTenantShouldThrowWhenArgumentsAreNull() {
        Assertions.assertThatThrownBy(() -> {
                      this.tenantService.createTenant(new CreateTenantDTO(null, "password"));
                  })
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
                      this.tenantService.createTenant(new CreateTenantDTO(dummyPhone, null));
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createTenantShouldHaveEncodedRawPassword() {
        Mockito.when(tenantRepo.saveAndFlush(Mockito.any()))
               .thenReturn(new Tenant(null, "password-hash", dummyPhone, EActiveState.ACTIVE));

        this.tenantService.createTenant(createTenantDTO);

        Mockito.verify(passwordEncoder, Mockito.atMostOnce())
               .encode(Mockito.eq(dummyPassword));
    }

    @Test
    public void getTenantByIdShouldThrowWhenArgumentIsNull() {
        Assertions.assertThatThrownBy(() -> {
                      this.tenantService.getTenantById(null);
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getTenantByIdShouldReturnInstanceOfResponseDTO() {
        Mockito.when(tenantRepo.findById(Mockito.any()))
               .thenReturn(Optional.ofNullable(new Tenant(null, "password_hash", dummyPhone, EActiveState.ACTIVE)));

        Object result = this.tenantService.getTenantById(UUID.randomUUID());

        Assertions.assertThat(result)
                  .isNotNull();

        Assertions.assertThat(result)
                  .isInstanceOf(ResponseDTO.class);
    }

    @Test
    public void getTenantByPhoneShouldThrowWhenArgumentIsNull() {
        Assertions.assertThatThrownBy(() -> {
                      this.tenantService.getTenantByPhone(null);
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getTenantByPhoneShouldHaveCalledRepoFindByPhone() {
        Mockito.when(tenantRepo.findByPhone(Mockito.any()))
               .thenReturn(Optional.ofNullable(new Tenant(null, "password_hash", dummyPhone, EActiveState.ACTIVE)));

        this.tenantService.getTenantByPhone(dummyPhone);

        Mockito.verify(tenantRepo, Mockito.atLeastOnce())
               .findByPhone(Mockito.any());
    }

    @Test
    public void getTenantByPhoneShouldReturnAnInstanceResponseDTO() {
        Mockito.when(tenantRepo.findByPhone(Mockito.any())).
               thenReturn(Optional.ofNullable(new Tenant(null, "password_hash", dummyPhone, EActiveState.ACTIVE)));

        Object result = this.tenantService.getTenantByPhone(dummyPhone);

        Assertions.assertThat(result)
                  .isNotNull();
        Assertions.assertThat(result)
                  .isInstanceOf(ResponseDTO.class);
    }

    @Test
    public void patchProfileByIdShouldThrowWhenParametersAreNull() {
        PatchDTO patchDTO = new PatchDTO("+9472 00 11 2233", "password", null, EActiveState.ACTIVE);

        Assertions.assertThatThrownBy(() -> {
                      this.tenantService.patchProfileById(null, patchDTO);
                  })
                  .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
                      this.tenantService.patchProfileById(UUID.randomUUID(), null);
                  })
                  .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void patchProfileByDTOShouldHaveCallFindById() {
        PatchDTO patchDTO = new PatchDTO("+9472 00 11 2233", "password", null, EActiveState.ACTIVE);

        Tenant tenant = new Tenant(null, "password_hash", dummyPhone, EActiveState.ACTIVE);

        Mockito.when(tenantRepo.findById(Mockito.any()))
               .thenReturn(Optional.ofNullable(tenant));

        Mockito.when(tenantRepo.saveAndFlush(Mockito.any()))
               .thenReturn(tenant);

        this.tenantService.patchProfileById(UUID.randomUUID(), patchDTO);

        Mockito.verify(tenantRepo, Mockito.times(1))
               .findById(Mockito.any());
    }

    @Test
    public void patchProfileByDTOShouldHaveCallShopRepoFindByIDWhenShopIsPresent() {
        PatchDTO patchDTO = new PatchDTO(null, null, UUID.randomUUID(), null);
        Shop shop = new Shop("shop", EActiveState.ACTIVE);
        Tenant tenant = new Tenant(null, "password_hash", dummyPhone, EActiveState.ACTIVE);

        Mockito.when(tenantRepo.findById(Mockito.any()))
               .thenReturn(Optional.ofNullable(tenant));
        Mockito.when(shopRepo.findById(Mockito.any()))
               .thenReturn(Optional.ofNullable(shop));
        Mockito.when(tenantRepo.saveAndFlush(Mockito.any()))
               .thenReturn(tenant);

        this.tenantService.patchProfileById(UUID.randomUUID(), patchDTO);

        Mockito.verify(shopRepo, Mockito.times(1))
               .findById(Mockito.any());

    }

    @Test
    public void patchProfileByIdShouldHaveCalledSaveAndFlush() {
        PatchDTO patchDTO = new PatchDTO(null, "new-password", null, null);
        Tenant tenant = new Tenant(null, "password_hash", dummyPhone, EActiveState.ACTIVE);

        Mockito.when(tenantRepo.findById(Mockito.any()))
               .thenReturn(Optional.ofNullable(tenant));
        Mockito.when(tenantRepo.saveAndFlush(Mockito.any()))
               .thenReturn(tenant);

        this.tenantService.patchProfileById(UUID.randomUUID(), patchDTO);

        Mockito.verify(tenantRepo, Mockito.times(1))
               .saveAndFlush(Mockito.any());
    }

    @Test
    public void patchProfileByIdShouldReturnAnInstanceOfResponseDTO() {
        Tenant tenant = new Tenant(null, "password_hash", dummyPhone, EActiveState.ACTIVE);
        PatchDTO patchDTO = new PatchDTO("+94 00 11 2233", null, null, null);

        Mockito.when(tenantRepo.findById(Mockito.any()))
               .thenReturn(Optional.ofNullable(tenant));
        Mockito.when(tenantRepo.saveAndFlush(Mockito.any()))
               .thenReturn(tenant);

        Object result = this.tenantService.patchProfileById(UUID.randomUUID(), patchDTO);

        Assertions.assertThat(result)
                  .isNotNull();
        Assertions.assertThat(result)
                  .isInstanceOf(ResponseDTO.class);

    }

}
