package securitybasicauth.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;

import static securitybasicauth.demo.SecurityConst.SECRET;

@Component
public class JwtTokenUtils {

    public String generateToken(String subject) {

        HashMap<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 900_000))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public Claims getClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    public String extractUsernameFromToken(String token){
         return getClaimsFromToken(token).getSubject();
    }
    public Date getExpirationDateFromToken(String token){
        return getClaimsFromToken(token).getExpiration();
    }

    private Boolean isExpired(String token){
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        String usernameFromToken = extractUsernameFromToken(token);

        return usernameFromToken.equals(userDetails.getUsername()) && !isExpired(token);
    }
    public Boolean validateToken(String token, String username){
        String usernameFromToken = extractUsernameFromToken(token);

        return usernameFromToken.equals(username) && !isExpired(token);
    }

}
