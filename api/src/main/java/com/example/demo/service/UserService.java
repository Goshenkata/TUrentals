package com.example.demo.service;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.dto.request.RegistrationDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public MessageResponseDTO registerUser(RegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail()) ) {
            return new MessageResponseDTO(400, "User with email already exists");
        }
        if (userRepository.existsByPhoneNumber(registrationDTO.getPhoneNumber())) {
            return new MessageResponseDTO(400, "User with phone number already exists");
        }

        UserEntity userEntity = modelMapper.map(registrationDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userEntity.setRole(RoleEnum.USER);

        userRepository.save(userEntity);
        return new MessageResponseDTO(200, "User registration successful");
    }
}
