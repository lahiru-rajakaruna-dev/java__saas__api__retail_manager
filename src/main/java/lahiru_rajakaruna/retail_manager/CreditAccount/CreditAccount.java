package lahiru_rajakaruna.retail_manager.CreditAccount;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.Common.BaseEntity;
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
@Table(name = "credit_account",
        uniqueConstraints = @UniqueConstraint(name = "uk_credit_account_shop_phone",
                columnNames = {"shop_id", "phone"}))
public class CreditAccount extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    @Column(name = "name", nullable = false, updatable = true, length = 29)
    private String name;

    @Column(name = "phone", nullable = false, updatable = true)
    private String phone;

}
