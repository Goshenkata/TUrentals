package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.request.AssignToEmployeeDTO;
import com.example.demo.dto.request.OrderCreateDTO;
import com.example.demo.dto.response.OrderDTO;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("order/")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("create")
    public ResponseEntity<MessageResponseDTO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        MessageResponseDTO response = orderService.createOrder(orderCreateDTO, principal.getName());
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("getPending")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "For managers, get the pending orders")
    public ResponseEntity<List<OrderDTO>> getPendingOrders() {
        List<OrderDTO> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("assignEmployee")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "For managers, assign an employee to an order")
    public ResponseEntity<MessageResponseDTO> assignEmployee(@RequestBody AssignToEmployeeDTO assignment) {
        MessageResponseDTO response = orderService.assignEmployee(assignment);
        return ResponseEntity.status(response.status()).body(response);
    }
}