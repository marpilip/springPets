package bank.springbootmvc.controllers;

import bank.springbootmvc.converters.PetDtoConverter;
import bank.springbootmvc.converters.UserDtoConverter;
import bank.springbootmvc.dto.UserDto;
import bank.springbootmvc.model.User;
import bank.springbootmvc.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final PetDtoConverter petDtoConverter;

    public UserController(UserService userService, UserDtoConverter userDtoConverter, PetDtoConverter petDtoConverter) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
        this.petDtoConverter = petDtoConverter;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Validated UserDto userDto) {
        User createdUser = userService.createUser(userDtoConverter.convertFromDto(userDto));
        return ResponseEntity.ok(userDtoConverter.convertToDto(createdUser));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long id,
                                              @RequestBody @Validated UserDto userDto) {
        User user = new User(
                id,
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge(),
                userDto.getPets().stream().map(petDtoConverter::convertFromDto).toList()
        );

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(userDtoConverter.convertToDto(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(userDtoConverter.convertToDto(user));
    }
}
