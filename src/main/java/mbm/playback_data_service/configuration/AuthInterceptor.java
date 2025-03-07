package mbm.playback_data_service.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTH_HEADER = "Authorization";
    private static final String SESSION_ID_HEADER = "X-Session-Id";
    private static final String EXCLUDED_PATH = "/playback/auth/token";
    private static final String AUTHORIZATION_ERROR = "Missing Authorization header";

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        final String requestPath = request.getRequestURI();
        final String authHeader = request.getHeader(AUTH_HEADER);
        final String sessionId = request.getHeader(SESSION_ID_HEADER);

        if (requestPath.equals(EXCLUDED_PATH) && !isNullOrEmpty(sessionId)) {
            return true;
        }

        if (isNullOrEmpty(authHeader)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, AUTHORIZATION_ERROR);
            return false;
        }

        return true;
    }

    private boolean isNullOrEmpty(final String value) {
        return value == null || value.isBlank();
    }
}
