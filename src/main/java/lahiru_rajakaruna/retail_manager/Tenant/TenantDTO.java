package lahiru_rajakaruna.retail_manager.Tenant;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class TenantDTO {
    private UUID id;
    private String name;
    private String phone;
    private String password;
    private String passwordHash;
    private boolean isActive;
    private UUID shopId;

    public static TenantDTO convertToDTO(Tenant tenantEntity) {
        TenantDTO dto = new TenantDTO();
        dto.setId(tenantEntity.getId());

        if (tenantEntity.getName() != null) {
            dto.setName(tenantEntity.getName());
        }
        if (tenantEntity.getPhone() != null) {
            dto.setPhone(tenantEntity.getPhone());
        }
        if (tenantEntity.getPasswordHash() != null) {
            dto.setPasswordHash(tenantEntity.getPasswordHash());
        }
        if (tenantEntity.getShop() != null) {
            dto.setShopId(tenantEntity.getShop().getId());
        }
        if (tenantEntity.isActive()) {
            dto.setActive(true);
        } else {
            dto.setActive(false);
        }
        return dto;
    }

    public static Tenant convertToEntity(TenantDTO dto,
                                         PasswordEncoder passwordEncoder) {
        Tenant tenant = new Tenant();

        if (dto.getName().isPresent()) {
            tenant.setName(dto.getName().get());
        } else {
            throw new RuntimeException("Cannot Convert: Name not provided");
        }

        if (dto.getPhone().isPresent()) {
            tenant.setPhone(dto.getPhone().get());
        } else {
            throw new RuntimeException("Cannot Convert: Phone not provided");
        }

        if (dto.getPasswordHash().isPresent()) {
            tenant.setPasswordHash(dto.getPasswordHash().get());
        } else if (dto.getPassword().isPresent()) {
            tenant.setPasswordHash(passwordEncoder.encode(dto.getPassword()
                                                             .get()));
        } else {
            throw new RuntimeException("Cannot Convert:  Password not provided");
        }

        return tenant;
    }

    public UUID getId() {
        return this.id;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<String> getPhone() {
        return Optional.ofNullable(this.phone);
    }

    public Optional<String> getPasswordHash() {
        return Optional.ofNullable(this.passwordHash);
    }

    public Optional<UUID> getShopId() {
        return Optional.ofNullable(this.shopId);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(this.password);
    }

    public Optional<Boolean> isActive() {
        return Optional.ofNullable(isActive);
    }
}
