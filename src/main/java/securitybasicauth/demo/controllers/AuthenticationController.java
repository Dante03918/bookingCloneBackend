package securitybasicauth.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securitybasicauth.demo.models.LoginUserModel;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.Authentication;

@CrossOrigin(origins = "/**", maxAge = 3600)
@RestController
public class AuthenticationController {

    private final Authentication authentication;

    public AuthenticationController(Authentication authentication){
        this.authentication = authentication;
    }

    @PostMapping("/login")
    public ResponseEntity<?>logIn(@RequestBody LoginUserModel loginUserModel) {

        return authentication.logIn(loginUserModel);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserModel registerUserModel) {

        return authentication.signUp(registerUserModel);

    }
}
