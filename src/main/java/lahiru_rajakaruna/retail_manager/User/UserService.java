package lahiru_rajakaruna.retail_manager.User;

import lahiru_rajakaruna.retail_manager.Shop.Shop;
import lahiru_rajakaruna.retail_manager.Shop.ShopRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final IUserRepository userRepo;
    private final ShopRepository shopRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepo, PasswordEncoder passwordEncoder, ShopRepository shopRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.shopRepo = shopRepo;
    }

    public List<UserDTO> getAllUsers() {
        return this.userRepo.findAll().stream().map(UserDTO::convertToDTO).toList();
    }

    public UserDTO getUserById(UUID id) throws Exception {
        User user = this.userRepo.findById(id).orElseThrow(() -> new Exception("User not found"));
        return UserDTO.convertToDTO(user);
    }

    public UserDTO patchUser(UUID id, UserDTO updates) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (updates.getName().isPresent()) {
            user.setName(updates.getName().get());
        }

        if (updates.getPhone().isPresent()) {
            user.setPhone(updates.getPhone().get());
        }

        if (updates.getPassword().isPresent()) {
            String hash = this.passwordEncoder.encode(updates.getPassword().get());
            user.setPasswordHash(hash);
        }

        if (updates.getShopId().isPresent()) {
            Shop shop = this.shopRepo.findById(updates.getShopId().get()).orElseThrow(() -> new RuntimeException("Could not find the shop"));
            user.setShop(shop);
        }

        return UserDTO.convertToDTO(this.userRepo.save(user));
    }
}
