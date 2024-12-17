package com.example.demo.service;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.CategoryEnum;
import com.example.demo.dto.response.CategoryDTO;
import com.example.demo.model.CategoryEntity;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    public void seedCategories() {
        if (categoryRepository.count() == 0) {
            for (CategoryEnum value : CategoryEnum.values()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setName(value.toString());
                categoryRepository.save(categoryEntity);
            }
        }
    }

    public void createCategory(String name) {
        Optional<CategoryEntity> cat = categoryRepository.findByName(name);
        if (cat.isEmpty()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(name);
            categoryRepository.save(categoryEntity);
        }
    }

    public MessageResponseDTO deleteCategory(String name) {
        //test
        Optional<CategoryEntity> cat = categoryRepository.findByName(name);
        if (cat.isEmpty()) {
            return new MessageResponseDTO(404, "Category not found");
        }
        if (itemRepository.existsByCategory(cat.get())) {
            return new MessageResponseDTO(400, "Category is in use");
        }

        categoryRepository.delete(cat.get());
        return new MessageResponseDTO(200, "Category deleted");
    }

    public List<CategoryDTO> searchCategories(String query) {
        List<CategoryDTO> categies = categoryRepository.findAll()
                .stream()
                .map(categoryEntity -> modelMapper.map(categoryEntity, CategoryDTO.class))
                .toList();
        if (query != null) {
            List<CategoryDTO> sorted = new ArrayList<>();
            FuzzySearch.extractSorted(query, categies.stream().map(CategoryDTO::getName).toList(), 80)
                    .forEach(extractedResult -> {
                        sorted.add(categies.get(extractedResult.getIndex()));
                    });
            return sorted;
        }
        return categies;
    }

}
