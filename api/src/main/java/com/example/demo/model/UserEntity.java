package com.example.demo.model;


import com.example.demo.dto.enums.RoleEnum;
import jakarta.annotation.Nonnull;
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
    @Nonnull
    private String firstName;

    @Column(nullable = false)
    @Nonnull
    private String lastName;

    @Column(nullable = false, unique = true)
    @Nonnull
    private String email;

    @Column(nullable = false)
    @Nonnull
    private String password;

    @Column(nullable = false, unique = true)
    @Nonnull
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @NonNull
    private RoleEnum role;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    List<OrderEntity> orders;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderAssignmentEntity> assignments;
}
