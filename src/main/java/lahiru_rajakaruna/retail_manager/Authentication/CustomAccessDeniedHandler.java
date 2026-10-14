package lahiru_rajakaruna.retail_manager.Authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException)
    throws IOException, ServletException {
        Map<String, Object> data = new HashMap<>();
        data.put("message", accessDeniedException.getMessage());
        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String jsonString = new ObjectMapper().writeValueAsString(data);
        response.getWriter()
                .write(jsonString);
    }
}
