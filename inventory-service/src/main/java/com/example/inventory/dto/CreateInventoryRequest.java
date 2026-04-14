package com.example.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateInventoryRequest(
        @NotBlank String productCode,
        @NotBlank String name,
        @NotNull @Min(0) Integer availableQuantity,
        @NotNull @DecimalMin("0.01") BigDecimal price
) {
}
