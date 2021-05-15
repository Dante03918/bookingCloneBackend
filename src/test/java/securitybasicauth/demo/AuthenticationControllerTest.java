package securitybasicauth.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void signupShouldReturnStatus200And400() throws Exception {

        RegisterUserModel registerUserModel = new RegisterUserModel();

        ObjectMapper mapper = new ObjectMapper();

        registerUserModel.setPassword("pass");
        registerUserModel.setAge(43);
        registerUserModel.setGender("m");
        registerUserModel.setEmail("a@a.pl");
        registerUserModel.setSurname("dante");
        registerUserModel.setName("dante");

        String json = mapper.writeValueAsString(registerUserModel);

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string("User created"));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().string("User already exists"));

    }

    @Test
    void shouldReturnStatus200WithJwtToken() throws Exception {

        RegisterUserModel registerUserModel = new RegisterUserModel();

        ObjectMapper mapper = new ObjectMapper();

        registerUserModel.setPassword("pass1");
        registerUserModel.setAge(43);
        registerUserModel.setGender("m");
        registerUserModel.setEmail("a@a1.pl");
        registerUserModel.setSurname("dante");
        registerUserModel.setName("dante");

        String json = mapper.writeValueAsString(registerUserModel);

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        LoginUserModel loginUserModel = new LoginUserModel();

        loginUserModel.setEmail("a@a1.pl");
        loginUserModel.setPassword("pass1");

        String jsonLogin = mapper.writeValueAsString(loginUserModel);


        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(jsonLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

}