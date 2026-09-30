package lahiru_rajakaruna.retail_manager.Shop;

import jakarta.persistence.*;
import lahiru_rajakaruna.retail_manager.Common.BaseEntity;
import lahiru_rajakaruna.retail_manager.Common.EActiveState;
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "active_state", nullable = false, updatable = true)
    private EActiveState activeState;
}
