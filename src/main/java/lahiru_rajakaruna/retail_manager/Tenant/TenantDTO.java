package lahiru_rajakaruna.retail_manager.Tenant;

import java.util.Optional;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class TenantDTO {

	private UUID id = null;

	private String name = null;

	private String phone = null;

	private String password = null;

	private String passwordHash = null;

	private EActiveState activeState = null;

	private UUID shopId = null;

	public static TenantDTO convertToDTO(Tenant tenantEntity) {
		TenantDTO dto = new TenantDTO();
		dto.setId(tenantEntity.getId());

		if (tenantEntity.getName() != null) {
			dto.setName(tenantEntity.getName());
		}
		else {
			throw new RuntimeException("Cannot Convert: Tenant name is null");
		}

		if (tenantEntity.getPhone() != null) {
			dto.setPhone(tenantEntity.getPhone());
		}
		else {
			throw new RuntimeException("Cannot Convert: Tenant phone is null");
		}

		if (tenantEntity.getPasswordHash() != null) {
			dto.setPasswordHash(tenantEntity.getPasswordHash());
		}
		else {
			throw new RuntimeException("Cannot Convert: Tenant password hash is null");
		}

		if (tenantEntity.getShop() != null) {
			dto.setShopId(tenantEntity.getShop().getId());
		}

		if (tenantEntity.getActiveState() != null) {
			dto.setActiveState(tenantEntity.getActiveState());
		}
		else {
			throw new RuntimeException("Cannot Convert: Tenant active state is null");
		}

		return dto;
	}

	public static Tenant convertToEntity(TenantDTO dto, PasswordEncoder passwordEncoder) {
		Tenant tenant = new Tenant();

		if (dto.getName().isPresent()) {
			tenant.setName(dto.getName().get());
		}
		else {
			throw new RuntimeException("Cannot Convert: Name not provided");
		}

		if (dto.getPhone().isPresent()) {
			tenant.setPhone(dto.getPhone().get());
		}
		else {
			throw new RuntimeException("Cannot Convert: Phone not provided");
		}

		if (dto.getPasswordHash().isPresent()) {
			tenant.setPasswordHash(dto.getPasswordHash().get());
		}
		else if (dto.getPassword().isPresent()) {
			tenant.setPasswordHash(passwordEncoder.encode(dto.getPassword().get()));
		}
		else {
			throw new RuntimeException("Cannot Convert:  Password not provided");
		}

		if (dto.getActiveState().isPresent()) {
			tenant.setActiveState(dto.getActiveState().get());
		}
		else {
			throw new RuntimeException("Cannot Convert: Active state not provided");
		}

		return tenant;
	}

	public UUID getId() {
		return this.id;
	}

	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	public Optional<String> getPhone() {
		return Optional.ofNullable(this.phone);
	}

	public Optional<String> getPasswordHash() {
		return Optional.ofNullable(this.passwordHash);
	}

	public Optional<UUID> getShopId() {
		return Optional.ofNullable(this.shopId);
	}

	public Optional<String> getPassword() {
		return Optional.ofNullable(this.password);
	}

	public Optional<EActiveState> getActiveState() {
		return Optional.ofNullable(activeState);
	}

}
