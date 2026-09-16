package lahiru_rajakaruna.retail_manager.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = this.userService.getAllUsers();
        return users;
    }

    @GetMapping("/{id}")
    public UserDTO getUserDetails(@PathVariable UUID id) throws RuntimeException {
        UserDTO user = this.userService.getUserById(id);
        return user;
    }


    @PatchMapping("/{id}")
    public UserDTO patchUser(@RequestBody UserDTO updates, @PathVariable UUID id) throws RuntimeException {
        UserDTO updatedUser = this.userService.patchUser(id, updates);
        return updatedUser;
    }

    @PutMapping("/{id}")
    public UserDTO putUser(@RequestBody UserDTO updates, @PathVariable UUID id) {
        UserDTO replacedUser = this.userService.putUser(id, updates);
        return replacedUser;
    }

}
