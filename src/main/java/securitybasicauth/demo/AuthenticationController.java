package securitybasicauth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import securitybasicauth.demo.models.LoginUserModel;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.repositories.RegisterUserRepo;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class AuthenticationController {


    private final RegisterUserRepo registerUserRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;

    public AuthenticationController(RegisterUserRepo registerUserRepo, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.registerUserRepo = registerUserRepo;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){            // passwordEncoder injected with constructor causes circular dependency exception
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginUserModel loginUserModel) throws Exception {


        if(authenticate(loginUserModel)){
             String  token =  jwtTokenUtil.generateToken(loginUserModel.getEmail());
            return ResponseEntity.ok(token);
           } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is not correct");
        }
    }

    private boolean authenticate(LoginUserModel loginUserModel) throws Exception {

        try{
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserModel.getEmail(), loginUserModel.getPassword()));
            return true;
        } catch(BadCredentialsException badCredentialsException){
            throw new Exception("Bad Credentials", badCredentialsException);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserModel registerUserModel){

        boolean exists = registerUserRepo.existsByEmail(registerUserModel.getEmail());

         String result;
        HttpStatus httpStatus;

        if(!exists){
            registerUserModel.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));

            registerUserRepo.save(registerUserModel);
            result = "User created";
            httpStatus = HttpStatus.OK;
        } else {
            result = "User already exists";
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(result, httpStatus);
    }




}
