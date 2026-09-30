package lahiru_rajakaruna.retail_manager.SaleItem.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleItemResponseDTO {

    @NotNull(message = "ID cannot be null")
    private UUID id;

    @NotNull(message = "ShopId cannot be null")
    private UUID shopId;

    @NotNull(message = "SaleId cannot be null")
    private UUID saleId;

    @NotNull(message = "ProductId cannot be null")
    private UUID productId;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;

    @NotNull(message = "Quantity cannot be null")
    private BigDecimal quantity;

    @NotNull(message = "Discount cannot be null")
    private BigDecimal discount;

    @NotNull(message = "Total cannot be null")
    private BigDecimal total;

    public void setPrice(BigDecimal price) {
        boolean isNull = price == null;
        boolean isLessThanZero = !isNull && price.compareTo(BigDecimal.ZERO) < 0;

        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid price value");
        }

        this.price = price;
    }

    public void setQuantity(BigDecimal quantity) {
        boolean isNull = quantity == null;
        boolean isLessThanZero = !isNull && quantity.compareTo(BigDecimal.ZERO) < 0;

        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid quantity value");
        }

        this.quantity = quantity;
    }

    public void setDiscount(BigDecimal discount) {
        boolean isNull = discount == null;
        boolean isLessThanZero = !isNull && discount.compareTo(BigDecimal.ZERO) < 0;

        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid discount value");
        }

        this.discount = discount;
    }

    public void setTotal(BigDecimal total) {
        boolean isNull = total == null;
        boolean isLessThanZero = !isNull && total.compareTo(BigDecimal.ZERO) < 0;

        if (isNull || isLessThanZero) {
            throw new IllegalArgumentException("Invalid total value");
        }

        this.total = total;
    }
}
