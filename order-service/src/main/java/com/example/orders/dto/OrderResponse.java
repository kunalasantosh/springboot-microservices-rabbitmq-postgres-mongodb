package com.example.orders.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OrderResponse(
        Long id,
        String productCode,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String customerEmail,
        String status,
        OffsetDateTime createdAt
) {
}
