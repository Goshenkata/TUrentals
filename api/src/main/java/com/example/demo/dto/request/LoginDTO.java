package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class LoginDTO {
    @NotNull
    @Email
    private final String email;
    @NotNull
    @Size(min = 8, max = 100)
    private final String password;
}