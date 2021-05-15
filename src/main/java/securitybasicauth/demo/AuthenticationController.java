package securitybasicauth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import securitybasicauth.demo.repositories.RegisterUserRepo;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class AuthenticationController {

    @Autowired
    RegisterUserRepo registerUserRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Bean
    PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginUserModel loginUserModel) throws Exception {


        if(authenticate(loginUserModel.getEmail(), loginUserModel.getPassword())){
             String  token =  jwtTokenUtil.generateToken(loginUserModel.getEmail());
            return ResponseEntity.ok(token);
           } else {
            return ResponseEntity.notFound().headers(HttpHeaders.EMPTY).build();
        }
    }

    private boolean authenticate(String email, String password) throws Exception {
        try{
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
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

        return new ResponseEntity(result, httpStatus);
    }


    @GetMapping("/token")
    public ResponseEntity validateToken(HttpServletRequest request) {
       String token = request.getHeader("Authorization");

       String cleanToken = token.substring(7);
       String username = jwtTokenUtil.getUsernameFromToken(cleanToken);
       if(jwtTokenUtil.validateToken(cleanToken, username)){
           return ResponseEntity.ok("Token prawidłowy");
       }
       return ResponseEntity.badRequest().build();
    }

}
