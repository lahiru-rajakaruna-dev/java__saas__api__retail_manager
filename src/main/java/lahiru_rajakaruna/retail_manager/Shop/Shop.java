package lahiru_rajakaruna.retail_manager.Shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.BaseEntity;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
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

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Tenant.class)
	@JoinColumn(name = "owner_id", updatable = false, nullable = false,
		    unique = true)
	private Tenant owner;

	@Column(name = "name", nullable = false, updatable = true)
	private String name;

	@Column(name = "active_state", nullable = false, updatable = true)
	private EActiveState activeState;
}
