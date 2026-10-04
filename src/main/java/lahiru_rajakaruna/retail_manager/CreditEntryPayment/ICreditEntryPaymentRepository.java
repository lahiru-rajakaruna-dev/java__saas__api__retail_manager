package lahiru_rajakaruna.retail_manager.CreditEntryPayment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ICreditEntryPaymentRepository extends JpaRepository<CreditEntryPayment, UUID> {

    List<CreditEntryPayment> findAllByCreditPayment_IdOrderByCreatedAtAscIdAsc(UUID creditPaymentId);

    List<CreditEntryPayment> findAllByCreditPayment_IdIn(Collection<UUID> creditPaymentIds);

    List<CreditEntryPayment> findAllByCreditEntry_IdOrderByCreatedAtAscIdAsc(UUID creditEntryId);
}
