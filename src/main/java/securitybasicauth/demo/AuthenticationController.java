package securitybasicauth.demo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.h2.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


    @CrossOrigin
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserModel registerUserModel){


        Boolean exists = registerUserRepo.existsByEmail(registerUserModel.getEmail());

         String result;
        HttpStatus httpStatus;

        if(!exists){
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
