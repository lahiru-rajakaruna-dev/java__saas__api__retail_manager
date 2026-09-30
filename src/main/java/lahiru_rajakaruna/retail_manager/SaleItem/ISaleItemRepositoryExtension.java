package lahiru_rajakaruna.retail_manager.SaleItem;

import java.util.List;
import java.util.UUID;

public interface ISaleItemRepositoryExtension {

    public List<SaleItem> getSaleItemsBySaleId(UUID saleId);

}
