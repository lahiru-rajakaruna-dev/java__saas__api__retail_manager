package lahiru_rajakaruna.retail_manager.SalePayment;

import java.util.Optional;
import java.util.UUID;

public interface ISalePaymentRepositoryExtension {
    public boolean existsBySaleId(UUID saleId);

    public Optional<SalePayment> findBySaleId(UUID saleId);
}
