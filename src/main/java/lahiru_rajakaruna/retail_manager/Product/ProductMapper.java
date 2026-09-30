package lahiru_rajakaruna.retail_manager.Product;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Product.DTOs.CreateProductDTO;
import lahiru_rajakaruna.retail_manager.Product.DTOs.ResponseProductDTO;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

import java.math.BigDecimal;
import java.util.function.Function;

public class ProductMapper {
    private ProductMapper() {
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

    private static void mustBeNonNegative(BigDecimal value, String message) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static Product convertToProduct(CreateProductDTO dto, Shop shop) {
        objectAndPropertyMustBeNonNull(shop, Shop::getId, "Shop cannot be null");

        requireNonNull(dto.getName(), "Product name cannot be null");
        requireNonNull(dto.getPrice(), "Product price cannot be null");
        requireNonNull(dto.getQuantity(), "Product quantity cannot be null");
        requireNonNull(dto.getUnit(), "Product measurement unit cannot be null");

        mustBeNonNegative(dto.getPrice(), "Product price must be non negative");
        mustBeNonNegative(dto.getQuantity(), "Product quantity must be non negative");

        Product product = new Product();
        product.setShop(shop);
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setUnit(dto.getUnit());
        product.setActiveState(EActiveState.INACTIVE);
        return product;
    }

    public static ResponseProductDTO convertToDTO(Product entity) {
        objectAndPropertyMustBeNonNull(entity.getShop(), Shop::getId, "Product shop cannot be null");
        requireNonNull(entity.getId(), "Product id cannot be null");
        requireNonNull(entity.getName(), "Product name cannot be null");
        requireNonNull(entity.getPrice(), "Product price cannot be null");
        requireNonNull(entity.getQuantity(), "Product quantity cannot be null");
        requireNonNull(entity.getUnit(), "Product measurement unit cannot be null");
        requireNonNull(entity.getActiveState(), "Product state cannot be null");

        mustBeNonNegative(entity.getPrice(), "Product price cannot be negative");
        mustBeNonNegative(entity.getQuantity(), "Product quantity cannot be negative");

        ResponseProductDTO dto = new ResponseProductDTO();
        dto.setId(entity.getId());
        dto.setShopId(entity.getShop()
                            .getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setState(entity.getActiveState());
        return dto;
    }
}
