/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author bl4z3
 */
@NoArgsConstructor
@AllArgsConstructor
public class ProductRepositoryExtentionImpl implements IProductRepositoryExtention {

	private final EntityManager entityManager;

	@Autowired
	public ProductRepositoryExtentionImpl(EntityManager em) {
		this.entityManager = em;
	}

	@Override
	public List<Product> findAllByShopId(UUID shopId) {
		if (shopId == null) {
			throw new IllegalArgumentException(
				"Shop id parameter not provided");
		}

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> query = cb.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);
		Predicate matchesShopId = cb.equal(root.get("shop_id"), shopId);
		query.select(root).where(matchesShopId);

		return entityManager.createQuery(query).getResultList();

	}

}
