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
public class CreateDTO {

	private String name;
	private String phone;

	public void setPhone(String phone) {
		boolean isAPhoneNumber = phone
			.matches("\\+94(7[0-9])\\s(\\d{3})\\s(\\d{4})");
		if (!isAPhoneNumber) {
			throw new IllegalArgumentException("Phone number does not match the enfoced pattern: +947[0-9] XXX XXXX");
		}

		this.phone = phone;
	}
}
