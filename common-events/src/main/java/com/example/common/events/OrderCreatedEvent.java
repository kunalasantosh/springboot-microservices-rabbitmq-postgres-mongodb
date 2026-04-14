package com.example.common.events;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        String productCode,
        Integer quantity,
        BigDecimal totalPrice,
        String customerEmail
) {
}
