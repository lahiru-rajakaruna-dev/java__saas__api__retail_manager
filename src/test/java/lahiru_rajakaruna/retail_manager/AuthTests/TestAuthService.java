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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
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

    @MockitoSpyBean
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
    public void loginShouldThrowWhenCredentialsAreNull() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login(null, null);
        });
    }

    @Test
    public void loginProcessTimeShouldMatchForRegisteredAndNonRegisteredPhone() {
        String nonRegisteredPhone = "+9471 25 62 920";
        Mockito.when(tenantRepo.findByPhone(Mockito.eq(dummyPhone)))
               .thenReturn(Optional.ofNullable(dummyTenant));
        Mockito.when(tenantRepo.findByPhone(Mockito.eq(nonRegisteredPhone)))
               .thenReturn(Optional.empty());

        Instant login_attempt_1_start = Instant.now();
        authService.login(dummyPhone, dummyPassword);
        Instant login_attempt_1_end = Instant.now();
        Duration valid_cred_login_process_duration = Duration.between(login_attempt_1_start, login_attempt_1_end);


        Instant login_attempt_2_start = Instant.now();
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login(nonRegisteredPhone, "strong-password");
        });
        Instant login_attempt_2_end = Instant.now();
        Duration invalid_cred_login_process_duration = Duration.between(login_attempt_2_start, login_attempt_2_end);


        long processTimeDifference = Math.abs(valid_cred_login_process_duration.toMillis() - invalid_cred_login_process_duration.toMillis());
        Assertions.assertTrue(processTimeDifference < 5L, "Execution time difference was too large");
    }

    @Test
    public void loginShouldThrowWithSameMessageForInvalidCredentials() {
        Mockito.when(tenantRepo.findByPhone(Mockito.eq(dummyPhone)))
               .thenReturn(Optional.ofNullable(dummyTenant));

        RuntimeException exceptionThrownForInvalidPassword = Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login(dummyPhone, "wrong-password");
        });

        Mockito.when(tenantRepo.findByPhone(Mockito.any(String.class)))
               .thenReturn(Optional.empty());

        RuntimeException exceptionThrownForNonRegisteredPhone = Assertions.assertThrows(RuntimeException.class, () -> {
            authService.login("+9400 00 00 000", dummyPassword);
        });

        Assertions.assertTrue(exceptionThrownForInvalidPassword.getMessage()
                                                               .contentEquals(exceptionThrownForNonRegisteredPhone.getMessage()));
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

    @Test
    public void registrationShouldThrowWhenCredentialsAreNull() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(null, null);
        });
    }

    @Test
    public void registrationShouldThrowForWeakPasswords() {
//        NUMBERS ONLY
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(dummyPhone, "12345678");
        });
//        LETTERS ONLY
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(dummyPhone, "AABBCCDD");
        });
//        LESS THAN 8 CHARS
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(dummyPhone, "123");
        });
//        NO UPPERCASE
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(dummyPhone, "aa88**cc");
        });
//        NO LOWERCASE
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(dummyPhone, "AA88**CC");
        });
//        NO SPECIAL CHARS
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register(dummyPhone, "AA88CCdd");
        });
    }

    @Test
    public void registrationShouldThrowForNonValidPhoneNumberPatterns() {
//        INVALID COUNTRY CODE
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register("+172 00 00 000", "password");
        });
//        INVALID MOBILE IDENTIFIER
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register("+9462 00 00 000", "password");
        });
//        INVALID NETWORK OPERATOR IDENTIFIER
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register("+9479 00 00 000", "password");
        });
//        INVALID LENGTH
        Assertions.assertThrows(RuntimeException.class, () -> {
            authService.register("+9479 00 00 0000", "password");
        });
    }
}
