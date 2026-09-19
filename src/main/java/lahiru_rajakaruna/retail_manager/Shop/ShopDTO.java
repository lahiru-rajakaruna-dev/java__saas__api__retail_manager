package lahiru_rajakaruna.retail_manager.Shop;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class ShopDTO {
    private UUID id;
    private String name;

    public UUID getId() {
        return this.id;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }
}
