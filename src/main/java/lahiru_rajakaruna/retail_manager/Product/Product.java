/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 *
 * @author bl4z3
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product
        extends BaseEntity {

    @JoinColumn(name = "shop_id", updatable = false, nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Shop shop;

    @Column(name = "name", nullable = false, updatable = true)
    private String name;

    @Column(name = "price", nullable = false, updatable = true)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false, updatable = true)
    private BigDecimal quantity;

    @Column(name = "unit", nullable = false, updatable = true)
    private MessurementUnit unit;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_active", nullable = false, updatable = true)
    private EActiveState activeState;
}
