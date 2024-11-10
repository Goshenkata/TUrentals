package com.example.demo.controller;


import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.request.ItemCreateDTO;
import com.example.demo.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/items/")
@RestController
@AllArgsConstructor
public class ItemController {
    private ItemService itemService;

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Create a new item, only for managers")
    public MessageResponseDTO createItem(@Valid ItemCreateDTO itemCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return itemService.createItem(itemCreateDTO);
    }
}
