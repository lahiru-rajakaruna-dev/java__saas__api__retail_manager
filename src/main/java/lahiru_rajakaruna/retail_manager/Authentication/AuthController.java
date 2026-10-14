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

@Controller
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

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@ModelAttribute LoginPayloadDTO loginPayload, HttpServletResponse response) {
        String token = authService.login(loginPayload.getPhoneNumber(), loginPayload.getPassword());
//        GET JWT
        String jwt = jwtEncoder.encode(token);
//        REDIRECT WITH AUTH COOKIE
        Cookie authCookie = new Cookie(SecurityConstants.AUTH_COOKIE_NAME.getValue(), jwt);
        authCookie.setPath("/api");
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge(12 * 24 * 60 * 60);
        response.addCookie(authCookie);
        return "redirect:/";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register(@ModelAttribute CreateTenantDTO registerPayload, HttpServletResponse response, Model model) {
        this.authService.register(registerPayload.getPhone(), registerPayload.getPassword());
        model.addAttribute("message", "Registration Successful");
        return "redirect:/auth/login#login";
    }
}
