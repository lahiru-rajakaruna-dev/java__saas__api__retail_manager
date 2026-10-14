package lahiru_rajakaruna.retail_manager.Authentication.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterPayloadDTO {
    private String phoneNumber;
    private String password;

    public void setPassword(String password) {
        checkPasswordStrength(password);
        this.password = password;
    }

    private void checkPasswordStrength(String password) {
        boolean lengthGreaterThan8 = password.length() >= 8;
        boolean hasNumbers = password.matches(".*\\d.*");
        boolean hasSpecialCharacters = password
                .matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+.*");

        if (!lengthGreaterThan8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!hasNumbers) {
            throw new IllegalArgumentException("Password must contain numbers");
        }
        if (!hasSpecialCharacters) {
            throw new IllegalArgumentException("Password must contain special characters");
        }
    }
}
