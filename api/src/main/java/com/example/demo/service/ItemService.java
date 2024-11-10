package com.example.demo.service;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.request.ItemCreateDTO;
import com.example.demo.model.ItemEntity;
import com.example.demo.repository.ItemRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ModelMapper mapper;
    private final ItemRepository itemRepository;

    public MessageResponseDTO createItem(ItemCreateDTO itemCreateDTO) {
        ItemEntity entity = mapper.map(itemCreateDTO, ItemEntity.class);
        try {
            itemRepository.save(entity);
        } catch (Exception e) {
            return new MessageResponseDTO(400, "Error creating item");
        }
        return new MessageResponseDTO(200, "Item created successfully");
    }
}
