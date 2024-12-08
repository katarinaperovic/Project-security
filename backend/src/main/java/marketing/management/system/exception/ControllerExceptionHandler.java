package marketing.management.system.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final static Logger logger = LogManager.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<Object> handleUniqueErrors(PermissionException ex, HttpServletRequest request) {

        String ipAddress = request.getRemoteAddr();
        String host = request.getRemoteHost();
        int port = request.getRemotePort();
        logger.warn(
                "User ".concat(ex.getUser())
                        .concat(" from IP: ").concat(ipAddress)
                        .concat(" HOST: ").concat(host)
                        .concat(" PORT: ").concat(String.valueOf(port))
                        .concat(" Failed to access resources with permission: ").concat(ex.getPermissionName()));

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

}
