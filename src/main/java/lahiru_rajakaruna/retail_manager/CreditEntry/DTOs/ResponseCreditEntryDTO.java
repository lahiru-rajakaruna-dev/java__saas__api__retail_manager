package lahiru_rajakaruna.retail_manager.CreditEntry.DTOs;


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
public class ResponseCreditEntryDTO {

    private UUID id;
    private UUID shopId;
    private UUID saleId;
    private UUID creditAccountId;
    private BigDecimal originalAmount;
    private BigDecimal totalReceivedAmount;
    private BigDecimal outstandingAmount;

    public void setOriginalAmount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Original amount cannot be null");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Original amount must be greater than zero");
        }

        this.originalAmount = value;
    }

    public void setTotalReceivedAmount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Total received amount cannot be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total received amount must zero or greater");
        }

        this.totalReceivedAmount = value;
    }

    public void setOutstandingAmount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Outstanding amount cannot be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Outstanding amount must zero or greater");
        }

        this.totalReceivedAmount = value;
    }
}