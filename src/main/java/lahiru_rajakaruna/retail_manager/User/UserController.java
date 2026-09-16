package lahiru_rajakaruna.retail_manager.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        List<User> users = this.userService.getAllUsers();
        return users;
    }

    @GetMapping("/{id}")
    public User getUserDetails(@PathVariable UUID id) throws Exception {
        User user = this.userService.getUserById(id);
        return user;
    }


    @PatchMapping("/{id}")
    public User updateUser(@RequestBody User userUpdates, @PathVariable UUID id, @RequestParam(name = "field", required = true) String field) throws Exception {
    }


}
