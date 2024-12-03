package com.example.demo.model;


import com.example.demo.dto.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity(name = "users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    @NonNull
    private String firstName;

    @Column(nullable = false)
    @NonNull
    private String lastName;

    @Column(nullable = false, unique = true)
    @NonNull
    private String email;

    @Column(nullable = false)
    @NonNull
    private String password;

    @Column(nullable = false, unique = true)
    @NonNull
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @NonNull
    private RoleEnum role;

    @OneToMany(mappedBy = "customer")
    List<OrderEntity> orders;
}
