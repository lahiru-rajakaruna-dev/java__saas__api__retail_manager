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

    private BigDecimal price;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal total;

    public void setPrice(BigDecimal price) {
        boolean isNull = price == null;
        boolean isLessThanZero = !isNull && price.compareTo(BigDecimal.ZERO) < 0;

        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid value for price");
        }

        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        boolean isNull = quantity == null;
        boolean isLessThanZero = !isNull && quantity < 0;

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

    public void setTotal(BigDecimal total) {
        boolean isNull = total == null;
        boolean isLessThanZero = !isNull && total.compareTo(BigDecimal.ZERO) < 0;

        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid value for total");
        }

        this.total = total;
    }
}