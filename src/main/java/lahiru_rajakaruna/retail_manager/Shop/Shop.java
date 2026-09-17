package lahiru_rajakaruna.retail_manager.Shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lombok.*;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name = "shops")
public class Shop extends BaseEntity {
    @Column(name = "name")
    private String name;

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }
}
