package lahiru_rajakaruna.retail_manager.Authentication;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.AuthSession;
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
    public String login(String phoneNumber, String password) {
        requireNonNull(phoneNumber, "Phone Number Not Provided");
        requireNonNull(password, "Password Not Provided");

        Tenant tenant = tenantRepo.findByPhone(phoneNumber)
                                  .orElse(null);
        Shop shop = tenant.getShop();

        /*
         * THIS IS TO MATCH THE TIME WITH NON-REGISTERED PHONE NUMBERS TO REGISTERED PHONE NUMBER
         * HASHING ALGORITHM TAKES TIME TO MATCH. SO IF THERE IS NO TENANT HASHING ALGORITHM HAS NOTHING TO HASH AND
         * COMPARE AGAINST. BY PROVIDING A DUMMY HASH IT ADDS A DUMMY HASHING TIME.
         * */
        String hash = tenant != null ? tenant.getPasswordHash() : DUMMY_HASH;

        if (!passwordEncoder.matches(password, hash)) {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = UUID.randomUUID()
                           .toString();

        AuthSession session = new AuthSession(tenant.getId(), shop != null ? shop
                                                                             .getId() : null, tenant.getPhone(), UserRole.TENANT);
        sessionManager.set(token, session);
        return token;
    }

    @Transactional
    public void register(String phoneNumber, String password) {
        requireNonNull(phoneNumber, "Phone number is not provided");
        requireNonNull(password, "Password is not provided");

        String passwordHash = passwordEncoder.encode(password);
        tenantService.createTenant(new CreateTenantDTO(phoneNumber, passwordHash));
    }

    private void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException("Null Parameter: %s".formatted(message));
        }
    }
}
