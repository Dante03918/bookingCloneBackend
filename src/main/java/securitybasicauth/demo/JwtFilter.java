package securitybasicauth.demo;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import securitybasicauth.demo.utils.JwtTokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

   private final UserDetailsServiceImpl userDetailsService;
   private final JwtTokenUtils tokenUtil;

  @Autowired
  public JwtFilter(JwtTokenUtils tokenUtil, UserDetailsServiceImpl userDetailsService){
      this.tokenUtil = tokenUtil;
      this.userDetailsService = userDetailsService;
  }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String tokenWithBearerPrefix = request.getHeader("Authorization");

        String cleanToken = null;
        String username = null;
        if (tokenWithBearerPrefix != null && tokenWithBearerPrefix.startsWith("Bearer")) {
            cleanToken = tokenWithBearerPrefix.substring(7);

            try {
                username = tokenUtil.extractUsernameFromToken(cleanToken);
            } catch (ExpiredJwtException e) {
                System.out.println("Expired");
            }
        }

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

             tokenUtil.validateToken(cleanToken, userDetails.getUsername());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        chain.doFilter(request, response);
    }
}
