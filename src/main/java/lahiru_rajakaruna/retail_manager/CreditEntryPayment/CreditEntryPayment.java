package lahiru_rajakaruna.retail_manager.CreditEntryPayment;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.Common.BaseEntity;
import lahiru_rajakaruna.retail_manager.CreditEntry.CreditEntry;
import lahiru_rajakaruna.retail_manager.CreditPayment.CreditPayment;
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
@Table(name = "credit_entry_payment")
@Check(constraints = "allocation > 0")
public class CreditEntryPayment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_entry_id", nullable = false, updatable = false)
    private CreditEntry creditEntry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_payment_id", nullable = false, updatable = false)
    private CreditPayment creditPayment;

    // Allocations are an immutable ledger: never updated after creation
    @Column(name = "allocation", nullable = false, updatable = false, precision = 19, scale = 2)
    private BigDecimal allocation;

}
