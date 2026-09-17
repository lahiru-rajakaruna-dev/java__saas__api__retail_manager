package lahiru_rajakaruna.retail_manager.AbstractBaseClasses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue()
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id")
    protected UUID id;

    @Column(name = "time_stamp", nullable = false, updatable = true)
    protected Instant timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected Instant createdAt;
    @Column(name = "updated_at", nullable = false, updatable = true)
    protected Instant updatedAt;
}
