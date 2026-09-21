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
    public ResponseEntity<List<TenantDTO>> getAllUsers() {
        List<TenantDTO> users = this.tenantService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getUserDetails(@PathVariable UUID id)
    throws RuntimeException {
        if (id == null) {
            throw new RuntimeException("ID is not provided");
        }

        TenantDTO user = this.tenantService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<TenantDTO> patchUser(@RequestBody TenantDTO updates, @PathVariable UUID id) throws RuntimeException {
        TenantDTO updatedUser = this.tenantService.patchUser(id, updates);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantDTO> putUser(@RequestBody TenantDTO updates,
                                             @PathVariable UUID id) {
        if (id == null) {
            throw new RuntimeException("ID not provided");
        }

        return ResponseEntity.ok(tenantService.updateProfile(id, updates));
    }

}
