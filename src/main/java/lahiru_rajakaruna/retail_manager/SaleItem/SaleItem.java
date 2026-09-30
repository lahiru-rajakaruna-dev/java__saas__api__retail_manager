package lahiru_rajakaruna.retail_manager.SaleItem;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lahiru_rajakaruna.retail_manager.Product.Product;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class SaleItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Shop.class)
    @JoinColumn(name = "shop_id", nullable = false, updatable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Sale.class)
    @JoinColumn(name = "sale_id", nullable = false, updatable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Product.class)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    @Column(name = "price", nullable = false, updatable = true)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false, updatable = true)
    private BigDecimal quantity;

    @Column(name = "discount", nullable = false, updatable = true)
    private BigDecimal discount;

    @Column(name = "total", nullable = false, updatable = true)
    private BigDecimal total;

}