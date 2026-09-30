package lahiru_rajakaruna.retail_manager.SaleItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISaleItemRepository extends JpaRepository<SaleItem, UUID>, ISaleItemRepositoryExtension {

}
