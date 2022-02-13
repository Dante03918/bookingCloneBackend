package securitybasicauth.demo.utils;


import org.springframework.stereotype.Component;
import securitybasicauth.demo.models.RegisterUserModel;

@Component
public class RegisterModelValidation {

    public boolean validModel(RegisterUserModel registerUserModel){

        if(registerUserModel.getEmail() != null &&
                registerUserModel.getPassword() != null &&
                registerUserModel.getName() != null &&
                registerUserModel.getSurname() != null &&
                registerUserModel.getAge() != 0)
        {
        return true;}

        else return false;
    }

}
