package securitybasicauth.demo;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import securitybasicauth.demo.models.RegisterUserModel;
import securitybasicauth.demo.repositories.RegisterUserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RegisterUserRepo userRepo;

    public UserDetailsServiceImpl(RegisterUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        RegisterUserModel userModel = userRepo.findByEmail(email);

        return User.withUsername(userModel.getEmail())
                .password(userModel.getPassword())
                .disabled(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities("USER")
                .build();
    }
}
