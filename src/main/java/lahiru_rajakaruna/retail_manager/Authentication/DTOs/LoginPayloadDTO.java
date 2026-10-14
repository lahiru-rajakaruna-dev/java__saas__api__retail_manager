package lahiru_rajakaruna.retail_manager.Authentication.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginPayloadDTO {
    private String phoneNumber;
    private String password;

    @Override
    public String toString() {
        return "LoginPayloadDTO{" +
                "phone='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
