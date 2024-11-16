package salen.tasks.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;

    @Email(message = "email should be valid")
    @NotBlank(message = "email is mandatory")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Size(min = 8, message = "password should 8 or more chars")
    private String password;

    @NotNull(message = "role is mandatory")
    private Set<String> roles;
}
