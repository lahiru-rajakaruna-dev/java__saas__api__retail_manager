package lahiru_rajakaruna.retail_manager.Tenant;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.Common.BaseEntity;
import lahiru_rajakaruna.retail_manager.Common.EActiveState;
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
public class Tenant extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @Column(name = "password_hash", nullable = false, updatable = true)
    private String passwordHash;

    @Column(name = "phone_number", nullable = false, updatable = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", nullable = false, updatable = true)
    private EActiveState state;

}
