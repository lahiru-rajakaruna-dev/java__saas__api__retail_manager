package lahiru_rajakaruna.retail_manager.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable UUID id) throws RuntimeException {
        UserDTO user = this.userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> patchUser(@RequestBody UserDTO updates, @PathVariable UUID id) throws RuntimeException {
        UserDTO updatedUser = this.userService.patchUser(id, updates);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> putUser(@RequestBody UserDTO updates, @PathVariable UUID id) {
        UserDTO replacedUser = this.userService.putUser(id, updates);
        return ResponseEntity.ok(replacedUser);
    }

}
