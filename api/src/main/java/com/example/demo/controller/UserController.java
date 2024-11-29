package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.dto.request.RegistrationDTO;
import com.example.demo.dto.response.JWTDTO;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user/")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

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
        UserDetails userDetails =(UserDetails) authentication.getPrincipal();
        RoleEnum roleEnum = RoleEnum.valueOf(userDetails.getAuthorities().toArray()[0].toString());
        String jwt = jwtUtils.generateToken(userDetails.getUsername(), roleEnum);
        return ResponseEntity.ok(new JWTDTO(userDetails.getUsername(), jwt, roleEnum.toString()));
    }

    @PostMapping("isValid")
    ResponseEntity<?> isValid(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        boolean result = jwtUtils.validateToken(token);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}