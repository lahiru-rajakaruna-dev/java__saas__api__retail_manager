package lahiru_rajakaruna.retail_manager.Tenant;

import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
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

        Tenant tenant = this.tenantRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (updates.getName().isPresent()) {
            tenant.setName(updates.getName().get());
        }

        if (updates.getPhone().isPresent()) {
            tenant.setPhone(updates.getPhone().get());
        }

        if (updates.getPassword().isPresent()) {
            String hash = this.passwordEncoder.encode(updates.getPassword().get());
            tenant.setPasswordHash(hash);
        }

        if (updates.getShopId().isPresent()) {
            Shop shop = this.shopRepo.findById(updates.getShopId().get()).orElseThrow(() -> new RuntimeException("Could not find the shop"));
            tenant.setShop(shop);
        }

        return TenantDTO.convertToDTO(this.tenantRepo.save(tenant));
    }

    public TenantDTO putUser(UUID id, TenantDTO replacement) throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("ID parameter is null");
        }

        Tenant tenant = this.tenantRepo.findById(id).orElseThrow(() -> new RuntimeException("Could not find the user"));

        if (replacement.getName().isPresent()) {
            tenant.setName(replacement.getName().get());
        } else {
            tenant.setName(null);
        }

        if (replacement.getPhone().isPresent()) {
            tenant.setPhone(replacement.getPhone().get());
        } else {
            tenant.setPhone(null);
        }

        if (replacement.getShopId().isPresent()) {
            Shop shop = this.shopRepo.findById(replacement.getShopId().get()).orElseThrow(() -> new RuntimeException("Could not find the shop"));
            tenant.setShop(shop);
        } else {
            tenant.setShop(null);
        }

        return TenantDTO.convertToDTO(this.tenantRepo.save(tenant));
    }
}
