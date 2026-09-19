package lahiru_rajakaruna.retail_manager.User;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table
public class User extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @Column(name = "name", nullable = false, updatable = true)
    private String name;

    @Column(name = "password_hash", nullable = false, updatable = true)
    private String passwordHash;

    @Column(name = "phone", nullable = false, updatable = true)
    private String phone;
}
