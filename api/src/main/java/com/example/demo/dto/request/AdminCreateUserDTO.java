package com.example.demo.dto.request;

import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.validation.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCreateUserDTO {
    @NotNull
    @Size(min = 3, max = 100)
    private String firstName;
    @NotNull
    @Size(min = 3, max = 100)
    private String lastName;
    @NotNull
    @Size(min = 10, max = 10)
    private String phoneNumber;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 8, max = 100)
    private String password;
    @ValidEnum(enumClass = RoleEnum.class)
    @NotNull
    private String role;
}