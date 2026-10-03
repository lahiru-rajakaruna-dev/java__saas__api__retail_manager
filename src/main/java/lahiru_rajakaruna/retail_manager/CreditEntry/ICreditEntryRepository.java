package lahiru_rajakaruna.retail_manager.CreditEntry;


import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICreditEntryRepository extends JpaRepository<CreditEntry, UUID> {

    boolean existsBySale_Id(UUID saleId);

    Optional<CreditEntry> findBySale_Id(UUID saleId);

    List<CreditEntry> findAllByCreditAccount_IdOrderByCreatedAtAscIdAsc(UUID creditAccountId);

    @Query("""
            select e from CreditEntry e
            where e.creditAccount.id = :creditAccountId
              and e.totalReceivedAmount < e.originalAmount
              and e.state = ECreditEntryState.PENDING
            order by e.createdAt asc, e.id asc
            """)
    List<CreditEntry> findOutstandingEntriesByCreditAccountId(@Param("creditAccountId") UUID creditAccountId);

    /**
     * Oldest outstanding entries first, with a row lock so that concurrent payments
     * cannot over-allocate the same entry. Must be called inside a transaction.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select e from CreditEntry e
            where e.creditAccount.id = :creditAccountId
              and e.totalReceivedAmount < e.originalAmount
              and e.state = ECreditEntryState.PENDING
            order by e.createdAt asc, e.id asc
            """)
    List<CreditEntry> findOutstandingEntriesByCreditAccountIdOrderedByCreatedAt(@Param("creditAccountId") UUID creditAccountId);
}
