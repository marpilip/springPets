package bank.springbootmvc.controllers;

import bank.springbootmvc.converters.PetDtoConverter;
import bank.springbootmvc.dto.PetDto;
import bank.springbootmvc.model.Pet;
import bank.springbootmvc.services.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;
    private final PetDtoConverter petDtoConverter;

    public PetController(PetService petService, PetDtoConverter petDtoConverter) {
        this.petService = petService;
        this.petDtoConverter = petDtoConverter;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody @Validated PetDto petDto) {
        Pet pet = petDtoConverter.convertFromDto(petDto);
        Pet createdPet = petService.createPet(pet);
        return ResponseEntity.ok().body(petDtoConverter.convertToDto(createdPet));
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetDto> updatePet(@PathVariable("petId") Long id,
                                            @RequestBody @Validated PetDto petDto) {
        Pet pet = new Pet(
                id,
                petDto.getName(),
                petDto.getUserId()
        );

        Pet updatedPet = petService.updatePet(pet);
        return ResponseEntity.ok().body(petDtoConverter.convertToDto(updatedPet));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetDto> getPetById(@PathVariable("petId") Long id) {
        Pet pet = petService.getPet(id);
        return ResponseEntity.ok().body(petDtoConverter.convertToDto(pet));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<PetDto> deletePet(@PathVariable("petId") Long id) {
        petService.deletePet(petService.getPet(id));
        return ResponseEntity.noContent().build();
    }
}
