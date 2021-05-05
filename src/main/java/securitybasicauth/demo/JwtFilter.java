package securitybasicauth.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static io.jsonwebtoken.Jwts.parser;
public class JwtFilter extends BasicAuthenticationFilter {

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader("Authorization");

        UsernamePasswordAuthenticationToken authResult = getAuthenticationByToken(header);


        SecurityContextHolder.getContext().setAuthentication(authResult);

        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthenticationByToken(String header) {
      Jws<Claims> claims = Jwts.parser().setSigningKey("aaa".getBytes()).parseClaimsJws(header);

      String username = claims.getBody().get("name").toString();
      String role = claims.getBody().get("role").toString();

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);

        return usernamePasswordAuthenticationToken;
    }


    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return super.getAuthenticationManager();
    }
}
