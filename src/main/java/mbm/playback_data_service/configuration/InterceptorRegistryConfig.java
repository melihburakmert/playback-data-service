package mbm.playback_data_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorRegistryConfig implements WebMvcConfigurer {

    // TODO: Reuse these constants. They are duplicated in SessionAuthInterceptor.
    private static final String API_PATH_PATTERN = "/playback/**";
    private static final String LOGIN_PATH_PATTERN = "/playback/auth/login";
    private static final String CALLBACK_PATH_PATTERN = "/playback/auth/callback";

    private final SessionAuthInterceptor sessionAuthInterceptor;

    public InterceptorRegistryConfig(final SessionAuthInterceptor sessionAuthInterceptor) {
        this.sessionAuthInterceptor = sessionAuthInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(sessionAuthInterceptor)
                .addPathPatterns(API_PATH_PATTERN)
                .excludePathPatterns(LOGIN_PATH_PATTERN, CALLBACK_PATH_PATTERN);
    }
}
