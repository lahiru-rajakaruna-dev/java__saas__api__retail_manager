/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Tenant;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateTenantDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author bl4z3
 */
public class TenantMapper {

    private TenantMapper() {
    }

    public static Tenant convertToTenant(CreateTenantDTO dto, PasswordEncoder passwordEncoder) {
        if (dto.getPhoneNumber() == null || dto.getPhoneNumber()
                                               .isBlank()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid phone number");
        }
        if (dto.getPassword() == null || dto.getPassword()
                                            .isBlank()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid password");
        }

        Tenant t = new Tenant();

        t.setPhoneNumber(dto.getPhoneNumber());
        t.setState(EActiveState.INACTIVE);

        String encodedPassword = passwordEncoder.encode(dto
                                                                .getPassword());

        t.setPasswordHash(encodedPassword);

        return t;
    }

    public static ResponseDTO convertToResponseDTO(Tenant entity) {
        if (entity.getPhoneNumber() == null || entity.getPhoneNumber()
                                                     .isBlank()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid phone number");
        }
        if (entity.getPasswordHash() == null || entity.getPasswordHash()
                                                      .isBlank()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid password hash");
        }

        ResponseDTO dto = new ResponseDTO();
        dto.setId(entity.getId());
        dto.setPhone(entity.getPhoneNumber());
        dto.setActiveState(entity.getState());

        if (entity.getShop() != null && entity.getShop()
                                              .getId() != null) {
            dto.setShopId(entity.getShop()
                                .getId());
        }

        return dto;
    }
}
