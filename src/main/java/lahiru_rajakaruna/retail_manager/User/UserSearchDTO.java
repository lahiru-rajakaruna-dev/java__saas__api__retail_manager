package lahiru_rajakaruna.retail_manager.User;

import java.util.Optional;
import java.util.UUID;

public class UserSearchDTO {
    private String name;
    private String phone;
    private UUID storeId;

    public UserSearchDTO() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<String> getPhone() {
        return Optional.ofNullable(this.phone);
    }

    public Optional<UUID> getStoreId() {
        return Optional.ofNullable(this.storeId);
    }
}
