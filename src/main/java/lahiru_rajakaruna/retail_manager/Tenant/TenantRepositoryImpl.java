package lahiru_rajakaruna.retail_manager.Tenant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ISearchable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
public class TenantRepositoryImpl implements ISearchable<Tenant, TenantSearchDTO> {
    private final EntityManager entityManager;

    public TenantRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Tenant> search(TenantSearchDTO parameters) throws Exception {
        TypedQuery<Tenant> query = buildQuery(parameters);
        return query.getResultList();
    }

    private TypedQuery<Tenant> buildQuery(TenantSearchDTO parameters) throws Exception {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> query = cb.createQuery(Tenant.class);
        Root<Tenant> table = query.from(Tenant.class);

        HashMap<Field, Object> parameterValues = new HashMap<Field, Object>();


        Field[] fields = parameters.getClass().getDeclaredFields();
        log.info(Arrays.toString(fields));


        for (Field field : fields) {
            log.info(field.getName());

            String derivedMethodName = String.format("get%s", StringUtils.capitalize(field.getName()));
            Method method;
            try {
                method = parameters.getClass().getMethod(derivedMethodName);
            } catch (NoSuchMethodException e) {
                log.warn(e.getCause().toString());
                throw new RuntimeException(e);
            }

            Object parameterValue = method.invoke(parameters);
            parameterValues.put(field, parameterValue);
        }


        parameterValues.forEach((Field field, Object value) -> {
            if (value == null) {
                return;
            }

            query.where(
                    cb.like(
                            table.get(field.getName()),
                            value.toString()
                    )
            );
        });

        return entityManager.createQuery(query);
    }

}
