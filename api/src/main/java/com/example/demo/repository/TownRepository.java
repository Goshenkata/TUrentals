package com.example.demo.repository;

import com.example.demo.model.address.TownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<TownEntity, Long> {
    Optional<TownEntity> findByName(String town);
}
