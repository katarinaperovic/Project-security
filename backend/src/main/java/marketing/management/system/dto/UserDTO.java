package marketing.management.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import marketing.management.system.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import marketing.management.system.model.PackageType;

@Data
public class UserDTO {
    @Pattern(regexp = "^[a-zA-Z\\s]+$",message="Name must contains only letters")
    @Size(max = 25)
    private String name;
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Surname must contains only letters")
    @Size(max = 25)
    private String surname;
    @Pattern(regexp = "^[a-zA-Z0-9\\s!@#$%^&*(),.?\":/'{}|<>=\\[\\]\\-;]+$")
    private String firmName;
    @Pattern(regexp = "^[0-9]{8,13}$", message = "Pib must contains only digits, exactly 10 digits")
    private String pib;
    @Pattern(regexp = "^[0-9]{8,10}$", message = "Phone number must contains only digits, exactly 8-10 digits")
    private String phone;
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 8, max = 25, message = "Password must be minimum 8 characters")
    @Pattern.List({
            @Pattern(regexp = "^(?=.*\\d).*$", message = "Password must contain digit!"),
            @Pattern(regexp = "^(?=.*[!@#$%^&*()_+=]).*$", message = "Password must contain special character!"),
            @Pattern(regexp = "^(?=.*[A-Z]).*$", message = "Password must contain uppercase letter!"),
            @Pattern(regexp = "^(?=.*[a-z]).*$", message = "Password must contain lowercase letter!")


    })
    private String password;
    private String re_password;
    @JsonProperty
    private Address address;
    private Long Id;
    private PackageType packageType;
}
