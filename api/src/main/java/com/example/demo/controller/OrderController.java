package com.example.demo.controller;

import com.example.demo.dto.request.OrderCreateDTO;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("order/")
public class OrderController {
    private final OrderService orderService;

    @RequestMapping("create")
    public ResponseEntity<Long> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Long id = orderService.createOrder(orderCreateDTO);
        return id != -1 ? ResponseEntity.ok(id) : ResponseEntity.badRequest().build();
    }
}
