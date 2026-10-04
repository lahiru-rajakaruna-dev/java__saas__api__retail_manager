package lahiru_rajakaruna.retail_manager.CreditPayment;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.Common.BaseEntity;
import lahiru_rajakaruna.retail_manager.CreditAccount.CreditAccount;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "credit_payment")
@Check(constraints = "amount > 0")
public class CreditPayment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_account_id", nullable = false, updatable = false)
    private CreditAccount creditAccount;

    @Column(name = "amount", nullable = false, updatable = false, precision = 19, scale = 2)
    private BigDecimal amount;

}
