package com.example.demo.repository;

import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.dto.response.UserDto;
import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    List<UserEntity> findAllByRole(RoleEnum role);

    UserEntity getByEmail(String email);
}
