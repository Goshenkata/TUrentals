package com.example.demo.service;

import com.example.demo.dto.enums.CategoryEnum;
import com.example.demo.model.CategoryEntity;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void seedCategories() {
        if (categoryRepository.count() == 0) {
            for (CategoryEnum value : CategoryEnum.values()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setName(value.toString());
                categoryRepository.save(categoryEntity);
            }
        }
    }

}
