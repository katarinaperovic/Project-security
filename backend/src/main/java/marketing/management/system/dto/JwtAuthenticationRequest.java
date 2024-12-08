package marketing.management.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationRequest {

    @NotEmpty
    @Email(message = "Email should be valid")
    private String username;
    @NotEmpty
    @Size(min = 8, max = 25, message = "Password must be minimum 8 characters")
    @Pattern.List({
            @Pattern(regexp = "^(?=.*\\d).*$", message = "Password must contain digit!"),
            @Pattern(regexp = "^(?=.*[!@#$%^&*()_+=]).*$", message = "Password must contain special character!"),
            @Pattern(regexp = "^(?=.*[A-Z]).*$", message = "Password must contain uppercase letter!"),
            @Pattern(regexp = "^(?=.*[a-z]).*$", message = "Password must contain lowercase letter!")

    })
    private String password;

    private String recaptchaToken;

}
