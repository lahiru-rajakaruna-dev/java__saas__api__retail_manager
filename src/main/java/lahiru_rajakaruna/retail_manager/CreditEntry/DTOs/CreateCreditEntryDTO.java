package lahiru_rajakaruna.retail_manager.CreditEntry.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Internal DTO: used by the sale-finalization flow, not exposed through a controller endpoint.
 * {@code amount} is the unpaid balance of the sale that becomes credit.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateCreditEntryDTO {

    private UUID saleId;
    private UUID creditAccountId;
    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be greater than zero");
        }
        this.amount = amount;
    }
}
