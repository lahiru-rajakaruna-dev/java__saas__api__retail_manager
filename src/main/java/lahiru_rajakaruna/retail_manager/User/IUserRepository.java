package lahiru_rajakaruna.retail_manager.User;

import lahiru_rajakaruna.retail_manager.AbstractBaseClasses.ISearchable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository
        extends JpaRepository<User, UUID>, ISearchable<User, UserSearchDTO> {

}
