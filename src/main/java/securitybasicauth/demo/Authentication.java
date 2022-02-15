package securitybasicauth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import securitybasicauth.demo.models.LoginUserModel;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.repositories.RegisterUserRepo;
import securitybasicauth.demo.utils.JwtTokenUtils;
import securitybasicauth.demo.utils.RegisterModelValidation;

@Component
public class Authentication {

    private final RegisterUserRepo registerUserRepo;
    private final Authentication authentication;
    private final JwtTokenUtils jwtTokenUtils;
    private final RegisterModelValidation registerModelValidation;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Authentication(RegisterUserRepo registerUserRepo,
                          Authentication authentication,
                          JwtTokenUtils jwtTokenUtils,
                          RegisterModelValidation registerModelValidation,
                          AuthenticationManager authenticationManager) {
        this.registerUserRepo = registerUserRepo;
        this.authentication = authentication;
        this.jwtTokenUtils = jwtTokenUtils;
        this.registerModelValidation = registerModelValidation;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {      // passwordEncoder injected with constructor causes circular dependency exception
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(LoginUserModel loginUserModel) throws BadCredentialsException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserModel.getEmail(), loginUserModel.getPassword()));
            return true;
        } catch (BadCredentialsException badCredentialsException) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect Credentials");
        }
    }

    public ResponseEntity<?> signUp(RegisterUserModel registerUserModel) {

        boolean exists = registerUserRepo.existsByEmail(registerUserModel.getEmail());

        if (!registerModelValidation.validModel(registerUserModel)) {
            throw new ResponseStatusException(HttpStatus.PARTIAL_CONTENT, "All form fields must be filled");
        } else {
            if (!exists) {
                registerUserModel.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
                registerUserRepo.save(registerUserModel);
                return new ResponseEntity<>("User created", HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.FOUND, "User already exists");
            }
        }
    }

    public ResponseEntity<?>logIn(LoginUserModel loginUserModel) {

        if (authentication.authenticate(loginUserModel)) {
            return ResponseEntity.ok(jwtTokenUtils.generateToken(loginUserModel.getEmail()));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is not correct");
        }
    }
}
