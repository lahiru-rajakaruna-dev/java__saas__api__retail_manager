package lahiru_rajakaruna.retail_manager.Authentication.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lahiru_rajakaruna.retail_manager.Authentication.SecurityConstants;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;


public class HookTraceIdFilter extends OncePerRequestFilter {
    private String LOG_HEADER = "[>>>] INCOMING REQUEST: %s";
    private String LOG_FOOTER = "[<<<] RESPONSE LEFT THE SERVER: %s\n";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        UUID traceId = UUID.randomUUID();
        request.setAttribute(SecurityConstants.TRACE_ID, traceId);

        System.out.println(LOG_HEADER.formatted(traceId));
        filterChain.doFilter(request, response);
        System.out.println(LOG_FOOTER.formatted(traceId));
    }
}
