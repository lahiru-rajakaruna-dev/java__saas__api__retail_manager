package lahiru_rajakaruna.retail_manager.Authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.LoginPayloadDTO;
import lahiru_rajakaruna.retail_manager.Common.JSONEncoderDecoder;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateTenantDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authService;
    private final JSONEncoderDecoder jwtEncoder;

    public AuthController(AuthenticationService authService, JSONEncoderDecoder jwtEncoder) {
        this.authService = authService;
        this.jwtEncoder = jwtEncoder;
    }

    @GetMapping("/login")
    public String sendLoginPage(Model model) {
        model.addAttribute("Title", "Login");
        return "authentication";
    }

    @GetMapping("/register")
    public String sendRegisterPage(Model model) {
        model.addAttribute("Title", "Register");
        return "authentication";
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginSuccessPayload> login(@ModelAttribute LoginPayloadDTO loginPayload) {
        LoginResult loginResult = authService.login(loginPayload.getPhoneNumber(), loginPayload.getPassword());

        ResponseCookie sessionCookie = ResponseCookie.from(SecurityConstants.SESSION_COOKIE_NAME, loginResult.sessionToken())
                                                     .path("/")
                                                     .secure(true)
                                                     .httpOnly(true)
                                                     .maxAge(SecurityConstants.DEFAULT_SESSION_TTL)
                                                     .sameSite("Lax")
                                                     .build();
        return ResponseEntity.status(HttpStatus.OK)
                             .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                             .body(new LoginSuccessPayload("Login Successful", loginResult.csrfToken()));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody() CreateTenantDTO registerPayload) {
        this.authService.register(registerPayload.getPhoneNumber(), registerPayload.getPassword());
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponsePayload("Registration Successful"));
    }
}
