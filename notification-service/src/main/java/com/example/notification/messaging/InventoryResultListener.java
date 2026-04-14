package com.example.notification.messaging;

import com.example.common.events.InventoryRejectedEvent;
import com.example.common.events.InventoryReservedEvent;
import com.example.notification.config.RabbitConfig;
import com.example.notification.service.NotificationLogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryResultListener {

    private final NotificationLogService notificationLogService;

    public InventoryResultListener(NotificationLogService notificationLogService) {
        this.notificationLogService = notificationLogService;
    }

    @RabbitListener(queues = RabbitConfig.RESERVED_QUEUE)
    public void onReserved(InventoryReservedEvent event) {
        notificationLogService.handleReserved(event);
    }

    @RabbitListener(queues = RabbitConfig.REJECTED_QUEUE)
    public void onRejected(InventoryRejectedEvent event) {
        notificationLogService.handleRejected(event);
    }
}
