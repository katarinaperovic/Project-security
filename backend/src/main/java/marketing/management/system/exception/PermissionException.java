package marketing.management.system.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@NoArgsConstructor
public class PermissionException extends RuntimeException{

    @Getter
    @Setter
    public String permissionName;

    @Getter
    @Setter
    public String user;

    public PermissionException(String message) {
        super(message);
    }
}
