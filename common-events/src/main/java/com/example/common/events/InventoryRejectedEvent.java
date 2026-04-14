package com.example.common.events;

public record InventoryRejectedEvent(
        Long orderId,
        String productCode,
        Integer requestedQuantity,
        String reason,
        String customerEmail
) {
}
