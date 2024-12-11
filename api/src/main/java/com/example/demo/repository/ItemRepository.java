package com.example.demo.repository;

import com.example.demo.dto.response.ItemDTO;
import com.example.demo.model.CategoryEntity;
import com.example.demo.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    boolean existsByCategory(CategoryEntity cat);
}
