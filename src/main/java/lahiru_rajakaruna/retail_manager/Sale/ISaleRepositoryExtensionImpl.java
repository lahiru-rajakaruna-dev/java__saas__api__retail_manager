/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package lahiru_rajakaruna.retail_manager.Sale;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * @author bl4z3
 */
@Repository
public class ISaleRepositoryExtensionImpl implements ISaleRepositoryExtension {

	private final EntityManager em;

	public ISaleRepositoryExtensionImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Sale> findAllByShopId(UUID id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sale> query = cb.createQuery(Sale.class);
		Root<Sale> root = query.from(Sale.class);

		Predicate shopIdMatch = cb.equal(root.get("shop").get("id"), id);

		query.select(root).where(shopIdMatch);

		return em.createQuery(query).getResultList();
	}

}
