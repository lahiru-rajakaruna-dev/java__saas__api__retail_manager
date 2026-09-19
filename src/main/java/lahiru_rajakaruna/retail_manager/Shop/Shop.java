package lahiru_rajakaruna.retail_manager.Shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Shop extends BaseEntity {
    @Column(name = "name", nullable = false, updatable = true)
    private String name;
}
