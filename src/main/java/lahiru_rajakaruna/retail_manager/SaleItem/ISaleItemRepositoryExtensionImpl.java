package lahiru_rajakaruna.retail_manager.SaleItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ISaleItemRepositoryExtensionImpl implements ISaleItemRepositoryExtension {
    private EntityManager em;

    public ISaleItemRepositoryExtensionImpl(EntityManager em) {
        if (em == null) {
            throw new Error("Entity Manager Is Null");
        }
        this.em = em;
    }

    @Override
    public List<SaleItem> getSaleItemsBySaleId(UUID saleId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SaleItem> query = cb.createQuery(SaleItem.class);
        Root<SaleItem> root = query.from(SaleItem.class);

        Predicate itMatchesSaleId = cb.equal(root.get("sale")
                                                 .get("id"), saleId);

        query.select(root)
             .where(itMatchesSaleId);

        List<SaleItem> saleItems = em.createQuery(query)
                                     .getResultList()
                                     .stream()
                                     .toList();

        return saleItems;
    }


}
