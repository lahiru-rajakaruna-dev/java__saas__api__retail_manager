package lahiru_rajakaruna.retail_manager.Tenant;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.PatchDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
        checkIfInternalComponentsNull();
    }

    public ResponseDTO createTenant(CreateDTO tenantData) {
        checkIfInternalComponentsNull();

        if (tenantData.getName() == null) {
            throw new IllegalArgumentException("Name not provided");
        }
        if (tenantData.getPhone() == null) {
            throw new IllegalArgumentException("Phone not provided");
        }
        if (tenantData.getPassword() == null) {
            throw new IllegalArgumentException(
                    "Password not provided");
        }

        Tenant newTenant = TenantMapper
                .convertToTenant(tenantData, passwordEncoder);

        Tenant savedTenant = this.tenantRepo.saveAndFlush(newTenant);

        return TenantMapper.convertToResponseDTO(savedTenant);
    }

    public List<ResponseDTO> getAllTenants() {
        return this.tenantRepo.findAll()
                              .stream()
                              .map(
                                      TenantMapper::convertToResponseDTO)
                              .toList();
    }

    public ResponseDTO getTenantById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }

        Tenant tenant = this.tenantRepo.findById(id)
                                       .orElseThrow(
                                               () -> new RuntimeException("Tenant not found"));
        return TenantMapper.convertToResponseDTO(tenant);
    }

    @Transactional()
    public ResponseDTO patchProfileById(UUID id, PatchDTO updates) {
        boolean hasNameField = updates.getName() != null;
        boolean hasPhoneField = updates.getPhone() != null;
        boolean hasPasswordField = updates.getPassword() != null;

        Tenant tenant = findTenantByIdOrThrow(id);

        if (hasNameField) {
            tenant.setName(updates.getName());
        }
        if (hasPhoneField) {
            tenant.setPhone(updates.getPhone());
        }
        if (hasPasswordField) {
            String passwordHash = passwordEncoder.encode(updates
                    .getPassword());
            tenant.setPasswordHash(passwordHash);
        }

        Tenant savedTenant = this.tenantRepo.saveAndFlush(tenant);
        return TenantMapper.convertToResponseDTO(savedTenant);
    }

    public ResponseDTO setShopById(UUID id, UUID shopId) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }

        if (shopId == null) {
            throw new IllegalArgumentException(
                    "ShopId parameter is null");
        }
        Shop shop = shopRepo.findById(shopId)
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "Could not find shop with ID: %s", id.toString())));

        Tenant tenant = findTenantByIdOrThrow(id);

        tenant.setShop(shop);
        return TenantMapper
                .convertToResponseDTO(tenantRepo.save(tenant));
    }

    public ResponseDTO disableTenantProfileById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }

        Tenant tenant = findTenantByIdOrThrow(id);
        tenant.setActiveState(EActiveState.INACTIVE);
        return TenantMapper
                .convertToResponseDTO(tenantRepo.save(tenant));
    }

    public ResponseDTO enableTenantProfileById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID parameter is null");
        }

        Tenant tenant = findTenantByIdOrThrow(id);
        tenant.setActiveState(EActiveState.ACTIVE);
        return TenantMapper
                .convertToResponseDTO(tenantRepo.save(tenant));
    }

    private Tenant findTenantByIdOrThrow(UUID id) {
        return tenantRepo.findById(id)
                         .orElseThrow(() -> new RuntimeException("Could not find tenant with ID: %s"
                                 .formatted(id)));
    }

    private void checkIfInternalComponentsNull() {
        Objects.requireNonNull(tenantRepo, "Tenant Repo Not Found");
        Objects.requireNonNull(shopRepo, "Shop Repo Not Found");
        Objects.requireNonNull(passwordEncoder,
                "Password Encoder Not Found");
    }

}
