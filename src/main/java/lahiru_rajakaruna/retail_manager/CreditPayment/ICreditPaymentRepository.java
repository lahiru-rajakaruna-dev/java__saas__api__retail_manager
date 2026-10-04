package lahiru_rajakaruna.retail_manager.CreditPayment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICreditPaymentRepository extends JpaRepository<CreditPayment, UUID> {

    List<CreditPayment> findAllByCreditAccount_IdOrderByCreatedAtDescIdDesc(UUID creditAccountId);
}
