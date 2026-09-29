package lahiru_rajakaruna.retail_manager.Shop.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateShopDTO {
	@NotNull(message = "Shop name cannnot be null")
	@NotBlank(message = "Shop name cannot only contain whitespace characters")
	private String name;
	private UUID ownerId;
}
