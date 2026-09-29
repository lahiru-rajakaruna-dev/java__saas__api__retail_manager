package lahiru_rajakaruna.retail_manager.Shop.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ShopResponseDTO {

	@NotNull(message = "ID cannot be null")
	private UUID id;
	@NotNull(message = "Owner id cannot be null")
	private UUID ownerId;
	@NotNull(message = "Name cannot be null")
	@NotBlank(message = "Name cannot only consist of whitespace characters")
	private String name;
	private EActiveState activeState;
}
