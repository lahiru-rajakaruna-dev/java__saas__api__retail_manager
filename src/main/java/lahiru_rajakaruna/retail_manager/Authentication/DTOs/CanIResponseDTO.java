package lahiru_rajakaruna.retail_manager.Authentication.DTOs;

import lahiru_rajakaruna.retail_manager.Authentication.ResponseErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CanIResponseDTO {
    private ResponseErrorType error = null;
    private String csrf = null;
    private String message = null;
}
