package lahiru_rajakaruna.retail_manager.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
public class User extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
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
