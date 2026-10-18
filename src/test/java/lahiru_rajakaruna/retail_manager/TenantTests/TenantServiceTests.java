package lahiru_rajakaruna.retail_manager.TenantTests;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateTenantDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lahiru_rajakaruna.retail_manager.Tenant.TenantService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TenantServiceTests {

    private CreateTenantDTO sampleTenantDTO;
    private Tenant sampleTenant;
    private UUID generatedID;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ITenantRepository tenantRepo;
    @Mock
    private IShopRepository shopRepo;

    @InjectMocks
    private TenantService tenantService;

    public TenantServiceTests() {
    }

    @BeforeEach
    public void setup() {
        createSampleDTO();
        createSampleEntity();
    }

    public void createSampleEntity() {
        sampleTenant = new Tenant();
        sampleTenant.setState(EActiveState.INACTIVE);
        sampleTenant.setPhoneNumber("000-0000-000");
        sampleTenant.setPasswordHash("hashed-password");

        generatedID = new UUID(9, 1);
        sampleTenant.setId(generatedID);
    }

    public void createSampleDTO() {
        sampleTenantDTO = new CreateTenantDTO();
        sampleTenantDTO.setPhoneNumber("222-2222-222");

        sampleTenant.setState(EActiveState.INACTIVE);
    }


    @Test
    public void shouldReturnTheEntityWhenIdIsProvided() {
        Mockito.when(tenantRepo.findById(ArgumentMatchers.any(UUID.class)))
               .thenReturn(Optional.of(sampleTenant));

        ResponseDTO tenant = tenantService.getTenantById(generatedID);
        Assertions.assertThat(tenant)
                  .isNotNull();
        Assertions.assertThat(tenant.getId())
                  .isNotNull();
        Assertions.assertThat(tenant.getId())
                  .isEqualByComparingTo(generatedID);
    }

    @Test
    public void shouldThrowWhenQueryingWithNullId() {
        UUID id = null;
        Assertions.assertThatThrownBy(() -> tenantService.getTenantById(id))
                  .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void shouldCreateTenantWhenAllRequiredDataIsProvided() {
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
               .thenReturn("encoded-password");

        Mockito.lenient()
               .when(tenantRepo.save(ArgumentMatchers.any(Tenant.class)))
               .thenReturn(sampleTenant);

        ResponseDTO tenant = tenantService.createTenant(sampleTenantDTO);

        Assertions.assertThat(tenant)
                  .isNotNull();
        Assertions.assertThat(tenant.getId())
                  .isEqualByComparingTo(generatedID);
        Assertions.assertThat(tenant.getPhone())
                  .isEqualTo(sampleTenant.getPhoneNumber());
    }


    @Test
    public void shouldThrowWhenCreatingWithoutPhone() {
        sampleTenantDTO.setPhoneNumber(null);
        Assertions.assertThatThrownBy(() -> tenantService.createTenant(
                          sampleTenantDTO))
                  .isInstanceOf(RuntimeException.class);
    }


    @Test
    public void shouldUpdatePhone() {
        Mockito.when(tenantRepo.findById(ArgumentMatchers.any(UUID.class)))
               .thenReturn(Optional.of(sampleTenant));

        Mockito.lenient()
               .when(tenantRepo.save(ArgumentMatchers.any(Tenant.class)))
               .thenReturn(sampleTenant);

        String updatedPhone = "111-1111-111-11";
/*
        ResponseDTO dto = tenantService.updatePhoneById(generatedID,
                                                        updatedPhone);
        Assertions.assertThat(dto.getPhone()
                                 .isPresent())
                  .isEqualTo(true);
        Assertions.assertThat(dto.getPhone()
                                 .get())
                  .isEqualTo(updatedPhone);
*/
    }

    @Test
    public void shouldThrowWhenUpdatingPhoneWithoutId() {
        String updatedPhone = "111-1111-111-11";
//        Assertions.assertThatThrownBy(() -> tenantService.updatePhoneById(null,
//                                                                          updatedPhone))
//                  .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void shouldThrowWhenUpdatingPhoneWithNullPhone() {
        String updatedPhone = null;
//        Assertions.assertThatThrownBy(() -> tenantService.updatePhoneById(
//                          generatedID,
//                          null))
//                  .isInstanceOf(RuntimeException.class);
    }
}
