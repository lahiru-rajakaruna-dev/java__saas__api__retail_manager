package lahiru_rajakaruna.retail_manager.Shop.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ShopResponseDTO {

    @NotNull(message = "ID cannot be null")
    private UUID id;
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot only consist of whitespace characters")
    private String name;
    private EActiveState activeState;
}
