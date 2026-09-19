package lahiru_rajakaruna.retail_manager.AbstractBaseClasses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
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

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp(source = SourceType.DB)
    protected Instant createdAt;
    @Column(name = "updated_at", nullable = false, updatable = true)
    @UpdateTimestamp(source = SourceType.DB)
    protected Instant updatedAt;
}
