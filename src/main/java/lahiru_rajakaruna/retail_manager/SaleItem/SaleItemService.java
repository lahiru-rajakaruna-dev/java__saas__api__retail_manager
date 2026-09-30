package lahiru_rajakaruna.retail_manager.SaleItem;

import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ESaleState;
import lahiru_rajakaruna.retail_manager.Product.IProductRepository;
import lahiru_rajakaruna.retail_manager.Product.Product;
import lahiru_rajakaruna.retail_manager.Sale.ISaleRepository;
import lahiru_rajakaruna.retail_manager.Sale.Sale;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.CreateSaleItemDTO;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.PatchSaleItemDTO;
import lahiru_rajakaruna.retail_manager.SaleItem.DTOs.SaleItemResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SaleItemService {

    private final ISaleItemRepository saleItemRepo;
    private final ISaleRepository saleRepo;
    private final IProductRepository productRepo;

    public SaleItemService(ISaleItemRepository saleItemRepo, ISaleRepository saleRepo, IProductRepository productRepo) {
        this.saleItemRepo = saleItemRepo;
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
        checkInternalComponentsPresence();
    }

    private void checkInternalComponentsPresence() {
        Objects.requireNonNull(saleItemRepo, "Sale Item Repository Not Found");
        Objects.requireNonNull(saleRepo, "Sale Repository Not Found");
        Objects.requireNonNull(productRepo, "Product Repository Not Found");
    }

    @Transactional
    public List<SaleItemResponseDTO> getItemsBySaleId(UUID saleId) {
        requireNonNull(saleId, "Sale ID parameter is null");
        findSaleOrThrow(saleId); // 404-style failure if the sale does not exist
        return saleItemRepo.getSaleItemsBySaleId(saleId)
                           .stream()
                           .map(SaleItemMapper::convertToDTO)
                           .toList();
    }

    @Transactional
    public SaleItemResponseDTO getItemById(UUID saleId, UUID itemId) {
        requireNonNull(saleId, "Sale ID parameter is null");
        requireNonNull(itemId, "Item ID parameter is null");
        SaleItem item = findItemInSaleOrThrow(saleId, itemId);
        return SaleItemMapper.convertToDTO(item);
    }

    @Transactional
    public SaleItemResponseDTO addItem(UUID saleId, CreateSaleItemDTO dto) {
        requireNonNull(saleId, "Sale ID parameter is null");
        requireNonNull(dto, "Sale item data is null");
        requireNonNull(dto.getProductId(), "Must provide a product for the sale item");
        requireNonNull(dto.getPrice(), "Must provide a price for the sale item");
        requireNonNull(dto.getQuantity(), "Must provide a quantity for the sale item");

        Sale sale = findSaleOrThrow(saleId);
        assertSaleIsOpen(sale);

        Product product = productRepo.findById(dto.getProductId())
                                     .orElseThrow(() -> new RuntimeException("Could not find product with ID: %s".formatted(dto.getProductId())));

        BigDecimal discount = dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO;

        dto.setDiscount(discount);
        dto.setTotal(calculateLineTotal(dto.getPrice(), dto.getQuantity(), discount));

        SaleItem item = SaleItemMapper.convertToSaleItem(dto, sale.getShop(), sale);
        item.setProduct(product);

        SaleItem savedItem = saleItemRepo.saveAndFlush(item);
        recalculateSaleTotal(sale);

        return SaleItemMapper.convertToDTO(savedItem);
    }

    @Transactional
    public SaleItemResponseDTO patchItem(UUID saleId, UUID itemId, PatchSaleItemDTO updates) {
        requireNonNull(saleId, "Sale ID parameter is null");
        requireNonNull(itemId, "Item ID parameter is null");
        requireNonNull(updates, "Updates are null");

        SaleItem item = findItemInSaleOrThrow(saleId, itemId);
        Sale sale = item.getSale();
        assertSaleIsOpen(sale);

        if (updates.getPrice() != null) {
            item.setPrice(updates.getPrice());
        }
        if (updates.getQuantity() != null) {
            // PatchSaleItemDTO uses Integer while the entity uses BigDecimal
            item.setQuantity(BigDecimal.valueOf(updates.getQuantity()));
        }
        if (updates.getDiscount() != null) {
            item.setDiscount(updates.getDiscount());
        }

        // Any change to price/quantity/discount invalidates the stored total,
        // so recompute it (a client-supplied total is ignored).
        item.setTotal(calculateLineTotal(item.getPrice(), item.getQuantity(), item.getDiscount()));

        SaleItem savedItem = saleItemRepo.saveAndFlush(item);
        recalculateSaleTotal(sale);

        return SaleItemMapper.convertToDTO(savedItem);
    }

    @Transactional
    public void removeItem(UUID saleId, UUID itemId) {
        requireNonNull(saleId, "Sale ID parameter is null");
        requireNonNull(itemId, "Item ID parameter is null");

        SaleItem item = findItemInSaleOrThrow(saleId, itemId);
        Sale sale = item.getSale();
        assertSaleIsOpen(sale);

        saleItemRepo.delete(item);
        saleItemRepo.flush();
        recalculateSaleTotal(sale);
    }

    private BigDecimal calculateLineTotal(BigDecimal price, BigDecimal quantity, BigDecimal discount) {
        BigDecimal total = price.multiply(quantity)
                                .subtract(discount);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount cannot exceed the item amount (price x quantity)");
        }
        return total;
    }

    private void recalculateSaleTotal(Sale sale) {
        BigDecimal newTotal = saleItemRepo.getSaleItemsBySaleId(sale.getId())
                                          .stream()
                                          .map((SaleItem item) ->
                                                  item.getPrice()
                                                      .multiply(item.getQuantity())
                                                      .subtract(item.getDiscount())
                                          )
                                          .reduce(BigDecimal.ZERO, BigDecimal::add);

        sale.setTotal(newTotal);
        saleRepo.saveAndFlush(sale);
    }

    private void assertSaleIsOpen(Sale sale) {
        if (!ESaleState.OPEN.equals(sale.getSaleState())) {
            throw new IllegalStateException("Cannot modify the items of a sale that is not open");
        }
    }

    private Sale findSaleOrThrow(UUID saleId) {
        return saleRepo.findById(saleId)
                       .orElseThrow(() -> new RuntimeException("Could not find sale with ID: %s".formatted(saleId)));
    }

    private SaleItem findItemInSaleOrThrow(UUID saleId, UUID itemId) {
        SaleItem item = saleItemRepo.findById(itemId)
                                    .orElseThrow(() -> new RuntimeException("Could not find sale item with ID: %s".formatted(itemId)));

        if (!item.getSale()
                 .getId()
                 .equals(saleId)) {
            throw new RuntimeException("Sale item %s does not belong to sale %s".formatted(itemId, saleId));
        }
        return item;
    }

    private void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
