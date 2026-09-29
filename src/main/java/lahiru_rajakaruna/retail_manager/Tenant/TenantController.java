package lahiru_rajakaruna.retail_manager.Tenant;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.PatchDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;

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
		return ResponseEntity.status(HttpStatus.OK).body(tenant);
	}

}
