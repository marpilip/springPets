package bank.springbootmvc.controllers;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void getUser() throws Exception {
        User user = userService.createUser(new User(
                null,
                "Test User",
                "test@example.com",
                20,
                new ArrayList<>()
        ));

        String responseJson = mockMvc.perform(get("/users/" + user.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User responseUser = objectMapper.readValue(responseJson, User.class);
        Assertions.assertEquals(user.id(), responseUser.id());
    }

    @Test
    void updateUser() throws Exception {
        User user = userService.createUser(new User(
                null,
                "Test User",
                "test@example.com",
                20,
                new ArrayList<>()
        ));

        User updatedUser = new User(
                user.id(),
                "Test User Updated",
                "testUpdated@example.com",
                23,
                new ArrayList<>()
        );

        String userJson = objectMapper.writeValueAsString(updatedUser);

        String responseJson = mockMvc.perform(put("/users/" + user.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User responseUser = objectMapper.readValue(responseJson, User.class);

        Assertions.assertEquals(user.id(), responseUser.id());
        Assertions.assertEquals(updatedUser.name(), responseUser.name());
        Assertions.assertEquals(updatedUser.email(), responseUser.email());
        Assertions.assertEquals(updatedUser.age(), responseUser.age());
        Assertions.assertEquals(updatedUser.pets(), responseUser.pets());
    }

    @Test
    void deleteUser() throws Exception {
        User user = userService.createUser(new User(
                null,
                "Test User",
                "test@example.com",
                20,
                new ArrayList<>()
        ));

        mockMvc.perform(delete("/users/" + user.id()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(new ArrayList<>(), userService.getAllUsers());
    }
}