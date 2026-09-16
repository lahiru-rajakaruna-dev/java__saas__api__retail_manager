package lahiru_rajakaruna.retail_manager.Shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lombok.*;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@Entity
public class Shop extends BaseEntity {
    @Column
    private String name;

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }
}
