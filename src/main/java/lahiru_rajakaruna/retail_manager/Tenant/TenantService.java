package lahiru_rajakaruna.retail_manager.Tenant;

import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TenantService {
    private final ITenantRepository tenantRepo;
    private final IShopRepository shopRepo;
    private final PasswordEncoder passwordEncoder;

    public TenantService(ITenantRepository tenantRepo,
                         PasswordEncoder passwordEncoder,
                         IShopRepository shopRepo) {
        this.tenantRepo = tenantRepo;
        this.passwordEncoder = passwordEncoder;
        this.shopRepo = shopRepo;
    }

    public List<TenantDTO> getAllUsers() {
        return this.tenantRepo.findAll()
                              .stream()
                              .map(TenantDTO::convertToDTO)
                              .toList();
    }

    public TenantDTO getUserById(UUID id) throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        Tenant tenant = this.tenantRepo.findById(id)
                                       .orElseThrow(() -> new RuntimeException(
                                               "User not found"));
        return TenantDTO.convertToDTO(tenant);
    }

    public TenantDTO updateNameById(UUID id, String name) {
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        if (name == null) {
            throw new RuntimeException("Name parameter is null");
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
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        if (phone == null) {
            throw new RuntimeException("Name parameter is null");
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
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        if (shopId == null) {
            throw new RuntimeException("ShopId parameter is null");
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
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        if (password == null) {
            throw new RuntimeException("Password parameter is null");
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
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
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
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
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
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        Tenant tenant = tenantRepo.findById(id)
                                  .orElseThrow(() -> new RuntimeException(String.format(
                                          "Could not find tenant with ID: %s",
                                          id.toString()
                                                                                       )));

        if (updates.getName().isPresent()) {
            tenant.setName(updates.getName().get());
        } else {
            throw new RuntimeException("Name cannot be empty");
        }

        if (updates.getPhone().isPresent()) {
            tenant.setPhone(updates.getPhone().get());
        } else {
            throw new RuntimeException("Phone number cannot be empty");
        }

        if (updates.getPassword().isPresent()) {
            String passwordHash = passwordEncoder.encode(updates.getPassword()
                                                                .get());
            tenant.setPasswordHash(passwordHash);
        } else {
            throw new RuntimeException("Password cannot be empty");
        }

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
            throw new RuntimeException("Profile state cannot be null");
        }

        return TenantDTO.convertToDTO(tenantRepo.save(tenant));
    }
}
