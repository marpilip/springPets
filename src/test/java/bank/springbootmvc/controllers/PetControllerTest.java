package bank.springbootmvc.controllers;

import bank.springbootmvc.model.Pet;
import bank.springbootmvc.model.User;
import bank.springbootmvc.services.PetService;
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
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createPet() throws Exception {
        User user = userService.createUser(
                new User(
                        null,
                        "test-user",
                        "test@example.com",
                        25,
                        new ArrayList<>()
                ));

        Pet pet = new Pet(
                null,
                "Test-pet",
                user.id()
        );

        String petJson = objectMapper.writeValueAsString(pet);

        String createdPetJson = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet petResponse = objectMapper.readValue(createdPetJson, Pet.class);

        Assertions.assertEquals(pet.name(), petResponse.name());
        Assertions.assertEquals(pet.userId(), petResponse.userId());
        Assertions.assertNotNull(petResponse.id());
        Assertions.assertEquals(user.id(), petResponse.userId());
        Assertions.assertEquals(1, user.pets().size());
    }
}