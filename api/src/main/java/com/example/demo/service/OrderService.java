package com.example.demo.service;

import com.example.demo.dto.request.OrderCreateDTO;
import com.example.demo.model.availability.CurrentAvailabilityEntity;
import com.example.demo.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private  final OrderRepository orderRepository;
    private final CurrentAvailabilityService currentAvailabilityService;

    //todo
    public Long createOrder(@Valid OrderCreateDTO orderCreateDTO) {
        return -1L;
    }
}
