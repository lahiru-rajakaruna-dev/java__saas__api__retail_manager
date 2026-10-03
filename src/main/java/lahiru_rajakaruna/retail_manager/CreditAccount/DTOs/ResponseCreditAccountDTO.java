package lahiru_rajakaruna.retail_manager.CreditAccount.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCreditAccountDTO {

    private UUID id;
    private UUID shopId;
    private String name;
    private String phone;
}
