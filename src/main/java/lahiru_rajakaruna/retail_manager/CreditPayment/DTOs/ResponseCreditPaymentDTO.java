package lahiru_rajakaruna.retail_manager.CreditPayment.DTOs;

import lahiru_rajakaruna.retail_manager.CreditEntryPayment.DTOs.ResponseCreditEntryPaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCreditPaymentDTO {

    private UUID id;
    private UUID shopId;
    private UUID creditAccountId;
    private BigDecimal amount;
    private Instant timestamp;
    private List<ResponseCreditEntryPaymentDTO> allocations;
}
