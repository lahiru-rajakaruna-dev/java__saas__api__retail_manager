package lahiru_rajakaruna.retail_manager.Shop;

import java.util.Optional;
import java.util.UUID;
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
  private boolean isActive;

  public static ShopDTO convertToDTO(Shop shop) {
    if (shop.getOwner() == null) {
      throw new NullPointerException("Owner not found");
    }

    ShopDTO dto = new ShopDTO();

    dto.setId(shop.getId());
    dto.setName(shop.getName());
    dto.setActive(shop.isActive());
    dto.setOwnerId(shop.getOwner().getId());

    return dto;
  }

  public static Shop convertToEntity(ShopDTO dto, Tenant owner) {
    Shop shop = new Shop();

    if (dto.getId().isPresent()) {
      shop.setId(dto.getId().get());
    }
    if (dto.getName().isPresent()) {
      shop.setName(dto.getName().get());
    }
    shop.setOwner(owner);

    return shop;
  }

  public Optional<UUID> getId() {
    return Optional.ofNullable(id);
  }

  public Optional<String> getName() {
    return Optional.ofNullable(name);
  }

  public Optional<Boolean> getActiveState() {
    return Optional.ofNullable(isActive);
  }
}
