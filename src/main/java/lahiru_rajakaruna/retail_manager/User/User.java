package lahiru_rajakaruna.retail_manager.User;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @OneToOne()
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @Column(name = "name", nullable = false, updatable = true)
    private String name;

    @Column(name = "password_hash", nullable = false, updatable = true)
    private String passwordHash;

    @Column(name = "phone", nullable = false, updatable = true)
    private String phone;

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<String> getPasswordHash() {
        return Optional.ofNullable(this.passwordHash);
    }

    public Optional<String> getPhone() {
        return Optional.ofNullable(this.phone);
    }

    public Optional<Shop> getShop() {
        return Optional.ofNullable(this.shop);
    }
}
