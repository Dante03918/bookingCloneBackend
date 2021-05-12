package securitybasicauth.demo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.internal.dynalink.support.NameCodec;
import org.h2.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import sun.security.util.Password;


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

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginUserModel loginUserModel) throws Exception {

        authenticate(loginUserModel.getEmail(), loginUserModel.getPassword());

       String token =  jwtTokenUtil.generateToken(loginUserModel.getEmail());

        return ResponseEntity.ok(token);
    }

    private void authenticate(String email, String password) throws Exception {
        try{
          Authentication result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            System.out.println(result);
        } catch(BadCredentialsException badCredentialsException){
            throw new Exception("Bad Credentials", badCredentialsException);
        }
    }


    @CrossOrigin
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserModel registerUserModel){


        Boolean exists = registerUserRepo.existsByEmail(registerUserModel.getEmail());

         String result;
        HttpStatus httpStatus;

        if(!exists){
            registerUserModel.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));

            registerUserRepo.save(registerUserModel);
            result = "User created";
            httpStatus = HttpStatus.OK;
        } else {
            result = "User alredy exists";
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity(result, httpStatus);
    }


}
