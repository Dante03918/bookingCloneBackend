package securitybasicauth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import securitybasicauth.demo.repositories.RegisterUserRepo;


@RestController
public class AuthenticationController {

    @Autowired
    RegisterUserRepo registerUserRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginUserModel loginUserModel) throws Exception {

        authenticate(loginUserModel.getEmail(), loginUserModel.getPassword());

       String token =  jwtTokenUtil.generateToken(loginUserModel.getEmail());

        return ResponseEntity.ok(token);
    }

    private void authenticate(String email, String password) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                    password));
        } catch(BadCredentialsException badCredentialsException){
            throw new Exception("Bad Credentials", badCredentialsException);
        }
    }


}
