package com.example.demo.repository;

import com.example.demo.model.address.PostCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostCodeRepository extends JpaRepository<PostCodeEntity, Long> {
    Optional<PostCodeEntity> findByCode(String postCode);
}
