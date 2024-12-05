package com.example.demo.controller;

import com.example.demo.dto.common.FilterDTO;
import com.example.demo.dto.enums.SortBy;
import com.example.demo.dto.request.ItemCreateDTO;
import com.example.demo.dto.response.ItemDTO;
import com.example.demo.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("item/")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("create")
    @Operation(summary = "Create a new item")
    public ResponseEntity<Long> createItem(@Valid ItemCreateDTO itemCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Long id = itemService.createItem(itemCreateDTO);
        return id != -1 ? ResponseEntity.ok(id) : ResponseEntity.badRequest().build();
    }
//    //todo add pagination and search
//    @GetMapping("search")
//    public ResponseEntity<List<ItemDTO>> searchItems(
//            @RequestParam String nameQuery,
//            @RequestParam(required = false) BigDecimal priceFrom,
//            @RequestParam(required = false) BigDecimal priceTo,
//            @RequestParam(required = false) String category,
//            @RequestParam(required = false) SortBy sortBy,
//            Pageable pageable
//    ) {
//        FilterDTO
//        List<ItemDTO> items = itemService.search();
//        return ResponseEntity.ok(items);
//    }

}
