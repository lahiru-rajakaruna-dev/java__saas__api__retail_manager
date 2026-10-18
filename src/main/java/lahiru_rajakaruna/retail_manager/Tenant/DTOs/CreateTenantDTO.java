/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Tenant.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author bl4z3
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTenantDTO {

    private String phoneNumber;
    private String password;

    public void setPassword(String password) {
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

        this.password = password;

    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll(" ", "");
        boolean isAPhoneNumber = phoneNumber
                .matches("\\+94(7[0-9])(\\d{3})(\\d{4})");
        if (!isAPhoneNumber) {
            throw new IllegalArgumentException("Phone number does not match the enforced pattern: +947[0-9]XXXXXXX");
        }

        this.phoneNumber = phoneNumber;
    }
}
