package lahiru_rajakaruna.retail_manager.CreditEntryPayment.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCreditEntryPaymentDTO {

    private UUID id;
    private UUID shopId;
    private UUID creditEntryId;
    private UUID creditPaymentId;
    private BigDecimal allocation;
    private Instant timestamp;
}
