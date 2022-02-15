package securitybasicauth.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import securitybasicauth.demo.utils.JwtTokenUtils;
import securitybasicauth.demo.models.LoginUserModel;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.repositories.RegisterUserRepo;
import securitybasicauth.demo.utils.RegisterModelValidation;
import securitybasicauth.demo.Authentication;

@CrossOrigin(origins = "/**", maxAge = 3600)
@RestController
public class AuthenticationController {


    private final RegisterUserRepo registerUserRepo;
    private final Authentication authentication;
    private final JwtTokenUtils jwtTokenUtils;
    private final RegisterModelValidation registerModelValidation;
    private PasswordEncoder passwordEncoder;

    public AuthenticationController(RegisterUserRepo registerUserRepo,
                                    Authentication authentication,
                                    JwtTokenUtils jwtTokenUtils,
                                    RegisterModelValidation registerModelValidation) {
        this.registerUserRepo = registerUserRepo;
        this.authentication = authentication;
        this.jwtTokenUtils = jwtTokenUtils;
        this.registerModelValidation = registerModelValidation;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {      // passwordEncoder injected with constructor causes circular dependency exception
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginUserModel loginUserModel) {

        if (authentication.authenticate(loginUserModel)) {
            return ResponseEntity.ok(jwtTokenUtils.generateToken(loginUserModel.getEmail()));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is not correct");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserModel registerUserModel) {

        boolean exists = registerUserRepo.existsByEmail(registerUserModel.getEmail());

        if (!registerModelValidation.validModel(registerUserModel)) {
            throw new ResponseStatusException(HttpStatus.PARTIAL_CONTENT, "All form fields must be filled");
        } else {
            if (!exists) {
                registerUserModel.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
                registerUserRepo.save(registerUserModel);
                return new ResponseEntity<>( "User created", HttpStatus.OK);
            } else {
                throw  new ResponseStatusException(HttpStatus.FOUND, "User already exists");
            }
        }
    }
}
