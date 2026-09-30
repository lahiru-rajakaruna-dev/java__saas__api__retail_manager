/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Tenant;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author bl4z3
 */
public class TenantMapper {

    private TenantMapper() {
    }

    public static Tenant convertToTenant(CreateDTO dto, PasswordEncoder passwordEncoder) {
        if (dto.getName() == null || dto.getName().isBlank() || dto
                .getName().isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid name");
        }
        if (dto.getPhone() == null || dto.getPhone().isBlank() || dto
                .getPhone().isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid phone number");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank() || dto
                .getPassword().isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid password");
        }

        Tenant t = new Tenant();

        t.setName(dto.getName());
        t.setPhone(dto.getPhone());
        t.setActiveState(EActiveState.INACTIVE);

        String encodedPassword = passwordEncoder.encode(dto
                .getPassword());

        t.setPasswordHash(encodedPassword);

        return t;
    }

    public static ResponseDTO convertToResponseDTO(Tenant entity) {
        if (entity.getName() == null || entity.getName().isBlank() || entity
                .getName().isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid name");
        }
        if (entity.getPhone() == null || entity.getPhone().isBlank() || entity
                .getPhone().isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid phone number");
        }
        if (entity.getPasswordHash() == null || entity.getPasswordHash()
                .isBlank() || entity
                .getPasswordHash().isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Must provided a valid password hash");
        }

        ResponseDTO dto = new ResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setActiveState(entity.getActiveState());

        if (entity.getShop() != null && entity.getShop().getId() != null) {
            dto.setShopId(entity.getShop().getId());
        }

        return dto;
    }
}
