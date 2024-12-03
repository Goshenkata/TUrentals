package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.dto.request.AdminCreateUserDTO;
import com.example.demo.dto.request.RegistrationDTO;
import com.example.demo.dto.response.UserDto;
import com.example.demo.dto.response.JWTDTO;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/user/")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<MessageResponseDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }
        MessageResponseDTO result = userService.registerUser(registrationDTO, RoleEnum.USER);
        return ResponseEntity.status(result.status()).body(result);
    }

    @PostMapping("login")
    @Operation(summary = "Logs user and returns JWT token")
    public ResponseEntity<JWTDTO> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        RoleEnum roleEnum = RoleEnum.valueOf(userDetails.getAuthorities().toArray()[0].toString());
        String jwt = jwtUtils.generateToken(userDetails.getUsername(), roleEnum);
        return ResponseEntity.ok(new JWTDTO(userDetails.getUsername(), jwt, roleEnum.toString()));
    }

    @GetMapping("isValid")
        //gets the user from the token and checks if the user is valid
    ResponseEntity<?> isValid(Principal principal, @RequestHeader("Authorization") String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        token = token.substring(7);
        if (userDetails != null) {
            return ResponseEntity.ok(new JWTDTO(userDetails.getUsername(), token, userDetails.getAuthorities().toArray()[0].toString()));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("createUser")
    @Operation(summary = "Admin creation of a new user with role")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) String role,
                                                  @RequestParam(required = false) String email) {
        if (role != null && RoleEnum.getRole(role).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.getUsers(role, email));
    }

    //todo change to id
    @DeleteMapping("deleteUser/{email}")
    @Operation(summary = "Delete a user by email")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable String email) {
        MessageResponseDTO result = userService.deleteUser(email);
        return ResponseEntity.status(result.status()).body(result);
    }

}