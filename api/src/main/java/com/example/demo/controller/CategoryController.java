package com.example.demo.controller;

import com.example.demo.dto.request.ItemCreateDTO;
import com.example.demo.dto.response.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("category/")
public class CategoryController {
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("create")
    public ResponseEntity<Long> createCategory(@RequestBody ItemCreateDTO itemCreateDTO) {
        return ResponseEntity.status(501).build();
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("search")
    ResponseEntity<List<CategoryDTO>> searchCategories() {
        return ResponseEntity.status(501).build();
    }
}
