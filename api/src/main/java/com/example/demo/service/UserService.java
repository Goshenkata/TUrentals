package com.example.demo.service;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.dto.request.RegistrationDTO;
import com.example.demo.dto.response.UserDto;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public MessageResponseDTO registerUser(RegistrationDTO registrationDTO, RoleEnum role) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            return new MessageResponseDTO(400, "User with email already exists");
        }
        if (userRepository.existsByPhoneNumber(registrationDTO.getPhoneNumber())) {
            return new MessageResponseDTO(400, "User with phone number already exists");
        }

        UserEntity userEntity = modelMapper.map(registrationDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userEntity.setRole(role);

        userRepository.save(userEntity);
        return new MessageResponseDTO(200, "User registration successful");
    }

    public List<UserDto> getUsers(String role, String email) {
        List<UserEntity> userEntities = new ArrayList<>();

        if (role != null) {
            //check if the role string can be converted to a RoleEnum
            if (RoleEnum.getRole(role).isEmpty()) {
                return List.of();
            }
            userEntities = userRepository.findAllByRole(RoleEnum.valueOf(role.toUpperCase()));
        } else {
            userEntities = userRepository.findAll();
        }

        if (email != null) {
            userEntities = FuzzySearch.extractSorted(
                    email,
                    userEntities.stream().map(UserEntity::getEmail).toList(),
                    75
            ).stream().map(ExtractedResult::getString).map(userRepository::getByEmail).toList();
        }

        return userEntities.stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .limit(100)
                .toList();
    }

    public MessageResponseDTO deleteUser(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isEmpty()) {
            return new MessageResponseDTO(404, String.format("User with email %s not found", email));
        }
        userRepository.delete(userEntity.get());
        return new MessageResponseDTO(200, String.format("User with email %s deleted", email));
    }
}
