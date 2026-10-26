package lahiru_rajakaruna.retail_manager.AuthTests;


import lahiru_rajakaruna.retail_manager.Authentication.AuthenticationService;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.LoginResult;
import lahiru_rajakaruna.retail_manager.Authentication.RedisSessionManager;
import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.DefaultSecureTokenGenerator;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lahiru_rajakaruna.retail_manager.Tenant.TenantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuthenticationService.class, TenantService.class, BCryptPasswordEncoder.class, DefaultSecureTokenGenerator.class})
public class TestAuthService {
    @MockitoBean
    public RedisSessionManager redisSessionManager;

    @MockitoBean
    private ITenantRepository tenantRepo;

    @MockitoBean
    private IShopRepository shopRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authService;

    private final String dummyPhone;
    private final String dummyPassword;
    private String dummyPasswordHash;
    private Tenant dummyTenant;

    public TestAuthService() {
        this.dummyPassword = "password";
        this.dummyPhone = "+9471 22 33 4567";
    }

    @BeforeEach
    public void setup() {
        this.dummyPasswordHash = passwordEncoder.encode(dummyPassword);
        this.dummyTenant = new Tenant(null, dummyPasswordHash, dummyPhone, EActiveState.ACTIVE);
    }

    @Test
    public void registrationShouldThrowWhenCredentialsAreNull() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(null, null);
        });
    }

    @Test
    public void loginShouldThrowWhenCredentialsAreNull() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login(null, null);
        });
    }

    @Test
    public void loginProcessTimeShouldMatchForRegisteredAndNonRegisteredPhone() {
        Mockito.when(tenantRepo.findByPhone(Mockito.eq(dummyPhone)))
               .thenReturn(Optional.ofNullable(dummyTenant));

        Instant start_1 = Instant.now();
        authService.login(dummyPhone, dummyPassword);
        Instant end_1 = Instant.now();
        Duration elapsed_1 = Duration.between(start_1, end_1);

        Instant start_2 = Instant.now();
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login("+9400 22 33 9999", "strong-password");
        });
        Instant end_2 = Instant.now();
        Duration elapsed_2 = Duration.between(start_2, end_2);

        Assertions.assertTrue(((elapsed_1.toMillis() - elapsed_2.toMillis()) < 100), "Execution time difference was too large");

    }

    @Test
    public void loginShouldThrowWithSameMessageForInvalidCredentials() {
        RuntimeException exception_1 = Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login(dummyPhone, "password");
        });

        RuntimeException exception_2 = Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login("+9400 00 00 000", "password");
        });

        Assertions.assertTrue(exception_1.getMessage()
                                         .contentEquals(exception_2.getMessage()));

    }

    @Test
    public void loginShouldReturnLoginResultDTOOnSuccessfulLogin() {
        Mockito.when(tenantRepo.findByPhone(Mockito.eq(dummyPhone)))
               .thenReturn(Optional.ofNullable(dummyTenant));

        LoginResult result = authService.login(dummyPhone, dummyPassword);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.csrfToken());
        Assertions.assertNotNull(result.sessionToken());
    }

//    TODO: ADD  REGISTRATION ENDPOINT TESTS
}
