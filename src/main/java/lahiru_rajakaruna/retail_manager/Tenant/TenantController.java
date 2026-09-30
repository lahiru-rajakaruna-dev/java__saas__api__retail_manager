package lahiru_rajakaruna.retail_manager.Tenant;

import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.PatchDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseDTO>> getAllTenants() {
        List<ResponseDTO> users = this.tenantService.getAllTenants();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getTenantDetails(@PathVariable UUID id) {
        ResponseDTO user = this.tenantService.getTenantById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> patchTenant(@RequestBody PatchDTO updates, @PathVariable UUID id) {
        ResponseDTO updatedTenant = tenantService.patchProfileById(id, updates);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(updatedTenant);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createTenant(@RequestBody CreateDTO tenantData) {
        ResponseDTO tenant = tenantService.createTenant(tenantData);
        return ResponseEntity.status(HttpStatus.CREATED).body(tenant);
    }

}
