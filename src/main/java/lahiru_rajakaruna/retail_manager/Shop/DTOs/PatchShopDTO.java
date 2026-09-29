package lahiru_rajakaruna.retail_manager.Shop.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchShopDTO {
	@NotNull(message = "Shop name cannot be null")
	@NotBlank(message = "Shop name cannot only contain whitespace characters")
	private String name;
	@NotNull(message = "Shop state cannot be null")
	private EActiveState activeState;
}
