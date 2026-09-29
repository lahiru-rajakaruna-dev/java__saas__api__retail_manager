package lahiru_rajakaruna.retail_manager.Sale.DTOs;

import java.math.BigDecimal;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatchSaleDTO {

	private BigDecimal total;
	private ESaleState state;

	public void setTotal(BigDecimal total) {
		boolean isNull = total == null;
		boolean isLessThanZero = total.compareTo(BigDecimal.ZERO) < 0;

		if (isNull || isLessThanZero) {
			throw new IllegalArgumentException("Invalid value for total");
		}

		this.total = total;
	}

}
