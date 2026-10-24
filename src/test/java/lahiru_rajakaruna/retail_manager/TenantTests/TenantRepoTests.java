package lahiru_rajakaruna.retail_manager.TenantTests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lahiru_rajakaruna.retail_manager.Common.EActiveState;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lahiru_rajakaruna.retail_manager.Tenant.TenantMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


@DataJpaTest
@AutoConfigureTestDatabase
public class TenantRepoTests {

    public final String dummyPhone = "+9400 12 34 5678";
    public final String dummyPasswordHash = "dummy-password-hash";
    public final EActiveState tenantState = EActiveState.ACTIVE;
    public Tenant dummyTenant = new Tenant(null, dummyPasswordHash, dummyPhone, tenantState);

    @Autowired
    public ITenantRepository userRepo;
    @Autowired
    public TestEntityManager entityManager;

    public TenantRepoTests() {
    }

    @AfterEach
    public void logDBRecords() {
        EntityManager em = entityManager.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tenant> query = cb.createQuery(Tenant.class);
        Root<Tenant> user_ = query.from(Tenant.class);

        em.createQuery(query)
          .getResultList()
          .forEach((Tenant u) -> {
              System.out.println("AFTER TEST: %s".formatted(TenantMapper.convertToResponseDTO(u)
                                                                        .toString()));
          });
    }


    @Test
    public void shouldSaveTheEntityInTheDataBase() {
        Tenant savedTenant = userRepo.save(dummyTenant);
        entityManager.flush();
        entityManager.clear();
        Tenant returnedTenant = entityManager.find(Tenant.class, savedTenant.getId());
        Assertions.assertNotNull(returnedTenant, "User Not Saved In The DB");
    }

    @Test
    public void shouldFetchTheUserById() {
        Tenant savedTenant = entityManager.persistAndFlush(dummyTenant);
        boolean isUserFoundOnTheDB = userRepo.findById(savedTenant.getId())
                                             .isPresent();
        Assertions.assertTrue(isUserFoundOnTheDB, "Repo Did Not Fetch The User From DB");
    }

    @Test
    public void shouldDeleteTheUserById() {
        Tenant savedTenant = entityManager.persistAndFlush(dummyTenant);
        userRepo.deleteById(savedTenant.getId());
        entityManager.flush();
        entityManager.clear();
        Tenant deletedTenant = entityManager.find(Tenant.class, savedTenant.getId());
        Assertions.assertNull(deletedTenant, "Repo Failed To Delete The User From DB");
    }

    @Test
    public void shouldUpdateTheUserById() {
        Tenant savedTenant = entityManager.persistAndFlush(dummyTenant);
        entityManager.flush();
        entityManager.clear();
        savedTenant.setState(EActiveState.INACTIVE);
        userRepo.save(savedTenant);
        entityManager.flush();
        entityManager.clear();
        Tenant tenant = entityManager.find(Tenant.class, savedTenant.getId());
        Assertions.assertNotNull(tenant);
        Assertions.assertEquals(EActiveState.INACTIVE, tenant.getState());
    }
}
