package lahiru_rajakaruna.retail_manager.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITenantRepository extends JpaRepository<Tenant, UUID> {

    @Query("""
                select tenant from Tenant tenant  where
                tenant.phone like concat('%', :phone, '%')
            """)
    public Optional<Tenant> findByPhone(@Param("phone") String phone);
}
