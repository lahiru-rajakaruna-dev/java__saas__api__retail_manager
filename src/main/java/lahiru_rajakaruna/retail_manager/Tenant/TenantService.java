package lahiru_rajakaruna.retail_manager.Tenant;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateDTO;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.ResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

	private final ITenantRepository tenantRepo;

	private final IShopRepository shopRepo;

	private final PasswordEncoder passwordEncoder;

	public TenantService(ITenantRepository tenantRepo,
			     PasswordEncoder passwordEncoder,
			     IShopRepository shopRepo) {
		this.tenantRepo = tenantRepo;
		this.passwordEncoder = passwordEncoder;
		this.shopRepo = shopRepo;
	}

	public ResponseDTO createTenant(CreateDTO tenantData) {
		checkIfInternalComponentsNull();

		if (tenantData.getName() == null) {
			throw new IllegalArgumentException("Name not provided");
		}
		if (tenantData.getPhone() == null) {
			throw new IllegalArgumentException("Phone not provided");
		}
		if (tenantData.getPassword() == null) {
			throw new IllegalArgumentException(
				"Password not provided");
		}

		Tenant newTenant = TenantMapper
			.convertToTenant(tenantData, passwordEncoder);

		Tenant savedTenant = this.tenantRepo.saveAndFlush(newTenant);

		return TenantMapper.convertToResponseDTO(savedTenant);
	}

	public List<ResponseDTO> getAllTenants() {
		checkIfInternalComponentsNull();
		return this.tenantRepo.findAll().stream().map(
			TenantMapper::convertToResponseDTO).toList();
	}

	public ResponseDTO getTenantById(UUID id) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Tenant tenant = this.tenantRepo.findById(id).orElseThrow(
			() -> new RuntimeException("Tenant not found"));
		return TenantMapper.convertToResponseDTO(tenant);
	}

	public ResponseDTO updateNameById(UUID id, String name) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		if (name == null) {
			throw new IllegalArgumentException(
				"Name parameter is null");
		}

		Tenant tenant = tenantRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find tenant with ID: %s", id.toString())));

		tenant.setName(name);
		return TenantMapper
			.convertToResponseDTO(tenantRepo.save(tenant));
	}

	public ResponseDTO updatePhoneById(UUID id, String phone) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		if (phone == null) {
			throw new IllegalArgumentException(
				"Name parameter is null");
		}

		Tenant tenant = tenantRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find tenant with ID: %s", id.toString())));

		tenant.setPhone(phone);
		return TenantMapper
			.convertToResponseDTO(tenantRepo.save(tenant));
	}

	public ResponseDTO setShopById(UUID id, UUID shopId) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		if (shopId == null) {
			throw new IllegalArgumentException(
				"ShopId parameter is null");
		}
		Shop shop = shopRepo.findById(shopId)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find shop with ID: %s", id.toString())));

		Tenant tenant = tenantRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find tenant with ID: %s", id.toString())));

		tenant.setShop(shop);
		return TenantMapper
			.convertToResponseDTO(tenantRepo.save(tenant));
	}

	public ResponseDTO updatePassword(UUID id, String password) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		if (password == null) {
			throw new IllegalArgumentException(
				"Password parameter is null");
		}

		String passwordHash = passwordEncoder.encode(password);

		Tenant tenant = tenantRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find tenant with ID: %s", id.toString())));

		tenant.setPasswordHash(passwordHash);
		return TenantMapper
			.convertToResponseDTO(tenantRepo.save(tenant));
	}

	public ResponseDTO disableTenantProfileById(UUID id) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Tenant tenant = tenantRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find tenant with ID: %s", id.toString())));

		tenant.setActiveState(EActiveState.INACTIVE);
		return TenantMapper
			.convertToResponseDTO(tenantRepo.save(tenant));
	}

	public ResponseDTO enableTenantProfileById(UUID id) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		Tenant tenant = tenantRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find tenant with ID: %s", id.toString())));

		tenant.setActiveState(EActiveState.ACTIVE);
		return TenantMapper
			.convertToResponseDTO(tenantRepo.save(tenant));
	}

	public ResponseDTO updateProfile(UUID id, ResponseDTO updates) {
		checkIfInternalComponentsNull();

		if (id == null) {
			throw new IllegalArgumentException(
				"ID parameter is null");
		}

		boolean isNameNull = updates.getName().isEmpty();
		boolean isPhoneNull = updates.getPhone().isEmpty();
		boolean isPasswordNull = updates.getPassword().isEmpty();
		boolean isActiveStateNull = updates.getActiveState().isEmpty();
		boolean isShopNull = updates.getShopId().isEmpty();

		if (isActiveStateNull || isNameNull || isPasswordNull || isPhoneNull || isShopNull) {
			throw new IllegalArgumentException(
				"All Fields Must Be Present");
		}

		Tenant tenant = tenantRepo.findById(id)
			.orElseThrow(() -> new RuntimeException(String.format(
			"Could not find tenant with ID: %s", id.toString())));

		if (updates.getShopId().isPresent()) {
			Shop shop = shopRepo.findById(updates.getShopId().get())
				.orElseThrow(() -> new RuntimeException(String
				.format("Could not find the shop with ID: %s",
					updates.getShopId().get())));

			tenant.setShop(shop);
		} else {
			tenant.setShop(null);
		}

		tenant.setName(updates.getName().get());
		tenant.setPhone(updates.getPhone().get());
		String passwordHash = passwordEncoder.encode(updates
			.getPassword().get());
		tenant.setPasswordHash(passwordHash);
		tenant.setActiveState(updates.getActiveState().get());

		return TenantMapper
			.convertToResponseDTO(tenantRepo.save(tenant));
	}

	private void checkIfInternalComponentsNull() {
		Objects.requireNonNull(tenantRepo, "Tenant Repo Not Found");
		Objects.requireNonNull(shopRepo, "Shop Repo Not Found");
		Objects.requireNonNull(passwordEncoder,
				       "Password Encoder Not Found");
	}

}
