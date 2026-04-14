package com.example.notification.service;

import com.example.common.events.InventoryRejectedEvent;
import com.example.common.events.InventoryReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationLogService {

    private static final Logger log = LoggerFactory.getLogger(NotificationLogService.class);

    public void handleReserved(InventoryReservedEvent event) {
        log.info("Inventory reserved for orderId={}, productCode={}, quantity={}, customerEmail={}",
                event.orderId(), event.productCode(), event.quantity(), event.customerEmail());
    }

    public void handleRejected(InventoryRejectedEvent event) {
        log.warn("Inventory rejected for orderId={}, productCode={}, requestedQuantity={}, reason={}, customerEmail={}",
                event.orderId(), event.productCode(), event.requestedQuantity(), event.reason(), event.customerEmail());
    }
}
