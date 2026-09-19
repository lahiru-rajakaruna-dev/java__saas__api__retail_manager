package lahiru_rajakaruna.retail_manager.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class UserDTO {
    private UUID id;
    private String name;
    private String phone;
    private String password;
    private String passwordHash;
    private UUID shopId;

    public static UserDTO convertToDTO(User userEntity) {
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());

        if (userEntity.getName() != null) {
            dto.setName(userEntity.getName());
        }
        if (userEntity.getPhone() != null) {
            dto.setPhone(userEntity.getPhone());
        }
        if (userEntity.getPasswordHash() != null) {
            dto.setPasswordHash(userEntity.getPasswordHash());
        }
        if (userEntity.getShop() != null) {
            dto.setShopId(userEntity.getShop().getId());
        }
        return dto;
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
}
