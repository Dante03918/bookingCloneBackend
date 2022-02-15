package securitybasicauth.demo;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import securitybasicauth.demo.models.LoginUserModel;

@Component
public class Authentication {

    private final AuthenticationManager authenticationManager;

    public Authentication(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    public boolean authenticate(LoginUserModel loginUserModel) throws BadCredentialsException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserModel.getEmail(), loginUserModel.getPassword()));
            return true;
        } catch (BadCredentialsException badCredentialsException) {
            throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Incorrect Credentials");
        }
    }
}
