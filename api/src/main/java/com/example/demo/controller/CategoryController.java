package com.example.demo.controller;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.response.CategoryDTO;
import com.example.demo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("")
    @Operation(summary = "Add a new category, if the category already exists it does nothing")
    public ResponseEntity<?> createCategory(@RequestParam() String name) {
        categoryService.createCategory(name);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("")
//    @Operation(summary = "Delete a category by name")
//    public ResponseEntity<MessageResponseDTO> deleteCategory(@RequestParam String name) {
//        MessageResponseDTO response = categoryService.deleteCategory(name);
//        return ResponseEntity.status(response.status()).body(response);
//    }

    @GetMapping("search")
    @Operation(summary = "returns all categories or fuzzy search by name")
    @PreAuthorize("hasAuthority('MANAGER')")
    ResponseEntity<List<String>> searchCategories(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(categoryService.searchCategories(query));
    }
}
