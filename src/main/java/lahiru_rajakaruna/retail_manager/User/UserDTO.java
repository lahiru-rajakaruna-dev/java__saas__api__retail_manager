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

        if (userEntity.getName().isPresent()) {
            dto.setName(userEntity.getName().get());
        }
        if (userEntity.getPhone().isPresent()) {
            dto.setPhone(userEntity.getPhone().get());
        }
        if (userEntity.getPasswordHash().isPresent()) {
            dto.setPasswordHash(userEntity.getPasswordHash().get());
        }
        if (userEntity.getShop().isPresent()) {
            dto.setShopId(userEntity.getShop().get().getId());
        }
        return dto;
    }

    public UUID getId() {
        return this.id;
    }

    public Optional<String> getName() {
        return Optional.of(this.name);
    }

    public Optional<String> getPhone() {
        return Optional.of(this.phone);
    }

    public Optional<String> getPasswordHash() {
        return Optional.of(this.passwordHash);
    }

    public Optional<UUID> getShopId() {
        return Optional.ofNullable(this.shopId);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(this.password);
    }
}
