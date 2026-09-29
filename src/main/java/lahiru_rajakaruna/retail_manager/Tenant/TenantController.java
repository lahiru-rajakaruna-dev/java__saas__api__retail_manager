package lahiru_rajakaruna.retail_manager.Tenant;

import java.util.List;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

	private final TenantService tenantService;

	public TenantController(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	@GetMapping
	public ResponseEntity<List<ResponseDTO>> getAllTenants() {
		List<ResponseDTO> users = this.tenantService
			.getAllTenants();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseDTO> getTenantDetails(
		@PathVariable UUID id) {
		ResponseDTO user = this.tenantService.getTenantById(id);
		return ResponseEntity.ok(user);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ResponseDTO> patchTenant(
		@RequestBody ResponseDTO updates,
		@PathVariable UUID id
	) {

		if (updates.getName().isPresent()) {
			tenantService
				.updateNameById(
					id,
					updates.getName()
						.get()
				);
		}
		if (updates.getPhone().isPresent()) {
			tenantService
				.updatePhoneById(
					id,
					updates.getPhone()
						.get()
				);
		}
		if (updates.getPassword().isPresent()) {
			tenantService
				.updatePassword(
					id,
					updates.getPassword()
						.get()
				);
		}
		if (updates.getShopId().isPresent()) {
			tenantService
				.setShopById(
					id,
					updates.getShopId()
						.get()
				);
		}
		if (updates.getActiveState().isPresent()) {
			if (updates.getActiveState().equals(
				EActiveState.ACTIVE)) {

				tenantService
					.enableTenantProfileById(id);

			} else {

				tenantService.disableTenantProfileById(
					id);
			}
		}

		return ResponseEntity.ok(tenantService.getTenantById(id));
	}

	@PostMapping
	public ResponseEntity<ResponseDTO> createTenant(
		@RequestBody ResponseDTO tenantData
	) {
		ResponseDTO tenant = tenantService.createTenant(tenantData);
		return ResponseEntity.ok(tenant);
	}

}
