package lahiru_rajakaruna.retail_manager.Tenant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {
    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        List<TenantDTO> users = this.tenantService.getAllTenants();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getTenantDetails(@PathVariable UUID id)
    throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("ID is not provided");
        }

        TenantDTO user = this.tenantService.getTenantById(id);
        return ResponseEntity.ok(user);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<TenantDTO> patchTenant(
            @RequestBody TenantDTO updates,
            @PathVariable UUID id
                                                )
    throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("ID is not provided");
        }

        if (updates.getName().isPresent()) {
            return ResponseEntity.ok(tenantService.updateNameById(
                    id,
                    updates.getName()
                           .get()
                                                                 ));
        }
        if (updates.getPhone().isPresent()) {
            return ResponseEntity.ok(tenantService.updatePhoneById(
                    id,
                    updates.getPhone()
                           .get()
                                                                  ));
        }
        if (updates.getPassword().isPresent()) {
            return ResponseEntity.ok(tenantService.updatePassword(
                    id,
                    updates.getPassword()
                           .get()
                                                                 ));
        }
        if (updates.getShopId().isPresent()) {
            return ResponseEntity.ok(tenantService.setShopById(
                    id,
                    updates.getShopId()
                           .get()
                                                              ));
        }
        if (updates.isActive().isPresent()) {
            if (updates.isActive().get()) {
                return ResponseEntity.ok(
                        tenantService.enableTenantProfileById(id)
                                        );
            } else {
                return ResponseEntity.ok(
                        tenantService.disableTenantProfileById(id)
                                        );
            }
        }

        throw new RuntimeException("Invalid Request");
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantDTO> putTenant(
            @RequestBody TenantDTO updates,
            @PathVariable UUID id
                                              ) {
        if (id == null) {
            throw new RuntimeException("ID not provided");
        }

        return ResponseEntity.ok(tenantService.updateProfile(id, updates));
    }

    @PostMapping
    public ResponseEntity<TenantDTO> createTenant(
            @RequestBody TenantDTO tenantData
                                                 ) {
        TenantDTO tenant = tenantService.createTenant(tenantData);
        return ResponseEntity.ok(tenant);
    }

}
