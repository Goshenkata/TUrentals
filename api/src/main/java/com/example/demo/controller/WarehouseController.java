package com.example.demo.controller;

import com.example.demo.dto.request.ItemAvailibilityChangeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WarehouseController {
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> changeQuantity(ItemAvailibilityChangeDTO itemAvailibilityChangeDTO) {
        return ResponseEntity.status(501).build();
    }
}