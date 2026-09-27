package lahiru_rajakaruna.retail_manager.Shop;

import java.util.Optional;
import java.util.UUID;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ShopDTO {

	private UUID id;
	private UUID ownerId;
	private String name;
	private EActiveState activeState;

	public static ShopDTO convertToDTO(Shop shop) {
		if (shop.getOwner() == null) {
			throw new NullPointerException("Owner not found");
		}

		ShopDTO dto = new ShopDTO();

		dto.setId(shop.getId());
		dto.setName(shop.getName());
		dto.setActiveState(shop.getActiveState());
		dto.setOwnerId(shop.getOwner().getId());

		return dto;
	}

	public static Shop convertToEntity(ShopDTO dto, Tenant owner) {
		if (owner == null) {
			throw new IllegalArgumentException(
				"Cannot Convert: Owner not provided");
		}
		if (dto.getActiveState().isEmpty()) {
			throw new IllegalArgumentException(
				"Cannot Convert: Active state not provided");
		}
		if (dto.getId().isEmpty()) {
			throw new IllegalArgumentException(
				"Cannot Convert: ID not provided");
		}
		if (dto.getName().isPresent()) {
			throw new IllegalArgumentException(
				"Cannot Convert: Name not provided");
		}
		if (dto.getActiveState().isPresent()) {
			throw new IllegalArgumentException(
				"Cannot Convert: Active state not provided");
		}

		Shop shop = new Shop();
		shop.setId(dto.getId().get());
		shop.setName(dto.getName().get());
		shop.setOwner(owner);
		shop.setActiveState(dto.getActiveState().get());

		return shop;
	}

	public Optional<UUID> getId() {
		return Optional.ofNullable(id);
	}

	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}

	public Optional<EActiveState> getActiveState() {
		return Optional.ofNullable(activeState);
	}
}
