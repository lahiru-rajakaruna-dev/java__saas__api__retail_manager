package lahiru_rajakaruna.retail_manager.Authentication;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.AuthSession;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.LoginResult;
import lahiru_rajakaruna.retail_manager.Common.SessionManager;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateTenantDTO;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lahiru_rajakaruna.retail_manager.Tenant.TenantService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final String DUMMY_HASH;
    private final ITenantRepository tenantRepo;
    private final TenantService tenantService;
    private final PasswordEncoder passwordEncoder;
    private final SessionManager sessionManager;

    public AuthenticationService(TenantService tenantService, ITenantRepository tenantRepo, PasswordEncoder passwordEncoder, SessionManager sessionManager) {
        this.tenantService = tenantService;
        this.tenantRepo = tenantRepo;
        this.passwordEncoder = passwordEncoder;
        this.sessionManager = sessionManager;
        DUMMY_HASH = passwordEncoder.encode("DUMMY_HASH_TO_FIX_TIME_GAP");
    }

    @Transactional
    public LoginResult login(String phoneNumber, String password) {
        requireNonNull(phoneNumber, "Phone Number Not Provided");
        requireNonNull(password, "Password Not Provided");

        Tenant tenant = tenantRepo.findByPhone(phoneNumber)
                                  .orElse(null);
        /*
         * THIS IS TO MATCH THE TIME WITH NON-REGISTERED PHONE NUMBERS TO REGISTERED PHONE NUMBER
         * HASHING ALGORITHM TAKES TIME TO MATCH. SO IF THERE IS NO TENANT HASHING ALGORITHM HAS NOTHING TO HASH AND
         * COMPARE AGAINST. BY PROVIDING A DUMMY HASH IT ADDS A DUMMY HASHING TIME.
         * */

        String hash = tenant != null ? tenant.getPasswordHash() : DUMMY_HASH;
        boolean passwordMatches = passwordEncoder.matches(password, hash);

        if (tenant == null || !passwordMatches) {
            throw new RuntimeException("Invalid Credentials");
        }

        Shop shop = tenant.getShop();
        String sessionToken = UUID.randomUUID()
                                  .toString();
        String rawCSRFToken = UUID.randomUUID()
                                  .toString();
//        TO RATE LIMIT AT /can-i
        String hashedCSRFToken = passwordEncoder.encode(rawCSRFToken);

        AuthSession session = new AuthSession(tenant.getId(), (shop != null) ? shop
                                                                               .getId() : null, tenant.getPhoneNumber(), UserRole.TENANT, rawCSRFToken, hashedCSRFToken);
        sessionManager.set(sessionToken, session);
        return new LoginResult(rawCSRFToken, sessionToken);
    }

    @Transactional
    public void register(String phoneNumber, String password) {
        requireNonNull(phoneNumber, "Phone number is not provided");
        requireNonNull(password, "Password is not provided");
        requireNonRegisteredPhone(phoneNumber);
        requireStrongPassword(password);

        String passwordHash = passwordEncoder.encode(password);
        tenantService.createTenant(new CreateTenantDTO(phoneNumber, passwordHash));
    }

    private void requireStrongPassword(String password) {
        boolean hasNumbers = password.matches("(.*\\d.*)+");
        boolean hasSpecialCharacters = password.matches("(.*[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>/?~].*)");
        boolean hasSimpleCaseLetters = password.matches("(.*[a-z].*)+");
        boolean hasUpperCaseLetters = password.matches("(.*[A-Z].*)");

        if (!hasNumbers || !hasSpecialCharacters || !hasSimpleCaseLetters || !hasUpperCaseLetters) {
            throw new RuntimeException("Weak Password");
        }
    }

    private void requireNonRegisteredPhone(String phoneNumber) {
        Tenant tenant = tenantRepo.findByPhone(phoneNumber)
                                  .orElse(null);

        if (tenant != null) {
            throw new RuntimeException("Require non registered phone number");
        }

    }

    private void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException("Null Parameter: %s".formatted(message));
        }
    }
}
