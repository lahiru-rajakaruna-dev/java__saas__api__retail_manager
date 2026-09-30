package lahiru_rajakaruna.retail_manager.Tenant.DTOs;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ResponseDTO {

    private UUID id = null;

    private String name = null;

    private String phone = null;

    private EActiveState activeState = null;

    private UUID shopId = null;
}
