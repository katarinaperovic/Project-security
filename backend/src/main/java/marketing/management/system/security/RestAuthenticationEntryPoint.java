package marketing.management.system.security;

import jakarta.servlet.http.HttpSession;
import marketing.management.system.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final static Logger logger = LogManager.getLogger(RestAuthenticationEntryPoint.class);
    private final TokenUtils tokenUtils;

    public RestAuthenticationEntryPoint(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("LOGGED_ONCE") == null) {
            String ipAddress = request.getRemoteAddr();
            String host = request.getRemoteHost();
            int port = request.getRemotePort();
            String email = request.getHeader("X-User-Email");

            logger.warn(
                    "User with email: ".concat(email)
                            .concat(" failed to log in from IP: ").concat(ipAddress)
                            .concat(" HOST: ").concat(host)
                            .concat(" PORT: ").concat(String.valueOf(port))
            );

            session.setAttribute("LOGGED_ONCE", true);
        }


        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}