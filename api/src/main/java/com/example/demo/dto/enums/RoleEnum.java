package com.example.demo.dto.enums;

import java.util.Optional;

public enum RoleEnum {
    USER, EMPLOYEE, MANAGER, ADMIN;

    public static Optional<RoleEnum> getRole(String role) {
        try {
            return Optional.of(RoleEnum.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
