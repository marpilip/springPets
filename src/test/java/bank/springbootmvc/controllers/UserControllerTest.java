package bank.springbootmvc.controllers;

import bank.springbootmvc.dto.UserDto;
import bank.springbootmvc.model.User;
import bank.springbootmvc.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createUser() throws Exception {
        User user = new User(
                null,
                "Test User",
                "test@example.com",
                20,
                new ArrayList<>()
        );

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User userResponse = objectMapper.readValue(createdUserJson, User.class);
        Assertions.assertEquals(user.name(), userResponse.name());
        Assertions.assertEquals(user.email(), userResponse.email());
        Assertions.assertEquals(user.age(), userResponse.age());
        Assertions.assertEquals(user.pets(), userResponse.pets());
        Assertions.assertNotNull(userResponse.id());
    }
}