package com.example.demo.controller;

import com.example.demo.dto.request.ItemAvailibilityChangeDTO;
import com.example.demo.dto.response.ChangeQuantityResult;
import com.example.demo.dto.response.OrderLineDTO;
import com.example.demo.dto.response.OrdersThatNeedAttentionDTO;
import com.example.demo.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/setQuantity")
    @Operation(summary = "changes the quantity of an item in the warehouse, returns orders that have been made unfulfillable by the change")
    public ResponseEntity<ChangeQuantityResult> setQuantity(@RequestBody @Valid ItemAvailibilityChangeDTO itemAvailibilityChangeDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            ChangeQuantityResult changeQuantityResult = warehouseService.setQuantity(itemAvailibilityChangeDTO);
            return ResponseEntity.ok(changeQuantityResult);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getLines")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<OrderLineDTO>> getLines() {
        return ResponseEntity.ok(warehouseService.getLines());
    }

    @GetMapping("/getOrdersThatNeedAttention")
    @Operation(summary = "gets all the orders that have been make unfulfillable by the changing of the quantity of an item")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<OrdersThatNeedAttentionDTO>> getOrdersThatNeedAttention() {
        return ResponseEntity.ok(warehouseService.getOrdersThatNeedAttention());
    }
}