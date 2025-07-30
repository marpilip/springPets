package bank.springbootmvc.services;

import bank.springbootmvc.model.Pet;
import bank.springbootmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {
    private final AtomicLong counter;
    private final UserService userService;

    public PetService(UserService userService) {
        this.counter = new AtomicLong();
        this.userService = userService;
    }

    public Pet createPet(Pet pet) {
        Pet createdPet = new Pet(
                counter.incrementAndGet(),
                pet.name(),
                pet.userId()
        );

        userService.getUser(pet.userId()).pets().add(createdPet);

        return createdPet;
    }

    public Pet updatePet(Pet pet) {
        if (pet.id() == null) {
            throw new NoSuchElementException("ID питомца не может быть null");
        }

        Pet foundPet = findPetById(pet.id()).orElseThrow(NoSuchElementException::new);
        Pet updatedPet = new Pet(
                foundPet.id(),
                pet.name(),
                pet.userId()
        );

        User user = userService.getUser(pet.userId());
        user.pets().remove(foundPet);
        user.pets().add(updatedPet);

        return updatedPet;
    }

    public void deletePet(Pet pet) {
        if (pet.id() == null) {
            throw new NoSuchElementException("ID питомца не может быть null");
        }

        Pet foundPet = findPetById(pet.id()).orElseThrow(NoSuchElementException::new);
        userService.getUser(pet.userId()).pets().remove(foundPet);
    }

    public Pet getPet(long id) {
        return findPetById(id).orElseThrow(NoSuchElementException::new);
    }

    public Optional<Pet> findPetById(Long id) {
        return userService.getAllUsers().stream()
                .flatMap(user -> user.pets().stream())
                .filter(pet -> pet.id().equals(id))
                .findAny();
    }
}
