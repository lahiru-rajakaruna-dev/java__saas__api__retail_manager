package lahiru_rajakaruna.retail_manager.CreditAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICreditAccountRepository extends JpaRepository<CreditAccount, UUID> {

    List<CreditAccount> findAllByShop_Id(UUID shopId);

    boolean existsByShop_IdAndPhone(UUID shopId, String phone);

    @Query("""
            select ca from CreditAccount ca
            where ca.shop.id = :shopId
              and (
                          lower(ca.name) like lower(concat('%', :term, '%'))
                   or ca.phone like concat('%', :term, '%')
                )
            """)
    List<CreditAccount> search(@Param("shopId") UUID shopId, @Param("term") String term);

    @Query("""
                select account from CreditAccount account
                where account.shop.id = :shopId and account.phone = :phone
            
            """)
    Optional<CreditAccount> findByPhone(@Param("shopId") UUID shopId, @Param("phone") String phone);

}