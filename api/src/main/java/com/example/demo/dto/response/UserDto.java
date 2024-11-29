package com.example.demo.dto.response;

import com.example.demo.dto.enums.RoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    //todo
    Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private RoleEnum role;
}
