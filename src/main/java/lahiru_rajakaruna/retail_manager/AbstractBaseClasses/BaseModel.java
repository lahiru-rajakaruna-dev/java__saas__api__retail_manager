package lahiru_rajakaruna.retail_manager.AbstractBaseClasses;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseModel {
	@Id
	@GeneratedValue
	protected UUID id;
	@Column(nullable = false)
	protected Instant timestamp;
	@Column(name = "created_at", nullable = false, updatable = false)
	protected Instant createdAt;
	@Column(name = "updated_at", nullable = false)
	protected Instant updatedAt;
}
