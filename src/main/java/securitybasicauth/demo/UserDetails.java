package securitybasicauth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import securitybasicauth.demo.repositories.RegisterUserRepo;

public class UserDetails  implements UserDetailsService {

//    @Autowired
//    RegisterUserModel registerUserModel;

    @Autowired
    PasswordEncoder bcryptEncoder;

    @Autowired
    RegisterUserRepo userRepo;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        if(userRepo.existsByEmail(email)){
//            RegisterUserModel userModel = userRepo.loadByEmail(email);
//
//        }
        return null;
    }
}
