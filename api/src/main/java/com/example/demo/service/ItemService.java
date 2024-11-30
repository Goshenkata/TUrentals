package com.example.demo.service;

import com.example.demo.dto.enums.CategoryEnum;
import com.example.demo.dto.request.ItemCreateDTO;
import com.example.demo.dto.response.ItemDTO;
import com.example.demo.model.CategoryEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.availability.OrderLineEntity;
import com.example.demo.model.availability.WarehouseLineEntity;
import com.example.demo.repository.*;
import jakarta.validation.constraints.Future;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ModelMapper mapper;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;


    public Long createItem(ItemCreateDTO itemCreateDTO) {
        ItemEntity entity = mapper.map(itemCreateDTO, ItemEntity.class);
        try {
            itemRepository.save(entity);
        } catch (Exception e) {
            return -1L;
        }
        return entity.getId();
    }

    public void seedItems() {
        if (itemRepository.count() > 0) {
            return; // Skip initialization if data already exists
        }
        createItem("Wooden Chair", "Elegant wooden chair for events.", BigDecimal.valueOf(10), "https://img.freepik.com/free-vector/vintage-armchair-vector-illustration-remixed-from-artwork-by-donald-harding_53876-115493.jpg?t=st=1732544311~exp=1732547911~hmac=0d80802e8aadd15bb53b6bbc3bc7e50bda56402ccf178cd7e2ffd959c4d8e679&w=740", 50, CategoryEnum.FURNITURE);
        createItem("Round Table", "Sturdy round table.", BigDecimal.valueOf(25), "https://img.freepik.com/free-vector/vintage-armchair-vector-illustration-remixed-from-artwork-by-donald-harding_53876-115493.jpg?t=st=1732544311~exp=1732547911~hmac=0d80802e8aadd15bb53b6bbc3bc7e50bda56402ccf178cd7e2ffd959c4d8e679&w=740", 20, CategoryEnum.FURNITURE);
        createItem("Leather Sofa", "Comfortable leather sofa.", BigDecimal.valueOf(50), "https://5.imimg.com/data5/SELLER/Default/2021/12/AR/OK/DV/14537345/5-seater-leather-sofa-set-500x500.jpg", 5, CategoryEnum.FURNITURE);

        // DECOR
        createItem("Wedding Backdrop", "Beautiful wedding backdrop.", BigDecimal.valueOf(100), "https://i.pinimg.com/736x/e5/00/59/e50059b1ad08243cb4140ac0ec60c4be.jpg", 3, CategoryEnum.DECOR);

        // LIGHTING
        createItem("Chandelier", "Crystal chandelier for elegance.", BigDecimal.valueOf(50), "https://m.media-amazon.com/images/I/71cKbryzK-L.jpg", 2, CategoryEnum.LIGHTING);
        createItem("LED Strip Lights", "Colorful LED strip lights.", BigDecimal.valueOf(10), "https://m.media-amazon.com/images/I/81-E3KFC9PL._AC_UF894,1000_QL80_.jpg", 25, CategoryEnum.LIGHTING);

        // TABLEWARE
        createItem("10x Dinner Plate", "Elegant white dinner plate.", BigDecimal.valueOf(5), "https://naimascatering.com/wp-content/uploads/2019/10/PR1113_1.jpg", 100, CategoryEnum.TABLEWARE);
        createItem("10x Wine Glass", "Crystal-clear wine glass.", BigDecimal.valueOf(5), "https://www.seriouseats.com/thmb/xnQObuVXoS4rVwmuQ0DlycZckB4=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/sea-primary-glassware-rkilgore-980-bddd4702ec1046cb80204d37a4003cc1.jpeg", 150, CategoryEnum.TABLEWARE);

        // LINENS
        createItem("Tablecloth", "White tablecloth for any table.", BigDecimal.valueOf(5), "https://m.media-amazon.com/images/I/81pAO-XuplL.jpg", 50, CategoryEnum.LINENS);
        createItem("Chair Cover", "Stretchable chair cover.", BigDecimal.valueOf(3), "https://nishatdecor.com/cdn/shop/files/bubble_chairs5.jpg?v=1722441393", 100, CategoryEnum.LINENS);

        // SHELTERS
        createItem("Canopy Tent", "Large event tent.", BigDecimal.valueOf(120), "https://impactcanopy.ca/cdn/shop/products/B07JH4XX43-19_54e36abc-cb2b-44dc-a497-aa345e0c3c8a_800x.jpg?v=1613505386", 3, CategoryEnum.SHELTERS);
        createItem("Sun Umbrella", "Outdoor umbrella for shade.", BigDecimal.valueOf(30), "https://image.made-in-china.com/202f0j00WYvbnMkPhUpD/Beach-Sun-Protection-Waterproof-Garden-Umbrella-Parasol-for-Outdoor-Event-From-Umbrella.webp", 15, CategoryEnum.SHELTERS);

        // STAGING
        createItem("Dance Floor", "Portable dance floor for events.", BigDecimal.valueOf(150), "https://example.com/images/dance-floor.jpg", 2, CategoryEnum.STAGING);
        createItem("Stage Podium", "Wooden podium for speeches.", BigDecimal.valueOf(75), "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQXTJCpa4kqJs4PBJhhMM-9aVO7ed-Q8vprZw&s", 5, CategoryEnum.STAGING);

        // CATERING
        createItem("Chafing Dish", "Food warmer for catering.", BigDecimal.valueOf(20), "https://www.truercatering.com/wp-content/uploads/2024/06/image-52.jpg", 10, CategoryEnum.CATERING);
        createItem("Drink Dispenser", "Large drink dispenser.", BigDecimal.valueOf(15), "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ64Qcy16PhqQYVlZ3DN_x3F-5dsh2g99e-jA&s", 8, CategoryEnum.CATERING);

        // COOKING
        createItem("Gas Grill", "Portable gas grill for outdoor cooking.", BigDecimal.valueOf(50), "https://cdn2.gofoodservice.com/images/products/orig/35688/53835.jpg", 5, CategoryEnum.COOKING);
        createItem("Fryer", "Deep fryer for cooking.", BigDecimal.valueOf(30), "https://simply-bbq.com/wp-content/uploads/2022/12/Deep-Fryer-Portable.jpg", 3, CategoryEnum.COOKING);

        // AUDIO
        createItem("PA System", "Professional PA sound system.", BigDecimal.valueOf(100), "https://destiny-files.com/image/webp_large/SSD5871_3.webp", 5, CategoryEnum.AUDIO);
        createItem("DJ Mixer", "DJ mixer for events.", BigDecimal.valueOf(60), "https://images-cdn.ubuy.co.in/65e118514cfdfe6e4c7d59bb-numark-mixtrack-pro-dj-controller-with.jpg", 3, CategoryEnum.AUDIO);
    }

    public void createItem(String name, String description, BigDecimal pricePerDay, String imageUrl, int initialQuantity, CategoryEnum category) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByName(category.toString());
        if (categoryEntity.isEmpty()) {
            categoryRepository.save(new CategoryEntity(category.toString()));
        }
        ItemEntity item = new ItemEntity();
        item.setName(name);
        item.setDescription(description);
        item.setPricePerDay(pricePerDay);
        item.setImageUrl(imageUrl);
        item.setCategory(categoryEntity.get());
        item.setCurrentQuantity(initialQuantity);

        itemRepository.save(item);
        //save the line to the warehouse
        WarehouseLineEntity line = new WarehouseLineEntity();
        line.setItem(item);
        line.setQuantity(initialQuantity);
        warehouseRepository.save(line);
    }


    public List<ItemDTO> search() {
        List<ItemEntity> items = itemRepository.findAll();
        return items.stream().map(item -> mapper.map(item, ItemDTO.class)).toList();
    }

    public int checkAvailabilityAtDateRange(Long itemId, @Future LocalDate devileryDate, @Future LocalDate returnDate) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow();
        int quantity = getQuantityAtDate(item, devileryDate);
        item.setCurrentQuantity(quantity);
        //check the quantity from the delivery date to the return date
        for(devileryDate = devileryDate.plusDays(1); devileryDate.isBefore(returnDate) || devileryDate.isEqual(returnDate); devileryDate = devileryDate.plusDays(1)) {
            int quantityAtDate = getQuantityAtDate(item, devileryDate);
            quantity = Math.min(quantity, quantityAtDate);
        }

        return quantity;
    }

    private int getQuantityAtDate(ItemEntity item, LocalDate devileryDate) {
        List<OrderEntity> deliveries = orderRepository.findAllDeliveriesBetweenDates(LocalDate.now(), devileryDate);
        List<OrderEntity> pickups = orderRepository.findAllPickupsBetweenDates(LocalDate.now(), devileryDate);
        int deliveryQuantity = deliveries.stream()
                .mapToInt(order ->
                        order.getLines().stream()
                                .filter(line -> line.getItem().getId().equals(item.getId()))
                                .mapToInt(OrderLineEntity::getQuantity).sum())
                .sum();
        int pickupQuantity = deliveries.stream()
                .mapToInt(order ->
                        order.getLines().stream()
                                .filter(line -> line.getItem().getId().equals(item.getId()))
                                .mapToInt(OrderLineEntity::getQuantity).sum())
                .sum();
        return item.getCurrentQuantity() - deliveryQuantity + pickupQuantity;
    }
}
