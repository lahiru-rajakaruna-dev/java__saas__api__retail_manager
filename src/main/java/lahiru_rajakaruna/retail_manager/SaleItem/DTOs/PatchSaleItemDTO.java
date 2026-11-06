package lahiru_rajakaruna.retail_manager.SaleItem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchSaleItemDTO {
    private BigDecimal quantity;
    private BigDecimal discount;

    public void setQuantity(BigDecimal quantity) {
        boolean isNull = quantity == null;
        boolean isLessThanZero = !isNull && quantity.compareTo(BigDecimal.ZERO) < 0;
        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid value for quantity");
        }
        this.quantity = quantity;
    }

    public void setDiscount(BigDecimal discount) {
        boolean isNull = discount == null;
        boolean isLessThanZero = !isNull && discount.compareTo(BigDecimal.ZERO) < 0;
        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid value for discount");
        }
        this.discount = discount;
    }
}