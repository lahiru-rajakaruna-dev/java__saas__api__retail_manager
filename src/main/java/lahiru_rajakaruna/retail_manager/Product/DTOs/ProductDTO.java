/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product.DTOs;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.EActiveState;
import lahiru_rajakaruna.retail_manager.Product.MessurementUnit;
import lahiru_rajakaruna.retail_manager.Product.Product;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author bl4z3
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProductDTO {

    private UUID id;
    private UUID shopId;
    private String name;
    private BigDecimal price;
    private BigDecimal quantity;
    private MessurementUnit unit;
    private EActiveState activeState;

    public Optional<UUID> getShopId() {
        return Optional.ofNullable(shopId);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<BigDecimal> getPrice() {
        return Optional.ofNullable(price);
    }

    public Optional<BigDecimal> getQuantity() {
        return Optional.ofNullable(quantity);
    }

    public Optional<MessurementUnit> getUnit() {
        return Optional.ofNullable(unit);
    }

    public Optional<EActiveState> getActiveState() {
        return Optional.ofNullable(activeState);
    }

    public Optional<UUID> getId() {
        return Optional.ofNullable(id);
    }

    public static Product convertToEntity(ProductDTO dto, Shop shop) {
        if (shop == null) {
            throw new IllegalArgumentException(
                    "Shop is not provided");
        }
        if (dto.getId().
               isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot Convert: Id is not provided");
        }
        if (dto.getName().
               isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot Convert: Name is not provided");
        }
        if (dto.getPrice().
               isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Price is not provided");
        }
        if (dto.getQuantity().
               isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: quantity not provided");
        }
        if (dto.getUnit()
               .isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Unit not provided");
        }
        if (dto.getActiveState()
               .isEmpty()) {
            throw new IllegalArgumentException("Cannot Convert: Active state not provided");
        }

        Product product = new Product();

        product.setId(dto.getId().
                         get());
        product.setName(dto.getName().
                           get());
        product.setPrice(dto.getPrice().
                            get());
        product.setQuantity(dto.getQuantity().
                               get());
        product.setUnit(dto.getUnit()
                           .get());
        product.setActiveState(dto.getActiveState()
                                  .get());
        product.setShop(shop);

        return product;
    }

    public static ProductDTO convertToDTO(Product entity) {
        ProductDTO dto = new ProductDTO();

        if (entity.getShop() == null) {
            throw new NullPointerException("Shop not found");
        }

        dto.setId(entity.getId());
        dto.setShopId(
                entity.getShop().
                      getId()
        );
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setActiveState(entity.getActiveState());

        return dto;
    }
}
