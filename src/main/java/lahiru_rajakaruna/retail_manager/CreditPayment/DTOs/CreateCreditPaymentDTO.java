package lahiru_rajakaruna.retail_manager.CreditPayment.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/** The credit account comes from the URL path, so only the amount is supplied. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateCreditPaymentDTO {

    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        boolean isNull = amount == null;
        boolean isNotPositive = !isNull && amount.compareTo(BigDecimal.ZERO) <= 0;

        if (isNull || isNotPositive) {
            throw new IllegalArgumentException("Invalid value for amount");
        }

        this.amount = amount;
    }
}
