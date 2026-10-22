package lahiru_rajakaruna.retail_manager.Authentication.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lahiru_rajakaruna.retail_manager.Authentication.DTOs.AuthSession;
import lahiru_rajakaruna.retail_manager.Authentication.*;
import lahiru_rajakaruna.retail_manager.Common.SessionManager;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;


public class UserAuthorizationFilter extends OncePerRequestFilter {
    private final Set<String> protectedOperations = Set.of("POST", "PATCH", "PUT", "DELETE");
    private final SessionManager sessionManager;

    public UserAuthorizationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestMethod = request.getMethod();
        boolean isNonProtectedOperation = !protectedOperations.contains(requestMethod);
        if (isNonProtectedOperation) {
            filterChain.doFilter(request, response);
            return;
        }

        String sessionToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SecurityConstants.SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    sessionToken = cookie.getValue();
                    break;
                }
            }
        }

        if (cookies == null || sessionToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        AuthSession userSession = sessionManager.get(sessionToken);
        if (userSession == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String csrfToken = request.getHeader(SecurityConstants.CSRF_HEADER);
        if (csrfToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        boolean validCSRFToken = userSession.getCsrfToken()
                                            .equals(csrfToken);
        if (!validCSRFToken) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserRole userRole = userSession.getRole();
        if (UserRole.GUEST.equals(userRole)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = (userSession.getUserId() != null) ? userSession.getUserId()
                                                                       .toString() : null;

        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }

        List<GrantedAuthority> grantedAuthorities = PrincipalRoleGrantedAuthorities.get(userSession.getRole());
        UserCredentials credentials = new UserCredentials(userSession.getPhone(), userSession.getCsrfToken());
        UserAuthenticationToken userAuthToken = new UserAuthenticationToken(userId, credentials, grantedAuthorities);
        SecurityContextHolder.getContext()
                             .setAuthentication(userAuthToken);
        filterChain.doFilter(request, response);
    }
}
