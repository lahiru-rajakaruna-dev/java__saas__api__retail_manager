package lahiru_rajakaruna.retail_manager.TenantTests;

import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lahiru_rajakaruna.retail_manager.Tenant.TenantDTO;
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

    private TenantDTO sampleTenantDTO;
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
        sampleTenant.setActive(false);
        sampleTenant.setName("Tester");
        sampleTenant.setPhone("000-0000-000");
        sampleTenant.setPasswordHash("hashed-password");

        generatedID = new UUID(9, 1);
        sampleTenant.setId(generatedID);
    }

    public void createSampleDTO() {
        sampleTenantDTO = new TenantDTO();
        sampleTenantDTO.setName("TesterDTO");
        sampleTenantDTO.setPhone("222-2222-222");
        sampleTenantDTO.setPassword("raw-password");
        sampleTenantDTO.setActive(false);
    }


    @Test
    public void shouldReturnTheEntityWhenIdIsProvided() {
        Mockito.when(tenantRepo.findById(ArgumentMatchers.any(UUID.class)))
               .thenReturn(Optional.of(sampleTenant));

        TenantDTO tenant = tenantService.getTenantById(generatedID);
        Assertions.assertThat(tenant).isNotNull();
        Assertions.assertThat(tenant.getId()).isNotNull();
        Assertions.assertThat(tenant.getId()).isEqualByComparingTo(generatedID);
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

        TenantDTO tenant = tenantService.createTenant(sampleTenantDTO);

        Assertions.assertThat(tenant).isNotNull();
        Assertions.assertThat(tenant.getId()).isEqualByComparingTo(generatedID);
        Assertions.assertThat(tenant.getName().get())
                  .isEqualTo(sampleTenant.getName());
        Assertions.assertThat(tenant.getPhone().get())
                  .isEqualTo(sampleTenant.getPhone());
        Assertions.assertThat(tenant.getPasswordHash().get())
                  .isEqualTo(sampleTenant.getPasswordHash());
    }

    @Test
    public void shouldThrowWhenCreatingWithoutName() {
        sampleTenantDTO.setName(null);
        Assertions.assertThatThrownBy(() -> tenantService.createTenant(
                sampleTenantDTO)).isInstanceOf(RuntimeException.class);

    }

    @Test
    public void shouldThrowWhenCreatingWithoutPhone() {
        sampleTenantDTO.setPhone(null);
        Assertions.assertThatThrownBy(() -> tenantService.createTenant(
                sampleTenantDTO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void shouldUpdateName() {
        Mockito.when(tenantRepo.findById(ArgumentMatchers.any(UUID.class)))
               .thenReturn(Optional.of(sampleTenant));

        Mockito.lenient()
               .when(tenantRepo.save(ArgumentMatchers.any(Tenant.class)))
               .thenReturn(sampleTenant);

        String updatedName = "Test User";
        TenantDTO dto = tenantService.updateNameById(generatedID, updatedName);
        Assertions.assertThat(dto.getName().isPresent()).isEqualTo(true);
        Assertions.assertThat(dto.getName().get()).isEqualTo(updatedName);
    }

    @Test
    public void shouldThrowWhenUpdatingNameWithoutId() {
        String updatedName = "Test User";
        Assertions.assertThatThrownBy(() -> tenantService.updateNameById(null,
                updatedName)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void shouldThrowWhenUpdatingNameWithNullName() {
        String updatedName = null;
        Assertions.assertThatThrownBy(() -> tenantService.updateNameById(
                generatedID,
                updatedName)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void shouldUpdatePhone() {
        Mockito.when(tenantRepo.findById(ArgumentMatchers.any(UUID.class)))
               .thenReturn(Optional.of(sampleTenant));

        Mockito.lenient()
               .when(tenantRepo.save(ArgumentMatchers.any(Tenant.class)))
               .thenReturn(sampleTenant);

        String updatedPhone = "111-1111-111-11";
        TenantDTO dto = tenantService.updatePhoneById(generatedID,
                updatedPhone);
        Assertions.assertThat(dto.getPhone().isPresent()).isEqualTo(true);
        Assertions.assertThat(dto.getPhone().get()).isEqualTo(updatedPhone);
    }

    @Test
    public void shouldThrowWhenUpdatingPhoneWithoutId() {
        String updatedPhone = "111-1111-111-11";
        Assertions.assertThatThrownBy(() -> tenantService.updatePhoneById(null,
                updatedPhone)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void shouldThrowWhenUpdatingPhoneWithNullPhone() {
        String updatedPhone = null;
        Assertions.assertThatThrownBy(() -> tenantService.updatePhoneById(
                generatedID,
                null)).isInstanceOf(RuntimeException.class);
    }
}
