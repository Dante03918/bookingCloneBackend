//package securitybasicauth.demo.repositories;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import securitybasicauth.demo.RegisterUserModel;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RegisterUserRepoTest {
//
//    @Autowired
//    RegisterUserRepo registerUserRepo;
//
//    @Test
//    public void shouldSaveObjectInDatasource() {
//
//        RegisterUserModel registerUserModel = new RegisterUserModel();
//        registerUserModel.setName("Test_Name");
//        registerUserModel.setSurname("Test_Surname");
//        registerUserModel.setEmail("test@test.pl");
//        registerUserModel.setGender("woman");
//        registerUserModel.setAge(32);
//        registerUserModel.setPassword("Test_Password");
//
//        registerUserRepo.save(registerUserModel);
//
//        assertTrue(registerUserRepo.existsByEmail("test@test.pl"));
//
//    }
//
//}