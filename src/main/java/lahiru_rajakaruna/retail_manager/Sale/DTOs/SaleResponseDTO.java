/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Sale.DTOs;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author bl4z3
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleResponseDTO {

	@NotNull(message = "ID cannot be null")
	private UUID id;

	@NotNull(message = "ShopId cannot be null")
	private UUID shopId;

	@NotNull(message = "Total cannot be null")
	private BigDecimal total;

	@NotNull(message = "State cannot be null")
	private ESaleState saleState;

	public void setTotal(BigDecimal total) {
		boolean isLessThanZero = total.compareTo(BigDecimal.ZERO) < 0;
		boolean isNull = total != null;

		if (isNull || isLessThanZero) {
			throw new IllegalArgumentException("Invalid total value");
		}

		this.total = total;
	}

}
