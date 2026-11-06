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
import org.assertj.core.api.Assertions;
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
        this.dummyTenant = new Tenant(null,
                                      dummyPasswordHash,
                                      dummyPhone,
                                      EActiveState.ACTIVE);
    }


    @Test
    public void loginShouldThrowWhenCredentialsAreNull() {
        Assertions.assertThatThrownBy(() -> {
                      authService.login(null, null);
                  })
                  .isInstanceOf(RuntimeException.class);
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
        Duration valid_cred_login_process_duration = Duration.between(
                login_attempt_1_start,
                login_attempt_1_end);


        Instant login_attempt_2_start = Instant.now();
        Assertions.assertThatThrownBy(() -> {
                      authService.login(nonRegisteredPhone, "strong-password");
                  })
                  .isInstanceOf(RuntimeException.class);
        Instant login_attempt_2_end = Instant.now();
        Duration invalid_cred_login_process_duration = Duration.between(
                login_attempt_2_start,
                login_attempt_2_end);


        long processTimeDifference = Math.abs(valid_cred_login_process_duration.toMillis() - invalid_cred_login_process_duration.toMillis());
        Assertions.assertThat(processTimeDifference)
                  .isLessThan(30);
    }

    @Test
    public void loginShouldThrowWithSameMessageForInvalidCredentials() {
        Mockito.when(tenantRepo.findByPhone(Mockito.eq(dummyPhone)))
               .thenReturn(Optional.ofNullable(dummyTenant));

        Assertions.assertThatThrownBy(() -> {
                      authService.login(dummyPhone, "wrong-password");
                  })
                  .isInstanceOf(RuntimeException.class)
                  .hasMessage(("Invalid Credentials"));

        Mockito.when(tenantRepo.findByPhone(Mockito.any(String.class)))
               .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> {
                      authService.login("+9400 00 00 000", dummyPassword);
                  })
                  .isInstanceOf(RuntimeException.class)
                  .hasMessage("Invalid Credentials");

    }

    @Test
    public void loginShouldReturnLoginResultDTOOnSuccessfulLogin() {
        Mockito.when(tenantRepo.findByPhone(Mockito.eq(dummyPhone)))
               .thenReturn(Optional.ofNullable(dummyTenant));

        LoginResult result = authService.login(dummyPhone, dummyPassword);
        Assertions.assertThat(result)
                  .isNotNull();
        Assertions.assertThat(result.csrfToken())
                  .isNotNull();
        Assertions.assertThat(result.sessionToken())
                  .isNotNull();
    }

    @Test
    public void registrationShouldThrowWhenCredentialsAreNull() {
        Assertions.assertThatThrownBy(() -> {
                      authService.register(null, null);
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void registrationShouldThrowForWeakPasswords() {
//        NUMBERS ONLY
        Assertions.assertThatThrownBy(() -> {
                      authService.register(dummyPhone, "12345678");
                  })
                  .isInstanceOf(RuntimeException.class);
//        LETTERS ONLY
        Assertions.assertThatThrownBy(() -> {
                      authService.register(dummyPhone, "AABBCCDD");
                  })
                  .isInstanceOf(RuntimeException.class);
//        LESS THAN 8 CHARS
        Assertions.assertThatThrownBy(() -> {
                      authService.register(dummyPhone, "123");
                  })
                  .isInstanceOf(RuntimeException.class);
//        NO UPPERCASE
        Assertions.assertThatThrownBy(() -> {
                      authService.register(dummyPhone, "aa88**cc");
                  })
                  .isInstanceOf(RuntimeException.class);
//        NO LOWERCASE
        Assertions.assertThatThrownBy(() -> {
                      authService.register(dummyPhone, "AA88**CC");
                  })
                  .isInstanceOf(RuntimeException.class);
//        NO SPECIAL CHARS
        Assertions.assertThatThrownBy(() -> {
                      authService.register(dummyPhone, "AA88CCdd");
                  })
                  .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void registrationShouldThrowForNonValidPhoneNumberPatterns() {
//        INVALID COUNTRY CODE
        Assertions.assertThatThrownBy(() -> {
                      authService.register("+172 00 00 000", "password");
                  })
                  .isInstanceOf(IllegalArgumentException.class);
//        INVALID MOBILE IDENTIFIER
        Assertions.assertThatThrownBy(() -> {
                      authService.register("+9462 00 00 000", "password");
                  })
                  .isInstanceOf(IllegalArgumentException.class);
//        INVALID NETWORK OPERATOR IDENTIFIER
        Assertions.assertThatThrownBy(() -> {
                      authService.register("+9479 00 00 000", "password");
                  })
                  .isInstanceOf(IllegalArgumentException.class);
//        INVALID LENGTH
        Assertions.assertThatThrownBy(() -> {
                      authService.register("+9479 00 00 0000", "password");
                  })
                  .isInstanceOf(IllegalArgumentException.class);
    }
}

// TODO: ADD TEST CASES FOR ACTIVE AND INACTIVE STATES OF TENANTS