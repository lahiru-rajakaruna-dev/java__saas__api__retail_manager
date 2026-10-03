package lahiru_rajakaruna.retail_manager.SalePayment.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDetailsDTO {
    private String name;
    private String phone;

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (name.length() >= 30) {
            throw new IllegalArgumentException("Name must be less than 30 characters");
        }
        this.name = name;
    }

    public void setPhone(String phone) {
        if (phone == null || !phone.matches("\\+94(7[0-9])\\s(\\d{3})\\s(\\d{4})")) {
            throw new IllegalArgumentException("Phone number does not match the enforced pattern: +947X XXX XXXX");
        }
        this.phone = phone;
    }
}
