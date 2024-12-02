package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.request.OrderCreateDTO;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
        MessageResponseDTO response =  orderService.createOrder(orderCreateDTO, principal.getName());
        return ResponseEntity.status(response.status()).body(response);
    }
}
