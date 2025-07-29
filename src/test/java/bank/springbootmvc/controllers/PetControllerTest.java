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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void updatePet() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test-user",
                "test@example.com",
                25,
                new ArrayList<>()
        ));

        Pet pet = petService.createPet(new Pet(
                null,
                "Test-pet",
                user.id()
        ));

        Pet updatedPet = new Pet(
                pet.id(),
                "Test-Updated-pet",
                user.id()
        );

        String updatedPetJson = objectMapper.writeValueAsString(updatedPet);

        String responseJson = mockMvc.perform(put("/pets/" + pet.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPetJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet petResponse = objectMapper.readValue(responseJson, Pet.class);
        Assertions.assertEquals(updatedPet.name(), petResponse.name());
        Assertions.assertEquals(user.id(), petResponse.userId());
        Assertions.assertNotNull(petResponse.id());
    }

    @Test
    void deletePet() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test-user",
                "test@example.com",
                25,
                new ArrayList<>()
        ));

        Pet pet = petService.createPet(new Pet(
                null,
                "Test-pet",
                user.id()
        ));

        mockMvc.perform(delete("/pets/" + pet.id()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(new ArrayList<>(), user.pets());
    }

    @Test
    void getPetById() throws Exception {
        User user = userService.createUser(new User(
                null,
                "test-user",
                "test@example.com",
                25,
                new ArrayList<>()
        ));

        Pet pet = petService.createPet(new Pet(
                null,
                "Test-pet",
                user.id()
        ));

        String responseJson = mockMvc.perform(get("/pets/" + pet.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet responsePet = objectMapper.readValue(responseJson, Pet.class);
        Assertions.assertEquals(pet.id(), responsePet.id());
        Assertions.assertEquals(pet.name(), responsePet.name());
    }
}