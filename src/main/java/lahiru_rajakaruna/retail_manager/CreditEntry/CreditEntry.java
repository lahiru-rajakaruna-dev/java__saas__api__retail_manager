package lahiru_rajakaruna.retail_manager.CreditEntry;


import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.Common.BaseEntity;
import lahiru_rajakaruna.retail_manager.CreditAccount.CreditAccount;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
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
@Table(name = "credit_entry")
@Check(constraints = "original_amount > 0 AND total_received_amount >= 0 AND total_received_amount <= original_amount")
public class CreditEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    // Exactly one CreditEntry per Sale -> unique FK
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false, updatable = false, unique = true)
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_account_id", nullable = false, updatable = false)
    private CreditAccount creditAccount;

    // Immutable after creation
    @Column(name = "original_amount", nullable = false, updatable = false, precision = 19, scale = 2)
    private BigDecimal originalAmount;

    @Column(name = "total_received_amount", nullable = false, updatable = true, precision = 19, scale = 2)
    private BigDecimal totalReceivedAmount;

    @Column(name = "state", nullable = false, updatable = true)
    @Enumerated(value = EnumType.STRING)
    private ECreditEntryState state;

    public BigDecimal getOutstandingAmount() {
        return originalAmount.subtract(totalReceivedAmount);
    }
}
