package bank.springbootmvc.converters;

import bank.springbootmvc.dto.PetDto;
import bank.springbootmvc.dto.UserDto;
import bank.springbootmvc.model.Pet;
import bank.springbootmvc.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoConverter {
    private final PetDtoConverter petDtoConverter;

    public UserDtoConverter(PetDtoConverter petDtoConverter) {
        this.petDtoConverter = petDtoConverter;
    }

    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.id());
        userDto.setName(user.name());
        userDto.setEmail(user.email());
        userDto.setAge(user.age());

        List<PetDto> petsDto = user.pets().stream()
                .map(petDtoConverter::convertToDto)
                .collect(Collectors.toList());

        userDto.setPets(petsDto);

        return userDto;
    }

    public User convertToEntity(UserDto dto) {
        List<Pet> pets = dto.getPets() != null
                ? dto.getPets().stream().map(petDtoConverter::convertToEntity).toList()
                : new ArrayList<>();

        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getAge(),
                pets
        );
    }
}
