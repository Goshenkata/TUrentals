package com.example.demo.service;

import com.example.demo.dto.common.MessageResponseDTO;
import com.example.demo.dto.enums.OrderStatus;
import com.example.demo.dto.enums.OrderType;
import com.example.demo.dto.enums.RoleEnum;
import com.example.demo.dto.request.AddressDTO;
import com.example.demo.dto.request.AssignToEmployeeDTO;
import com.example.demo.dto.request.ItemNumberPairDTO;
import com.example.demo.dto.request.OrderCreateDTO;
import com.example.demo.dto.response.OrderDTO;
import com.example.demo.dto.response.UserDto;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.OrderAssignmentEntity;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.model.availability.OrderLineEntity;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final ModelMapper modelMapper;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final OrderLineRepository orderLineRepository;
    private final AssignmentRepository assignmentRepository;

    @Transactional
    public MessageResponseDTO createOrder(OrderCreateDTO orderCreateDTO, String email) {
        Optional<UserEntity> customer = userRepository.findByEmail(email);
        if (customer.isEmpty()) {
            return new MessageResponseDTO(501, "User not found");
        }

        OrderEntity orderEntity = modelMapper.map(orderCreateDTO, OrderEntity.class);
        orderEntity.setDeliveryAddress(addressService.createAddress(orderCreateDTO.getAddress()));
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setCustomer(customer.get());

        BigDecimal pricePerDay = BigDecimal.ZERO;
        int totalDays = (int) (orderCreateDTO.getReturnDate().toEpochDay() - orderCreateDTO.getDeliveryDate().toEpochDay());

        List<OrderLineEntity> lines = new ArrayList<>();
        for (ItemNumberPairDTO itemDTO : orderCreateDTO.getItems()) {
            Optional<ItemEntity> itemEntity = itemRepository.findById(itemDTO.getItemId());
            if (itemEntity.isEmpty()) {
                return new MessageResponseDTO(501, "Item not found");
            }
            int availability = itemService.checkAvailabilityAtDateRange(itemDTO.getItemId(), orderCreateDTO.getDeliveryDate(), orderCreateDTO.getReturnDate());
            if (availability < itemDTO.getQuantity()) {
                log.warn("Not enough items available");
                return new MessageResponseDTO(501, "Not enough items available");
            }
            OrderLineEntity line = new OrderLineEntity();
            line.setItem(itemEntity.get());
            line.setQuantity(itemDTO.getQuantity());
            lines.add(line);
            pricePerDay = pricePerDay.add(itemEntity.get().getPricePerDay().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }
        orderEntity.setLines(lines);
        orderEntity.setTotalPrice(pricePerDay.multiply(BigDecimal.valueOf(totalDays)));

        orderLineRepository.saveAll(lines);
        orderRepository.save(orderEntity);
        return new MessageResponseDTO(200, "Order " + orderEntity.getId() + " created");
    }

    @Transactional
    public void seedOrders() {
        String email = "user@gmail.com";
        ItemEntity itemEntity = itemRepository.findAll().getFirst();
        if (orderRepository.count() == 0) {
            //should be available
            createOrder(
                    new OrderCreateDTO(
                            LocalDate.now(),
                            LocalDate.now().plusDays(1),
                            new AddressDTO("Bulgaria", "Sofia", "Sofia", "ul. Tintyava 15", "1000", "Leave items at the driveway"),
                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 50)))
                    , email
            );
            //should be available
            createOrder(
                    new OrderCreateDTO(
                            LocalDate.now().plusDays(1),
                            LocalDate.now().plusDays(3),
                            new AddressDTO("Bulgaria", "Sofia", "Sofia", "ul. Georgi Raychev 15", "1000", "Leave items at the driveway"),
                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
                    , email
            );
            //should fail
            createOrder(
                    new OrderCreateDTO(
                            LocalDate.now(),
                            LocalDate.now().plusDays(2),
                            new AddressDTO("Bulgaria", "Pleven", "Pleven", "ul. Ivan Kirkov 17", "5800", "Leave items at the driveway"),
                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 50)))
                    , email
            );
            //should work
            createOrder(
                    new OrderCreateDTO(
                            LocalDate.now().plusDays(1),
                            LocalDate.now().plusDays(4),
                            new AddressDTO("Bulgaria", "Varna", "Varna", "boul. Mariya Louiza 1", "9000", "Leave items at the driveway"),
                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 40)))
                    , email
            );
            //should fail
            createOrder(
                    new OrderCreateDTO(
                            LocalDate.now().plusDays(3),
                            LocalDate.now().plusDays(5),
                            new AddressDTO("Bulgaria", "Varna", "Varna", "boul. Primorski 3", "9000", "Leave items at the driveway"),
                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 40)))
                    , email
            );
            createOrder(
                    new OrderCreateDTO(
                            LocalDate.now().plusDays(3),
                            LocalDate.now().plusDays(5),
                            new AddressDTO("Bulgaria", "Kyustendil", "Dupnitsa", "ul. Ivan Stranski 15", "2600", "Leave items at the driveway"),
                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
                    , email
            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Kyustendil", "Kyustendil", "ul. Roden krai 10", "2500", "Leave items at the driveway"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Sofia", "Sofia", "boul. St.Klimet Ohridski 16", "1000", "Leave items at the driveway"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Sofia", "Sofia", "ul. Vitosha 15", "1000", "Leave at the main entrance"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Plovdiv", "Plovdiv City", "ul. Kapitan Raycho 10", "4000", "Leave at the back door"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Varna", "Varna City", "ul. Kniaz Boris 20", "9000", "Deliver to reception"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Ruse", "Ruse City", "ul. Petko Slaveykov 5", "7000", "Leave at the parking lot"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Stara Zagora", "Stara Zagora City", "ul. Todor Kableshkov 12", "6000", "Drop off at the side gate"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Veliko Tarnovo", "Veliko Tarnovo City", "ul. Ivaylo 30", "5000", "Deliver to the front porch"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Blagoevgrad", "Blagoevgrad City", "ul. Georgi Izmirliev 22", "2700", "Hand over at the security desk"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Shumen", "Shumen City", "ul. Dobri Voynikov 35", "9700", "Drop off near the stairs"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
//            createOrder(
//                    new OrderCreateDTO(
//                            LocalDate.now(),
//                            LocalDate.now().plusDays(5),
//                            new AddressDTO("Bulgaria", "Burgas", "Burgas City", "ul. Alexander Battenberg 25", "8000", "Hand over to the concierge"),
//                            List.of(new ItemNumberPairDTO(itemEntity.getId(), 10)))
//                    , email
//            );
        }
    }

    private Optional<OrderAssignmentEntity> getAssignment(OrderEntity orderEntity, OrderType orderType) {
        return orderEntity.getAssignments().stream().filter(orderAssignmentEntity -> orderAssignmentEntity.getOrderType().equals(orderType)).findFirst();
    }

    public List<OrderDTO> getPendingOrders() {
        List<OrderEntity> pendingOrdersAfterToday = orderRepository.findAllByDeliveryDateOrReturnDateAfterAndStatus(LocalDate.now(), LocalDate.now(), OrderStatus.PENDING);
        List<OrderDTO> pendingOrders = new ArrayList<>();
        //splits the orders into deliveries and pickups
        for (OrderEntity orderEntity : pendingOrdersAfterToday) {
            OrderDTO pickup = modelMapper.map(orderEntity, OrderDTO.class);
            pickup.setOrderType(OrderType.PICKUP);
            pendingOrders.add(pickup);

            getAssignment(orderEntity, OrderType.PICKUP).ifPresent(orderAssignmentEntity -> {
                pickup.setAssignenedTo(modelMapper.map(orderAssignmentEntity.getEmployee(), UserDto.class));
            });

            //add the delivery
            if (!orderEntity.getDeliveryDate().isBefore(LocalDate.now())) {
                OrderDTO delivery = modelMapper.map(orderEntity, OrderDTO.class);
                delivery.setOrderType(OrderType.DELIVERY);
                getAssignment(orderEntity, OrderType.DELIVERY).ifPresent(orderAssignmentEntity -> {
                    pickup.setAssignenedTo(modelMapper.map(orderAssignmentEntity.getEmployee(), UserDto.class));
                });
                pendingOrders.add(delivery);

            }
        }

        List<OrderDTO> orderedList = pendingOrders.stream().sorted((o1, o2) -> {
            //if both are deliveries, sort by delivery date
            if (o1.getOrderType().equals(OrderType.DELIVERY) && o2.getOrderType().equals(OrderType.DELIVERY)) {
                return o1.getDeliveryDate().compareTo(o2.getDeliveryDate());
            }
            //if both are pickups, sort by return date
            if (o1.getOrderType().equals(OrderType.PICKUP) && o2.getOrderType().equals(OrderType.PICKUP)) {
                return o1.getReturnDate().compareTo(o2.getReturnDate());
            }
            //if the left is delivery and the other is pickup
            if (o1.getOrderType().equals(OrderType.DELIVERY) && o2.getOrderType().equals(OrderType.PICKUP)) {
                return o1.getDeliveryDate().compareTo(o2.getReturnDate());
            }
            //if the left is pickup and the other is delivery
            return o1.getReturnDate().compareTo(o2.getDeliveryDate());
        }).toList();
        return orderedList;
    }

    public MessageResponseDTO assignEmployee(AssignToEmployeeDTO assignment) {
        Optional<OrderEntity> order = orderRepository.findById(assignment.getOrderId());
        if (order.isEmpty()) {
            return new MessageResponseDTO(501, "Order not found");
        }
        Optional<UserEntity> employee = userRepository.findById(assignment.getEmployeeId());
        if (employee.isEmpty() || employee.get().getRole() != RoleEnum.EMPLOYEE) {
            return new MessageResponseDTO(501, "Employee not found");
        }
        //check if the assignment is already made, overwrite it
        Optional<OrderAssignmentEntity> assignmentEntity = assignmentRepository.findByOrderAndOrderType(order.get(), assignment.getOrderType());
        if (assignmentEntity.isPresent()) {
            assignmentEntity.get().setEmployee(employee.get());
            assignmentRepository.save(assignmentEntity.get());
        } else {
            OrderAssignmentEntity newAssignment = new OrderAssignmentEntity();
            newAssignment.setOrder(order.get());
            newAssignment.setEmployee(employee.get());
            newAssignment.setOrderType(assignment.getOrderType());

            order.get().getAssignments().add(newAssignment);
            employee.get().getAssignments().add(newAssignment);

            assignmentRepository.save(newAssignment);
        }
        orderRepository.save(order.get());
        userRepository.save(employee.get());
        return new MessageResponseDTO(200, String.format("Employee %s assigned to order %s", employee.get().getEmail(), order.get().getId()));
    }
}