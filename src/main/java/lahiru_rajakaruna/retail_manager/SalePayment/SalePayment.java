package lahiru_rajakaruna.retail_manager.SalePayment;


import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.Common.BaseEntity;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Money received from the customer as part of a single Sale.
 * A Sale has at most one SalePayment (enforced by the unique constraint on sale_id).
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "sale_payment", uniqueConstraints = @UniqueConstraint(name = "uk_sale_payment_sale", columnNames = "sale_id"))
public class SalePayment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Shop.class)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    @OneToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Sale.class)
    @JoinColumn(name = "sale_id", nullable = false, updatable = false, unique = true)
    private Sale sale;

    @Column(name = "amount", nullable = false, updatable = true, precision = 19, scale = 2)
    private BigDecimal amount;

}
