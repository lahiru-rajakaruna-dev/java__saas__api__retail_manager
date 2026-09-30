package lahiru_rajakaruna.retail_manager.SalePayment;

import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.CreateSalePaymentDTO;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.ResponseSalePaymentDTO;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

import java.math.BigDecimal;
import java.util.function.Function;

public class SalePaymentMapper {
    private SalePaymentMapper() {
    }

    private static <T> void objectAndPropertyMustBeNonNull(T o, Function<T, ?> property, String message) {
        if (o == null || property.apply(o) == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static <T> void requireNonNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void mustBePositive(BigDecimal value, String message) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static SalePayment convertToSalePayment(CreateSalePaymentDTO dto, Sale sale) {
        requireNonNull(dto, "Sale payment data cannot be null");
        requireNonNull(dto.getShopId(), "Sale payment shop id cannot be null");
        requireNonNull(dto.getSaleId(), "Sale payment sale id cannot be null");
        requireNonNull(dto.getAmount(), "Sale payment amount cannot be null");
        objectAndPropertyMustBeNonNull(sale, Sale::getId, "Sale payment sale cannot be null");
        objectAndPropertyMustBeNonNull(sale.getShop(), Shop::getId, "Sale shop cannot be null");

        mustBePositive(dto.getAmount(), "Sale payment amount must be greater than zero");

        if (!sale.getId()
                 .equals(dto.getSaleId())) {
            throw new IllegalArgumentException("Sale payment sale id must match the provided sale");
        }
        if (!sale.getShop()
                 .getId()
                 .equals(dto.getShopId())) {
            throw new IllegalArgumentException("Sale payment shop must match the shop of the sale");
        }

        SalePayment payment = new SalePayment();
        payment.setSale(sale);
        payment.setShop(sale.getShop());
        payment.setAmount(dto.getAmount());
        return payment;
    }

    public static ResponseSalePaymentDTO convertToDTO(SalePayment entity) {
        requireNonNull(entity, "Sale payment cannot be null");
        requireNonNull(entity.getId(), "Sale payment id cannot be null");
        objectAndPropertyMustBeNonNull(entity.getShop(), Shop::getId, "Sale payment shop cannot be null");
        objectAndPropertyMustBeNonNull(entity.getSale(), Sale::getId, "Sale payment sale cannot be null");
        requireNonNull(entity.getAmount(), "Sale payment amount cannot be null");

        mustBePositive(entity.getAmount(), "Sale payment amount must be greater than zero");

        ResponseSalePaymentDTO dto = new ResponseSalePaymentDTO();
        dto.setId(entity.getId());
        dto.setShopId(entity.getShop()
                            .getId());
        dto.setSaleId(entity.getSale()
                            .getId());
        dto.setAmount(entity.getAmount());
        return dto;
    }
}