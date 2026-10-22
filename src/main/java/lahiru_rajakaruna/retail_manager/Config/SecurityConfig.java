package lahiru_rajakaruna.retail_manager.Config;

import lahiru_rajakaruna.retail_manager.Authentication.CustomAccessDeniedHandler;
import lahiru_rajakaruna.retail_manager.Authentication.CustomAuthenticationEntryPoint;
import lahiru_rajakaruna.retail_manager.Authentication.Filters.UserAuthorizationFilter;
import lahiru_rajakaruna.retail_manager.Authentication.RedisSessionManager;
import lahiru_rajakaruna.retail_manager.Common.SecureTokenGenerator;
import lahiru_rajakaruna.retail_manager.DefaultSecureTokenGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final UserAuthorizationFilter userAuthorizationFilter;

    public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint, RedisSessionManager redisSessionManager) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userAuthorizationFilter = new UserAuthorizationFilter(redisSessionManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSec) {
        httpSec
                .authorizeHttpRequests((authReq) -> authReq.requestMatchers("/error", "/auth/**", "/public/**")
                                                           .permitAll()
                                                           .anyRequest()
                                                           .authenticated())
                .csrf((csrf) -> csrf.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())
                .formLogin((formLogin) -> formLogin.disable())
                .addFilterAt(userAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        (sessionManager) -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        (exception) -> exception.authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler));

        return httpSec.build();
    }

    @Bean
    public SecureTokenGenerator secureTokenGenerator() {
        return new DefaultSecureTokenGenerator();
    }

}
