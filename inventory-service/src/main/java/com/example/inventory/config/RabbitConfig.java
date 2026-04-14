package com.example.inventory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_EXCHANGE = "order.events";
    public static final String NOTIFICATION_EXCHANGE = "notification.events";
    public static final String ORDER_CREATED_QUEUE = "inventory.order-created";
    public static final String NOTIFY_RESERVED_QUEUE = "notification.inventory-reserved";
    public static final String NOTIFY_REJECTED_QUEUE = "notification.inventory-rejected";
    public static final String INVENTORY_RESERVED_KEY = "inventory.reserved";
    public static final String INVENTORY_REJECTED_KEY = "inventory.rejected";

    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    Queue notifyReservedQueue() {
        return new Queue(NOTIFY_RESERVED_QUEUE, true);
    }

    @Bean
    Queue notifyRejectedQueue() {
        return new Queue(NOTIFY_REJECTED_QUEUE, true);
    }

    @Bean
    Binding orderCreatedBinding(Queue orderCreatedQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(orderExchange).with("order.created");
    }

    @Bean
    Binding notifyReservedBinding(Queue notifyReservedQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notifyReservedQueue).to(notificationExchange).with(INVENTORY_RESERVED_KEY);
    }

    @Bean
    Binding notifyRejectedBinding(Queue notifyRejectedQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notifyRejectedQueue).to(notificationExchange).with(INVENTORY_REJECTED_KEY);
    }
}
