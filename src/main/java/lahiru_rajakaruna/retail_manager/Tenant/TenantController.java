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
	public ResponseEntity<List<TenantDTO>> getAllTenants() {
		List<TenantDTO> users = this.tenantService
			.getAllTenants();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TenantDTO> getTenantDetails(
		@PathVariable UUID id)
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

	@PutMapping("/{id}")
	public ResponseEntity<TenantDTO> putTenant(
		@RequestBody TenantDTO updates,
		@PathVariable UUID id
	) {
		if (id == null) {
			throw new RuntimeException("ID not provided");
		}

		return ResponseEntity.ok(tenantService
			.updateProfile(id, updates));
	}

	@PostMapping
	public ResponseEntity<TenantDTO> createTenant(
		@RequestBody TenantDTO tenantData
	) {
		TenantDTO tenant = tenantService.createTenant(tenantData);
		return ResponseEntity.ok(tenant);
	}

}
