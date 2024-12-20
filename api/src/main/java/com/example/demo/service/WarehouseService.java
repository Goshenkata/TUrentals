package com.example.demo.service;

import com.example.demo.dto.enums.OrderType;
import com.example.demo.dto.request.ItemAvailibilityChangeDTO;
import com.example.demo.dto.response.*;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.availability.OrderLineEntity;
import com.example.demo.model.availability.WarehouseLineEntity;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;
import io.swagger.v3.oas.models.links.Link;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ItemService itemService;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BeanNameViewResolver beanNameViewResolver;


    //todo bug fix, seperate the check orders that cannot be fulfilled into a different endpoint
    @Transactional
    public ChangeQuantityResult setQuantity(ItemAvailibilityChangeDTO itemAvailibilityChangeDTO) throws IllegalArgumentException {
        List<OrderDTO> affectedOrders = new ArrayList<>();
        ItemEntity item = itemRepository.findById(itemAvailibilityChangeDTO.getItemId()).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        WarehouseLineEntity warehouseQuantity = warehouseRepository.findByItem(item);
        item.setCurrentQuantity(itemAvailibilityChangeDTO.getNewQuantity());
        // adding
        if (itemAvailibilityChangeDTO.getNewQuantity() >= warehouseQuantity.getQuantity()) {
            warehouseQuantity.setQuantity(itemAvailibilityChangeDTO.getNewQuantity());
            warehouseRepository.save(warehouseQuantity);
            itemRepository.save(item);
            return new ChangeQuantityResult(
                    itemAvailibilityChangeDTO.getItemId(),
                    itemAvailibilityChangeDTO.getNewQuantity(),
                    true,
                    affectedOrders);
        }

        // removing
        affectedOrders = getOrdersThatCannotBeFulfilled(item);
        //set new quantity
        warehouseQuantity.setQuantity(itemAvailibilityChangeDTO.getNewQuantity());
        warehouseRepository.save(warehouseQuantity);
        itemRepository.save(item);


        return new ChangeQuantityResult(
                itemAvailibilityChangeDTO.getItemId(),
                itemAvailibilityChangeDTO.getNewQuantity(),
                affectedOrders.isEmpty(),
                affectedOrders);
    }

    //orders that cannot be fulfilled
    private List<OrderDTO> getOrdersThatCannotBeFulfilled(ItemEntity item) {
        int currentQuantity = item.getCurrentQuantity();
        List<OrderEntity> affectedOrders = new ArrayList<>();
        LocalDate lastDate = orderRepository.findAllByReturnDateAfterOrderByReturnDate(LocalDate.now().minusDays(1)).getLast().getReturnDate();
        //cycle from today to the last return date
        for (LocalDate date = LocalDate.now(); date.isBefore(lastDate); date = date.plusDays(1)) {
            //get all deliveries and pickups for items on the date
            List<OrderEntity> deliveries = getOrdersForItemOnDate(item, date, OrderType.DELIVERY);
            List<OrderEntity> pickups = getOrdersForItemOnDate(item, date, OrderType.PICKUP)
                    .stream().filter(o -> !affectedOrders.contains(o)).toList();


            int sumPickups = itemService.getSumQuantity(pickups, item);
            currentQuantity += sumPickups;
            for (OrderEntity delivery : deliveries) {
                int deliveryQuantity = itemService.getSumQuantity(List.of(delivery), item);
                if (currentQuantity - deliveryQuantity < 0) {
                    affectedOrders.add(delivery);
                } else {
                    currentQuantity -= deliveryQuantity;
                }
            }
            log.info("Date: " + date + " Current quantity: " + currentQuantity);
        }
        return affectedOrders
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .peek(orderDTO -> orderDTO.setOrderType(OrderType.DELIVERY))
                .toList();
    }

    private List<OrderEntity> getOrdersForItemOnDate(ItemEntity item, LocalDate date, OrderType orderType) {
        List<OrderEntity> allOnDate = switch (orderType) {
            case DELIVERY -> orderRepository.findAllDelieriesOnDate(date);
            case PICKUP -> orderRepository.findAllPickupsOnDate(date);
        };
        return allOnDate
                .stream()
                .filter(order -> order
                        .getLines()
                        .stream()
                        .anyMatch(orderItem -> orderItem.getItem().getId().equals(item.getId())))
                .toList();
    }

    public List<OrderLineDTO> getLines() {
        return warehouseRepository.findAll()
                .stream()
                .map(warehouseLineEntity -> modelMapper.map(warehouseLineEntity, OrderLineDTO.class))
                .toList();
    }

    public List<OrdersThatNeedAttentionDTO> getOrdersThatNeedAttention() {
        Map<OrderDTO, List<OrderLineDTO>> map = new HashMap<>();
        List<ItemEntity> allItems = itemRepository.findAll();
        for (ItemEntity itemEntity : allItems) {
            List<OrderDTO> ordersThatCannotBeFulfilled = getOrdersThatCannotBeFulfilled(itemEntity);
            for (OrderDTO orderDTO : ordersThatCannotBeFulfilled) {
                //get the qunatity of the item in the order
                int quantityInOrder = orderDTO.getLines().
                        stream()
                        .filter(orderLineDTO -> orderLineDTO.getItem().getId().equals(itemEntity.getId()))
                        .findFirst()
                        .get()
                        .getQuantity();
                //get the quantity in the warehouse
                int quantityInWarehouse = warehouseRepository.findByItem(itemEntity).getQuantity();
                //get the difference
                int difference = quantityInOrder - quantityInWarehouse;
                OrderLineDTO diff = new OrderLineDTO(modelMapper.map(itemEntity, ItemDTO.class), difference);

                if (!map.containsKey(orderDTO)) {
                    map.put(orderDTO, new ArrayList<>());
                }
                map.get(orderDTO).add(diff);
            }
        }
        return map.entrySet()
                .stream()
                .map(entry -> new OrdersThatNeedAttentionDTO(entry.getKey(), entry.getValue()))
                .toList();
    }
}