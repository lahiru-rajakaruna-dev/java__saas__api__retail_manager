package lahiru_rajakaruna.retail_manager.Tenant;

import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TenantService {
    private final ITenantRepository tenantRepo;
    private final IShopRepository shopRepo;
    private final PasswordEncoder passwordEncoder;

    public TenantService(
            ITenantRepository tenantRepo,
            PasswordEncoder passwordEncoder,
            IShopRepository shopRepo
                        ) {
        this.tenantRepo = tenantRepo;
        this.passwordEncoder = passwordEncoder;
        this.shopRepo = shopRepo;
    }

    public TenantDTO createTenant(TenantDTO tenant) {
        checkIfInternalComponentsNull();

        if (tenant.getName().isEmpty()) {
            throw new IllegalArgumentException("Name not provided");
        }
        if (tenant.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone not provided");
        }
        if (tenant.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password not provided");
        }

        return TenantDTO.convertToDTO(
                this.tenantRepo.save(
                        TenantDTO.convertToEntity(
                                tenant, passwordEncoder
                                                 )
                                    )
                                     );
    }

    public List<TenantDTO> getAllTenants() {
        checkIfInternalComponentsNull();

        return this.tenantRepo.findAll()
                              .stream()
                              .map(TenantDTO::convertToDTO)
                              .toList();
    }

    public TenantDTO getTenantById(UUID id) throws RuntimeException {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        Tenant tenant = this.tenantRepo.findById(id)
                                       .orElseThrow(() -> new RuntimeException(
                                               "Tenant not found"));
        return TenantDTO.convertToDTO(tenant);
    }

    public TenantDTO updateNameById(UUID id, String name) {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name parameter is null");
        }

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        tenant.setName(name);
        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }

    public TenantDTO updatePhoneById(UUID id, String phone) {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        if (phone == null) {
            throw new IllegalArgumentException("Name parameter is null");
        }

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        tenant.setPhone(phone);
        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }

    public TenantDTO setShopById(UUID id, UUID shopId) {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        if (shopId == null) {
            throw new IllegalArgumentException("ShopId parameter is null");
        }
        Shop shop = shopRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s",
                                    id.toString()
                                                                                 )));

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        tenant.setShop(shop);
        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }

    public TenantDTO updatePassword(UUID id, String password) {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        if (password == null) {
            throw new IllegalArgumentException("Password parameter is null");
        }

        String passwordHash = passwordEncoder.encode(password);

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        tenant.setPasswordHash(passwordHash);
        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }

    public TenantDTO disableTenantProfileById(UUID id) {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        tenant.setActive(false);
        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }

    public TenantDTO enableTenantProfileById(UUID id) {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        tenant.setActive(true);
        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }

    public TenantDTO updateProfile(UUID id, TenantDTO updates) {
        checkIfInternalComponentsNull();

        if (id == null) {
            throw new IllegalArgumentException("ID parameter is null");
        }

        boolean isNameNull = updates.getName().isEmpty();
        boolean isPhoneNull = updates.getPhone().isEmpty();
        boolean isPasswordNull = updates.getPassword().isEmpty();
        boolean isActiveStateNull = updates.isActive().isEmpty();
        boolean isShopNull = updates.getShopId().isEmpty();

        if (isActiveStateNull ||
            isNameNull ||
            isPasswordNull ||
            isPhoneNull ||
            isShopNull) {
            throw new IllegalArgumentException("All Fields Must Be Present");
        }

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        tenant.setName(updates.getName().get());
        tenant.setPhone(updates.getPhone().get());
        String passwordHash = passwordEncoder.encode(updates.getPassword()
                                                            .get());
        tenant.setPasswordHash(passwordHash);

        if (updates.getShopId().isPresent()) {
            Shop shop = shopRepo.findById(updates.getShopId().get())
                                .orElseThrow(() -> new RuntimeException(
                                        String.format(
                                                "Could not find the shop with ID: %s",
                                                id
                                                     )));

            tenant.setShop(shop);
        } else {
            tenant.setShop(null);
        }

        if (updates.isActive().isPresent()) {
            if (updates.isActive().get()) {
                tenant.setActive(true);
            } else {
                tenant.setActive(false);
            }
        } else {
            throw new IllegalArgumentException("Profile state cannot be null");
        }

        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }

    private void checkIfInternalComponentsNull() {
        Objects.requireNonNull(tenantRepo, "Tenant Repo Not Found");
        Objects.requireNonNull(shopRepo, "Shop Repo Not Found");
        Objects.requireNonNull(passwordEncoder, "Password Encoder Not Found");
    }
}
