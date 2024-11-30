package com.example.demo.init;

import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CurrentAvailabilityService;
import com.example.demo.service.ItemService;
import com.example.demo.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DBInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final WarehouseService warehouseService;
    private final CurrentAvailabilityService currentAvailabilityService;

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        initUsers();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            List<UserEntity> users = List.of(
                    new UserEntity("Admin", "Adminov", adminEmail, passwordEncoder.encode(adminPassword), "08142035434", RoleEnum.ADMIN),
                    new UserEntity("User", "Userov", "user@gmail.com", passwordEncoder.encode("userPassword"), "08242035433", RoleEnum.USER),
                    new UserEntity("Manager", "Managerov", "manager@gmail.com", passwordEncoder.encode("managerPassword"), "08342035433", RoleEnum.MANAGER),
                    new UserEntity("Employee", "Employerov", "employee@gmail.com", passwordEncoder.encode("employeePassword"), "08442035433", RoleEnum.EMPLOYEE)
            );
            userRepository.saveAll(users);
            log.info("Users initialized in the database");
        }
        warehouseService.seedWarehouse();
        currentAvailabilityService.seedCurrentAvailability();
        categoryService.seedCategories();
        itemService.seedItems();
    }
}