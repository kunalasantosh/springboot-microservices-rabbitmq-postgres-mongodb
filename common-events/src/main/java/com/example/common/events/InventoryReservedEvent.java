package com.example.common.events;

public record InventoryReservedEvent(
        Long orderId,
        String productCode,
        Integer quantity,
        String status,
        String customerEmail
) {
}
