package marketing.management.system.util;

import marketing.management.system.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {

    @Value("${app.secret}")
    public String SECRET;
    @Value("${app.name}")
    private String APP_NAME;
    @Value("${app.refreshTokenExpiresIn}")
    private int REFRESH_TOKEN_EXPIRES_IN;
    @Value("${app.accessTokenExpiresIn}")
    private int ACCESS_EXPIRES_IN;
    @Value("Authorization")
    private String AUTH_HEADER;
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    public String generateAccessToken(User user) {
        return Jwts.builder().setIssuer(APP_NAME).setSubject(user.getUsername()).setIssuedAt(new Date()).setExpiration(generateTokenExpirationDate(ACCESS_EXPIRES_IN)).claim("role", user.getRoles().get(0).getName()).claim("name", user.getName()).claim("surname", user.getSurname()).claim("id", user.getId())

                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder().setIssuer(APP_NAME).setSubject(user.getUsername()).setIssuedAt(new Date()).setExpiration(generateTokenExpirationDate(REFRESH_TOKEN_EXPIRES_IN)).signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }


    private Date generateTokenExpirationDate(int expiresIn) {
        return new Date(new Date().getTime() + expiresIn);
    }


    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }


    public String getUsernameFromToken(String token) {
        String username;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            username = null;
        }

        return username;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }


    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            expiration = null;
        }

        return expiration;
    }


    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);

        return (username != null && username.equals(((User) userDetails).getEmail()) && created != null && expiration != null && created.before(expiration));
    }


    public int getAccessTokenExpiresIn() {
        return ACCESS_EXPIRES_IN;
    }

    public int getRefreshTokenExpiresIn() {
        return REFRESH_TOKEN_EXPIRES_IN;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }
}