package bank.springbootmvc.converters;

import bank.springbootmvc.dto.PetDto;
import bank.springbootmvc.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetDtoConverter {
    public PetDto convertToDto(Pet pet) {
        PetDto petDto = new PetDto();

        petDto.setId(pet.id());
        petDto.setName(pet.name());
        petDto.setUserId(pet.userId());
        return petDto;
    }

    public Pet convertFromDto(PetDto petDto) {
        return new Pet(
                petDto.getId(),
                petDto.getName(),
                petDto.getUserId()
        );
    }
}
