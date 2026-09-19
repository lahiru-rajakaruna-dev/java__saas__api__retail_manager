package lahiru_rajakaruna.retail_manager.TenantTests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lahiru_rajakaruna.retail_manager.Tenant.ITenantRepository;
import lahiru_rajakaruna.retail_manager.Tenant.Tenant;
import lahiru_rajakaruna.retail_manager.Tenant.TenantDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TenantRepoTests {
    @Autowired
    public ITenantRepository userRepo;
    @Autowired
    public TestEntityManager entityManager;

    private Tenant sampleTenant;

    public TenantRepoTests() {
    }

    @AfterEach
    public void logDBRecords() {
        EntityManager em = entityManager.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tenant> query = cb.createQuery(Tenant.class);
        Root<Tenant> user_ = query.from(Tenant.class);
        em.createQuery(query).getResultList().forEach((Tenant u) -> {
            System.out.println(TenantDTO.convertToDTO(u).toString());
        });
    }

    @BeforeEach
    public void createSampleUserRecord() {
        sampleTenant = new Tenant();
        sampleTenant.setName("Tester");
        sampleTenant.setPhone("000-000-000");
        sampleTenant.setPasswordHash("encoded-password");
    }


    @Test
    public void shouldSaveTheEntityInTheDataBase() {
        Tenant savedTenant = userRepo.save(sampleTenant);

        entityManager.flush();
        entityManager.clear();

        Tenant returnedTenant = entityManager.find(Tenant.class, savedTenant.getId());

        Assertions.assertNotNull(returnedTenant, "User Not Saved In The DB");
    }

    @Test
    public void shouldFetchTheUserById() {
        Tenant savedTenant = entityManager.persistAndFlush(sampleTenant);
        boolean isUserFoundOnTheDB = userRepo.findById(savedTenant.getId()).isPresent();
        Assertions.assertTrue(isUserFoundOnTheDB, "Repo Did Not Fetch The User From DB");
    }

    @Test
    public void shouldDeleteTheUserById() {
        Tenant savedTenant = entityManager.persistAndFlush(sampleTenant);

        userRepo.deleteById(savedTenant.getId());
        entityManager.flush();
        entityManager.clear();

        Tenant deletedTenant = entityManager.find(Tenant.class, savedTenant.getId());
        Assertions.assertNull(deletedTenant, "Repo Failed To Delete The User From DB");
    }

    @Test
    public void shouldUpdateTheUserById() {
        Tenant savedTenant = entityManager.persistAndFlush(sampleTenant);
        entityManager.clear();

        savedTenant.setName("Test User");
        userRepo.save(savedTenant);
        entityManager.flush();
        entityManager.clear();

        Tenant tenant = entityManager.find(Tenant.class, savedTenant.getId());

        if (tenant == null || tenant.getName().isEmpty()) {
            throw new Error("Repo Failed To Save The User Entity Correctly");
        }

        Assertions.assertEquals("Test User", tenant.getName());
    }
}
