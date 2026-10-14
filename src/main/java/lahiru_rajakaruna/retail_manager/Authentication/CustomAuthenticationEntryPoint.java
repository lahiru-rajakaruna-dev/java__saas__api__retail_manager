package lahiru_rajakaruna.retail_manager.Authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
    throws IOException, ServletException {
        System.out.println("UNAUTHORIZED_REQUEST: %s".formatted(request.getAttribute(SecurityConstants.TRACE_ID.getValue())));
        response.sendRedirect("/auth/login");
    }
}
