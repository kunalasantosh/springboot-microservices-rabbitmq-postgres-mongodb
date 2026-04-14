package com.example.inventory.repository;

import com.example.inventory.domain.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InventoryRepository extends MongoRepository<InventoryItem, String> {
    Optional<InventoryItem> findByProductCode(String productCode);
}
