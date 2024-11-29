package com.example.demo.controller;


import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.request.ItemAvailibilityChangeDTO;
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
@PreAuthorize("hasAuthority('MANAGER')")
public class ManagerController {
    private ItemService itemService;

    @PostMapping("/create")
    @Operation(summary = "Create a new item")
    public MessageResponseDTO createItem(@Valid ItemCreateDTO itemCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return itemService.createItem(itemCreateDTO);
    }

    @PostMapping("/create")
    @Operation(summary = "Allows the manager to change the availability of an item")
    public MessageResponseDTO changeAvailability(@Valid ItemAvailibilityChangeDTO itemAvailibilityChangeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return itemService.changeAvailability(itemAvailibilityChangeDTO);
    }

}
