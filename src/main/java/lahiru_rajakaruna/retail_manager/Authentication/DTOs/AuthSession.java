package lahiru_rajakaruna.retail_manager.Authentication.DTOs;

import lahiru_rajakaruna.retail_manager.Authentication.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
public class AuthSession {
    private UUID userId;
    private UUID shopId;
    private String phone;
    private UserRole role;
    private String csrfToken;
}
