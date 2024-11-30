package com.example.demo.repository;

import com.example.demo.model.availability.CurrentAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAvailabilityRepository extends JpaRepository<CurrentAvailabilityEntity, Long> {
}
