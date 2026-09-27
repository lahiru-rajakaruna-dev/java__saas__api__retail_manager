/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author bl4z3
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Sale extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Shop.class)
	@JoinColumn(name = "shop_id", nullable = false, updatable = false)
	private Shop shop;

	@Column(name = "total", nullable = false, updatable = true)
	private BigDecimal total;

	@Column(name = "is_closed", nullable = false, updatable = true)
	private boolean isClosed = false;

}
