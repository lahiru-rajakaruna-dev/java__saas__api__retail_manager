package lahiru_rajakaruna.retail_manager.Config;

import lahiru_rajakaruna.retail_manager.Authentication.Filters.HookTraceIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalFilterConfig {
    @Bean
    public FilterRegistrationBean<HookTraceIdFilter> registerTraceIdFilter() {
        FilterRegistrationBean<HookTraceIdFilter> filterRegister = new FilterRegistrationBean<>();
        filterRegister.setFilter(new HookTraceIdFilter());
        filterRegister.addUrlPatterns("/*");
        filterRegister.setOrder(-101);
        return filterRegister;
    }
}


