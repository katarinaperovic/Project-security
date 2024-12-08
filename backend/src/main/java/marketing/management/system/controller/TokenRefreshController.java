package marketing.management.system.controller;

import marketing.management.system.dto.UserTokenState;
import marketing.management.system.model.User;
import marketing.management.system.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;


@RestController
public class TokenRefreshController {
    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;
    @Autowired
    public TokenRefreshController(TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        this.tokenUtils = tokenUtils;

        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<UserTokenState> refreshAccessToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        if (refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String refreshTokenValue = refreshTokenHeader.substring(7);


        // provera da li je refresh token istekao
        Date refreshTokenExpirationDate = tokenUtils.getExpirationDateFromToken(refreshTokenValue);
        Date now = new Date();

        // refresh je istekao
        if (refreshTokenExpirationDate == null || refreshTokenExpirationDate.before(now)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // uzimanje user details iz tokena i provera da li je blokiran
        UserDetails userDetails = getUserDetailsFromToken(refreshTokenValue);
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = (User) userDetails;
        if (!user.isApproved() || !user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        //generisanje novog acces
        String accessToken = tokenUtils.generateAccessToken((User) userDetails);
        int accessTokenExpiresIn = tokenUtils.getAccessTokenExpiresIn();


        UserTokenState userTokenState = new UserTokenState();
        userTokenState.setAccessToken(accessToken);
        userTokenState.setAccessTokenexpiresIn(accessTokenExpiresIn);

        // ako nije istekao vracam isti
        userTokenState.setRefreshToken(refreshTokenValue);
        long expiresIn = (refreshTokenExpirationDate.getTime() - now.getTime());
        userTokenState.setRefreshTokenexpiresIn((int) expiresIn);

        return ResponseEntity.ok(userTokenState);
    }



    private UserDetails getUserDetailsFromToken(String refreshToken) {
        String username = tokenUtils.getUsernameFromToken(refreshToken);
        return userDetailsService.loadUserByUsername(username);
    }
}
