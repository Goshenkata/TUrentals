package com.example.demo.service;

import com.example.demo.model.availability.CurrentAvailabilityEntity;
import com.example.demo.repository.CurrentAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class CurrentAvailabilityService {
    private final CurrentAvailabilityRepository currentAvailabilityRepository;

    public void seedCurrentAvailability() {
        if (currentAvailabilityRepository.count() == 0) {
            CurrentAvailabilityEntity currentAvailabilityEntity = new CurrentAvailabilityEntity();
            currentAvailabilityEntity.setItems(new ArrayList<>());
            currentAvailabilityRepository.save(currentAvailabilityEntity);
        }
    }
}
