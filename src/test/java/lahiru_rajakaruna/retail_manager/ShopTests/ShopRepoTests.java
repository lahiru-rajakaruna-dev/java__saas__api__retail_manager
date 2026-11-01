package lahiru_rajakaruna.retail_manager.ShopTests;

import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ShopRepoTests {
    @Autowired
    private IShopRepository shopRepo;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ITenantRepository tenantRepo;


    public ShopRepoTests() {
    }


    @Test
    public void shouldSaveEntityOnThePersistence() {
        Shop shopToSave = new Shop("Test Shop", EActiveState.ACTIVE);
        Shop savedShopEntity = shopRepo.saveAndFlush(shopToSave);
        Shop retrievedEntity = entityManager.find(Shop.class, savedShopEntity.getId());

        Assertions.assertThat(retrievedEntity)
                  .isNotNull();

        Assertions.assertThat(retrievedEntity.getName())
                  .isEqualTo(shopToSave.getName());
    }

    @Test
    public void shouldRetrieveTheMatchingEntity() {
        Shop shopToSave = new Shop("Test Shop", EActiveState.ACTIVE);
        Shop savedShop = entityManager.persistAndFlush(shopToSave);

        Shop retrievedShop = shopRepo.findById(savedShop.getId())
                                     .orElse(null);

        Assertions.assertThat(retrievedShop)
                  .isNotNull();
        Assertions.assertThat(retrievedShop.getId())
                  .isEqualTo(savedShop.getId());
    }

    @Test
    public void shouldUpdateTheMatchingEntity() {
        Shop shopToSave = new Shop("Test Shop", EActiveState.ACTIVE);
        String updatedName = "Updated Shop Name";
        Shop savedShop = entityManager.persistAndFlush(shopToSave);

        Shop retrievedShop = shopRepo.findById(savedShop.getId())
                                     .orElse(null);

        Assertions.assertThat(retrievedShop)
                  .isNotNull();

        retrievedShop.setName(updatedName);
        shopRepo.saveAndFlush(retrievedShop);

        Shop retrievedUpdatedShop = entityManager.find(Shop.class, savedShop.getId());

        Assertions.assertThat(retrievedUpdatedShop)
                  .isNotNull();
        Assertions.assertThat(retrievedUpdatedShop.getName())
                  .isEqualTo(updatedName);
    }

    @Test
    public void shouldDeleteTHeMatchingEntity() {
        Shop shopToSave = new Shop("Test Shop", EActiveState.ACTIVE);
        Shop savedShop = entityManager.persistAndFlush(shopToSave);
        shopRepo.delete(savedShop);

        Shop deletedShop = entityManager.find(Shop.class, savedShop.getId());
        Assertions.assertThat(deletedShop)
                  .isNull();
    }
}
