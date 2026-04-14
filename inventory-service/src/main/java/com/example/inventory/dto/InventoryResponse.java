package com.example.inventory.dto;

import java.math.BigDecimal;

public record InventoryResponse(
        String id,
        String productCode,
        String name,
        Integer availableQuantity,
        BigDecimal price
) {
}
