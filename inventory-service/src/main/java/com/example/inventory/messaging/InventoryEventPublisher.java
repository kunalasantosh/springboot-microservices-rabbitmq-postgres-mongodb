package com.example.inventory.messaging;

import com.example.common.events.InventoryRejectedEvent;
import com.example.common.events.InventoryReservedEvent;
import com.example.inventory.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public InventoryEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishReserved(InventoryReservedEvent event) {
        rabbitTemplate.convertAndSend(RabbitConfig.NOTIFICATION_EXCHANGE, RabbitConfig.INVENTORY_RESERVED_KEY, event);
    }

    public void publishRejected(InventoryRejectedEvent event) {
        rabbitTemplate.convertAndSend(RabbitConfig.NOTIFICATION_EXCHANGE, RabbitConfig.INVENTORY_REJECTED_KEY, event);
    }
}
