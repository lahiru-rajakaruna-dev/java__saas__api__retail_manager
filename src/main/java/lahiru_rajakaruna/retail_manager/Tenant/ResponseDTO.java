package lahiru_rajakaruna.retail_manager.Tenant;

import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ResponseDTO {

	private UUID id = null;

	private String name = null;

	private String phone = null;

	private String password = null;

	private EActiveState activeState = null;

	private UUID shopId = null;
}
