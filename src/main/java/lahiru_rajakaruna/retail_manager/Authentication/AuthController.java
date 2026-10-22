package lahiru_rajakaruna.retail_manager.Authentication;

import lahiru_rajakaruna.retail_manager.Authentication.DTOs.*;
import lahiru_rajakaruna.retail_manager.Common.ResponsePayload;
import lahiru_rajakaruna.retail_manager.Common.SecureTokenGenerator;
import lahiru_rajakaruna.retail_manager.Common.SessionManager;
import lahiru_rajakaruna.retail_manager.Tenant.DTOs.CreateTenantDTO;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authService;
    private final SessionManager sessionManager;
    private final SecureTokenGenerator secureTokenGenerator;

    public AuthController(AuthenticationService authService, RedisSessionManager redisSessionManager, SecureTokenGenerator secureTokenGenerator) {
        this.authService = authService;
        this.sessionManager = redisSessionManager;
        this.secureTokenGenerator = secureTokenGenerator;
    }

    @GetMapping("/can-i")
    public ResponseEntity<Object> canIAccess(@CookieValue(value = SecurityConstants.SESSION_COOKIE_NAME, required = false) String sessionId) {
//        HAS SESSION
        if (sessionId != null) {
            AuthSession session = sessionManager.get(sessionId);
//            EXPIRED SESSION
            if (session == null) {
                String newGuestSessionToken = secureTokenGenerator.getToken(32);
                String rawCSRFToken = secureTokenGenerator.getToken(32);
                AuthSession guestAuthSession = new AuthSession(null, null, null, null, rawCSRFToken);
                sessionManager.set(newGuestSessionToken, guestAuthSession);
                ResponseCookie newGuestCookieForExpiredSession = ResponseCookie.from("__Host-sid", newGuestSessionToken)
                                                                               .httpOnly(true)
                                                                               .path("/")
                                                                               .maxAge(10 * 60)
                                                                               .secure(true)
                                                                               .sameSite("Lax")
                                                                               .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .header(HttpHeaders.SET_COOKIE, newGuestCookieForExpiredSession.toString())
                                     .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                     .body(new CanIResponseDTO(null, null, "Not Authenticated"));
            }
            return ResponseEntity.status(HttpStatus.OK)
                                 .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                 .body(new CanIResponseDTO(null, session.getRawCsrf(), "OK"));
        }
//        NO SESSION => GUEST SESSION FOR AUTHENTICATION WITH CSRF
        String rawCsrfToken = UUID.randomUUID()
                                  .toString();
        String sessionToken = UUID.randomUUID()
                                  .toString();

        AuthSession guestSession = new AuthSession(null, null, null, UserRole.GUEST, rawCsrfToken, _csrfToken);
        sessionManager.set(sessionToken, guestSession);

        ResponseCookie guestSessionIdCookie = ResponseCookie.from("__Host-sid", sessionToken)
                                                            .httpOnly(true)
                                                            .secure(true)
                                                            .path("/")
                                                            .sameSite("Lax")
                                                            .maxAge(10 * 60)
                                                            .build();


        return ResponseEntity.ok()
                             .header(HttpHeaders.SET_COOKIE, guestSessionIdCookie.toString())
                             .header(HttpHeaders.CACHE_CONTROL, "no-store")
                             .body(new CanIResponseDTO(ResponseErrorType.Unauthenticated, rawCsrfToken, "Not Authenticated"));
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginSuccessPayload> login(@CookieValue(SecurityConstants.SESSION_COOKIE_NAME) String guestSessionToken, @RequestBody LoginPayloadDTO loginPayload) {

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
