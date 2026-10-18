package lahiru_rajakaruna.retail_manager.Authentication.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.AuthSession;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.JWTAuthenticationToken;
import lahiru_rajakaruna.retail_manager.Common.JSONEncoderDecoder;
import lahiru_rajakaruna.retail_manager.Common.SessionManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final String AUTH_COOKIE = "access_token";
    private final JSONEncoderDecoder jsonEncoderDecoder;
    private final SessionManager sessionManager;

    public JWTAuthenticationFilter(JSONEncoderDecoder jsonEncoderDecoder, SessionManager sessionManager) {
        this.jsonEncoderDecoder = jsonEncoderDecoder;
        this.sessionManager = sessionManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();

        if (requestPath.matches("(/auth/.*)|(/public/.*)|(/h2-console)")) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        Cookie authCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("%s: %s".formatted(cookie.getName(), cookie.getValue()));
                if (cookie.getName()
                          .equals(AUTH_COOKIE)) {
                    authCookie = cookie;
                }
            }
        }

        if (authCookie != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authCookie.getValue();
            String sessionToken = jsonEncoderDecoder.decode(token);
            AuthSession session = sessionManager.get(sessionToken);
            if (session == null) {
                throw new IllegalStateException("Invalid session");
            }

            HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + session.getRole()
                                                                               .toString()));

            JWTAuthenticationToken jwtAuthToken = new JWTAuthenticationToken(grantedAuthorities, sessionToken);
            SecurityContextHolder.getContext()
                                 .setAuthentication(jwtAuthToken);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}