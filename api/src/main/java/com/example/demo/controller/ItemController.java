package com.example.demo.controller;

import com.example.demo.dto.common.FilterDTO;
import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.SortBy;
import com.example.demo.dto.request.ItemCreateDTO;
import com.example.demo.dto.response.ChangeQuantityResult;
import com.example.demo.dto.response.ItemDTO;
import com.example.demo.service.ItemService;
import com.example.demo.validation.DeliveryBeforeReturn;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("item/")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("create")
    @Operation(summary = "Create a new item")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<MessageResponseDTO> createItem(@Valid @RequestBody ItemCreateDTO itemCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body(new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }
        Long id = itemService.createItem(itemCreateDTO);
        return id != -1 ? ResponseEntity.ok(new MessageResponseDTO(200, "Item created with id: " + id)) : ResponseEntity.badRequest().body(new MessageResponseDTO(400, "Item with this name already exists"));
    }
    @GetMapping("search")
    public ResponseEntity<List<ItemDTO>> searchItems(
            @RequestParam(required = false) String nameQuery,
            @RequestParam(required = false) BigDecimal priceFrom,
            @RequestParam(required = false) BigDecimal priceTo,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) SortBy sortBy
    ) {
        FilterDTO filter = new FilterDTO(nameQuery, priceFrom, priceTo, category, sortBy);
        List<ItemDTO> items = itemService.search(filter);
        return ResponseEntity.ok(items);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get item by id")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        Optional<ItemDTO> item = itemService.getItem(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}