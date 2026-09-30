package lahiru_rajakaruna.retail_manager.SalePayment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISalePaymentRepository extends JpaRepository<SalePayment, UUID>, ISalePaymentRepositoryExtension {
}
