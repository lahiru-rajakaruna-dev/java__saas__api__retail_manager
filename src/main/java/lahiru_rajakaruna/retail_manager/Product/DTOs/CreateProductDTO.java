/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product.DTOs;

import lahiru_rajakaruna.retail_manager.Product.EMessurmentUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author bl4z3
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateProductDTO {
    private UUID shopId;
    private String name;
    private BigDecimal price;
    private BigDecimal quantity;
    private EMessurmentUnit unit;

    private void requireNonNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireZeroOrPositive(BigDecimal value, String message) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public void setPrice(BigDecimal value) {
        requireNonNull(value, "Price cannot be null");
        requireZeroOrPositive(value, "Price cannot be negative");
        this.price = value;
    }

    public void setQuantity(BigDecimal value) {
        requireNonNull(value, "Quantity cannot be null");
        requireZeroOrPositive(value, "Quantity cannot be negative");
        this.quantity = value;
    }
}
