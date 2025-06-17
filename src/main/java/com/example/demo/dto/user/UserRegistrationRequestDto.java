package com.example.demo.dto.user;

import com.example.demo.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@PasswordMatches
@Data
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 20, message = "Пароль має містити від 8 до 20 символів")
    private String password;
    @NotBlank
    @Length(min = 8, max = 20, message = "Пароль має містити від 8 до 20 символів")
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String shippingAddress;

    public UserRegistrationRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
