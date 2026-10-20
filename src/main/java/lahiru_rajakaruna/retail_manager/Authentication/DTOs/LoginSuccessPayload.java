package lahiru_rajakaruna.retail_manager.Authentication.DTOs;

import lahiru_rajakaruna.retail_manager.Common.ResponsePayload;
import lombok.Getter;

@Getter
public class LoginSuccessPayload extends ResponsePayload {
    private final String csrfToken;

    public LoginSuccessPayload(String message, String csrfToken) {
        super(message);
        this.csrfToken = csrfToken;
    }
}
