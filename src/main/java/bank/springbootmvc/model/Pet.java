package bank.springbootmvc.model;

public record Pet(
        Long id,
        String name,
        Long userId
) {
}