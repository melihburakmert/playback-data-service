package mbm.playback_data_service.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

    private static final String LOGIN_URI_PREFIX = "/playback/auth/login";
    private static final String CALLBACK_URI_PREFIX = "/playback/auth/callback";
    private static final String ACCESS_TOKEN_ATTRIBUTE = "accessToken";
    private static final String LOGIN_REDIRECT_PARAM = "/playback/auth/login?redirect=";

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler) throws Exception {

        if (isAuthRequest(request.getRequestURI())) {
            return true;
        }

        if (needsLogin(request)) {
            log.info("Access token not found. Redirecting to login");
            response.sendRedirect(LOGIN_REDIRECT_PARAM + request.getRequestURI());
            return false;
        }

        return true;
    }

    private boolean needsLogin(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        return session == null || session.getAttribute(ACCESS_TOKEN_ATTRIBUTE) == null;
    }

    private boolean isAuthRequest(final String requestUri) {
        return requestUri.startsWith(LOGIN_URI_PREFIX) || requestUri.startsWith(CALLBACK_URI_PREFIX);
    }
}
