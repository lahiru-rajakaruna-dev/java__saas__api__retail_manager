package lahiru_rajakaruna.retail_manager.SalePayment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Optional;
import java.util.UUID;

public class ISalePaymentRepositoryExtensionImpl implements ISalePaymentRepositoryExtension {
    private EntityManager em;

    public ISalePaymentRepositoryExtensionImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean existsBySaleId(UUID saleId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> query = cb.createQuery(SalePayment.class);
        Root<SalePayment> root = query.from(SalePayment.class);
        Predicate itMatchesSaleId = cb.equal(root.get("sale")
                                                 .get("id"), saleId);

        query.select(root)
             .where(itMatchesSaleId);
        int index = em.createQuery(query)
                      .getFirstResult();

        return index != 0;
    }

    @Override
    public Optional<SalePayment> findBySaleId(UUID saleId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SalePayment> query = cb.createQuery(SalePayment.class);
        Root<SalePayment> root = query.from(SalePayment.class);
        Predicate itMatchesSaleId = cb.equal(root.get("sale")
                                                 .get("id"), saleId);

        query.select(root)
             .where(itMatchesSaleId);
        SalePayment payment = em.createQuery(query)
                                .getSingleResult();

        return Optional.ofNullable(payment);
    }
}
