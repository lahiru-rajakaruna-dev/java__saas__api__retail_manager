package lahiru_rajakaruna.retail_manager.SaleItem;

import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.CreateSaleItemDTO;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.SaleItemResponseDTO;
import lahiru_rajakaruna.retail_manager.Shop.Shop;

import java.math.BigDecimal;

public class SaleItemMapper {

    private SaleItemMapper() {
    }

    public static SaleItemResponseDTO convertToDTO(SaleItem saleItem) {

        if (saleItem == null) {
            throw new IllegalArgumentException("Cannot Convert: SaleItem cannot be null");
        }

        boolean isIdNull = saleItem.getId() == null;

        boolean isShopNull = saleItem.getShop() == null;
        boolean isShopIdNull = !isShopNull && saleItem.getShop().getId() == null;

        boolean isSaleNull = saleItem.getSale() == null;
        boolean isSaleIdNull = !isSaleNull && saleItem.getSale().getId() == null;

        boolean isProductNull = saleItem.getProduct() == null;
        boolean isProductIdNull = !isProductNull && saleItem.getProduct().getId() == null;

        boolean isPriceNull = saleItem.getPrice() == null;
        boolean isPriceLessThanZero = !isPriceNull && saleItem.getPrice().compareTo(BigDecimal.ZERO) < 0;

        boolean isQuantityNull = saleItem.getQuantity() == null;
        boolean isQuantityLessThanZero = !isQuantityNull && saleItem.getQuantity().compareTo(BigDecimal.ZERO) < 0;

        boolean isDiscountNull = saleItem.getDiscount() == null;
        boolean isDiscountLessThanZero = !isDiscountNull && saleItem.getDiscount().compareTo(BigDecimal.ZERO) < 0;

        boolean isTotalNull = saleItem.getTotal() == null;
        boolean isTotalLessThanZero = !isTotalNull && saleItem.getTotal().compareTo(BigDecimal.ZERO) < 0;

        if (isIdNull) {
            throw new IllegalArgumentException("Cannot Convert: Id cannot be null");
        }
        if (isShopNull || isShopIdNull) {
            throw new IllegalArgumentException("Cannot Convert: Shop or ShopId cannot be null");
        }
        if (isSaleNull || isSaleIdNull) {
            throw new IllegalArgumentException("Cannot Convert: Sale or SaleId cannot be null");
        }
        if (isProductNull || isProductIdNull) {
            throw new IllegalArgumentException("Cannot Convert: Product or ProductId cannot be null");
        }
        if (isPriceNull || isPriceLessThanZero) {
            throw new IllegalArgumentException("Cannot Convert: Invalid price");
        }
        if (isQuantityNull || isQuantityLessThanZero) {
            throw new IllegalArgumentException("Cannot Convert: Invalid quantity");
        }
        if (isDiscountNull || isDiscountLessThanZero) {
            throw new IllegalArgumentException("Cannot Convert: Invalid discount");
        }
        if (isTotalNull || isTotalLessThanZero) {
            throw new IllegalArgumentException("Cannot Convert: Invalid total");
        }

        SaleItemResponseDTO dto = new SaleItemResponseDTO();
        dto.setId(saleItem.getId());
        dto.setShopId(saleItem.getShop().getId());
        dto.setSaleId(saleItem.getSale().getId());
        dto.setProductId(saleItem.getProduct().getId());
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

        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot Convert: Must provide a valid price");
        }
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot Convert: Must provide a valid quantity");
        }
        if (dto.getDiscount() == null || dto.getDiscount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot Convert: Must provide a valid discount");
        }
        if (dto.getTotal() == null || dto.getTotal().compareTo(BigDecimal.ZERO) < 0) {
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