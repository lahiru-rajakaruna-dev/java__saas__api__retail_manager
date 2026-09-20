package lahiru_rajakaruna.retail_manager.Shop;

import lombok.*;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ShopDTO {
    private UUID id;
    private String name;
    private boolean isActive;

    public static ShopDTO convertToDTO(Shop shop) {
        ShopDTO dto = new ShopDTO();

        dto.setId(shop.getId());
        dto.setName(shop.getName());
        dto.setActive(shop.isActive());

        return dto;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }
}
