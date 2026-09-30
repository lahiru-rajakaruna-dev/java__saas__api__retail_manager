package lahiru_rajakaruna.retail_manager.SalePayment.DTOs;

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
public class ResponseSalePaymentDTO {

    @NotNull(message = "ID cannot be null")
    private UUID id;

    @NotNull(message = "ShopId cannot be null")
    private UUID shopId;

    @NotNull(message = "SaleId cannot be null")
    private UUID saleId;

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    public void setAmount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}
