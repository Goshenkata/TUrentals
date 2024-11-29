package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.dto.request.AdminCreateUserDTO;
import com.example.demo.dto.request.RegistrationDTO;
import com.example.demo.dto.response.UserDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @PostMapping("createUser")
    @Operation(summary = "Admin creation of a new user with role")
    public ResponseEntity<MessageResponseDTO> adminCreateUser(@Valid @RequestBody AdminCreateUserDTO dto,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }
        MessageResponseDTO result = userService.registerUser(modelMapper.map(dto, RegistrationDTO.class), RoleEnum.valueOf(dto.getRole()));
        return ResponseEntity.status(result.status()).body(result);
    }

    @GetMapping("getUsers")
    @Operation(summary = "Gets users, can be sorted by role or email")
    public ResponseEntity<List<UserDto>>  getUsers(@RequestParam(required = false) String role,
                                                   @RequestParam(required = false) String email) {
        if (role != null && RoleEnum.getRole(role).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.getUsers(role, email));
    }

    //todo change to id
    @DeleteMapping("deleteUser/{email}")
    @Operation(summary = "Delete a user by email")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable String email) {
        MessageResponseDTO result = userService.deleteUser(email);
        return ResponseEntity.status(result.status()).body(result);
    }
}