package marketing.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserTokenState {

    private String accessToken;
    private String refreshToken;
    private int accessTokenexpiresIn;
    private int refreshTokenexpiresIn;
}
