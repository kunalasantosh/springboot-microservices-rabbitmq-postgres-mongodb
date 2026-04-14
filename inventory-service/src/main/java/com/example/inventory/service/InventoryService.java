package com.example.inventory.service;

import com.example.common.events.InventoryRejectedEvent;
import com.example.common.events.InventoryReservedEvent;
import com.example.common.events.OrderCreatedEvent;
import com.example.inventory.domain.InventoryItem;
import com.example.inventory.dto.CreateInventoryRequest;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.messaging.InventoryEventPublisher;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryEventPublisher eventPublisher;

    public InventoryService(InventoryRepository inventoryRepository, InventoryEventPublisher eventPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.eventPublisher = eventPublisher;
    }

    public InventoryResponse create(CreateInventoryRequest request) {
        InventoryItem item = new InventoryItem();
        item.setProductCode(request.productCode());
        item.setName(request.name());
        item.setAvailableQuantity(request.availableQuantity());
        item.setPrice(request.price());
        return toResponse(inventoryRepository.save(item));
    }

    public List<InventoryResponse> list() {
        return inventoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public void reserveStock(OrderCreatedEvent event) {
        inventoryRepository.findByProductCode(event.productCode()).ifPresentOrElse(item -> {
            if (item.getAvailableQuantity() >= event.quantity()) {
                item.setAvailableQuantity(item.getAvailableQuantity() - event.quantity());
                inventoryRepository.save(item);
                eventPublisher.publishReserved(new InventoryReservedEvent(
                        event.orderId(),
                        event.productCode(),
                        event.quantity(),
                        "RESERVED",
                        event.customerEmail()
                ));
            } else {
                eventPublisher.publishRejected(new InventoryRejectedEvent(
                        event.orderId(),
                        event.productCode(),
                        event.quantity(),
                        "Insufficient inventory",
                        event.customerEmail()
                ));
            }
        }, () -> eventPublisher.publishRejected(new InventoryRejectedEvent(
                event.orderId(),
                event.productCode(),
                event.quantity(),
                "Product code not found",
                event.customerEmail()
        )));
    }

    private InventoryResponse toResponse(InventoryItem item) {
        return new InventoryResponse(
                item.getId(),
                item.getProductCode(),
                item.getName(),
                item.getAvailableQuantity(),
                item.getPrice()
        );
    }
}
