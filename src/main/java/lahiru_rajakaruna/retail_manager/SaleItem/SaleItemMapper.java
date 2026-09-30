package lahiru_rajakaruna.retail_manager.SaleItem;

import lahiru_rajakaruna.retail_manager.Product.Product;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.CreateSaleItemDTO;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.SaleItemResponseDTO;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

import java.math.BigDecimal;
import java.util.function.Function;

public class SaleItemMapper {

    private SaleItemMapper() {
    }

    private static <T> boolean isPropertyOrValueNull(T parent, Function<T, ?> property) {
        return parent == null || property.apply(parent) == null;
    }

    private static boolean isValueNullOrNegative(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static SaleItemResponseDTO convertToDTO(SaleItem saleItem) {

        if (saleItem == null) {
            throw new IllegalArgumentException("Cannot Convert: SaleItem cannot be null");
        }

        boolean isIdNull = saleItem.getId() == null;


        boolean isShopInvalid = isPropertyOrValueNull(saleItem.getShop(), Shop::getId);
        boolean isSaleInvalid = isPropertyOrValueNull(saleItem.getSale(), Sale::getId);
        boolean isProductInvalid = isPropertyOrValueNull(saleItem.getProduct(), Product::getId);

        boolean isPriceInvalid = isValueNullOrNegative(saleItem.getPrice());
        boolean isQuantityInvalid = isValueNullOrNegative(saleItem.getQuantity());
        boolean isDiscountInvalid = isValueNullOrNegative(saleItem.getDiscount());
        boolean isTotalInvalid = isValueNullOrNegative(saleItem.getTotal());

        if (isIdNull) {
            throw new IllegalArgumentException("Cannot Convert: Id cannot be null");
        }
        if (isShopInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Shop or ShopId cannot be null");
        }
        if (isSaleInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Sale or SaleId cannot be null");
        }
        if (isProductInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Product or ProductId cannot be null");
        }
        if (isPriceInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Invalid price");
        }
        if (isQuantityInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Invalid quantity");
        }
        if (isDiscountInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Invalid discount");
        }
        if (isTotalInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Invalid total");
        }

        SaleItemResponseDTO dto = new SaleItemResponseDTO();
        dto.setId(saleItem.getId());
        dto.setShopId(saleItem.getShop()
                              .getId());
        dto.setSaleId(saleItem.getSale()
                              .getId());
        dto.setProductId(saleItem.getProduct()
                                 .getId());
        dto.setPrice(saleItem.getPrice());
        dto.setQuantity(saleItem.getQuantity());
        dto.setDiscount(saleItem.getDiscount());
        dto.setTotal(saleItem.getTotal());

        return dto;
    }

    public static SaleItem convertToSaleItem(CreateSaleItemDTO dto, Shop shop, Sale sale) {

        if (dto == null) {
            throw new IllegalArgumentException("Cannot Convert: DTO cannot be null");
        }

        boolean isPriceInvalid = isValueNullOrNegative(dto.getPrice());
        boolean isQuantityInvalid = isValueNullOrNegative(dto.getQuantity());
        boolean isDiscountInvalid = isValueNullOrNegative(dto.getDiscount());
        boolean isTotalInvalid = isValueNullOrNegative(dto.getTotal());

        if (isPriceInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Must provide a valid price");
        }
        if (isQuantityInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Must provide a valid quantity");
        }
        if (isDiscountInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Must provide a valid discount");
        }
        if (isTotalInvalid) {
            throw new IllegalArgumentException("Cannot Convert: Must provide a valid total");
        }

        SaleItem saleItem = new SaleItem();

        saleItem.setPrice(dto.getPrice());
        saleItem.setQuantity(dto.getQuantity());
        saleItem.setDiscount(dto.getDiscount());
        saleItem.setTotal(dto.getTotal());
        saleItem.setShop(shop);
        saleItem.setSale(sale);

        return saleItem;
    }
}