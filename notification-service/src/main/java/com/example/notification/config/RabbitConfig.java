package com.example.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "notification.events";
    public static final String RESERVED_QUEUE = "notification.inventory-reserved";
    public static final String REJECTED_QUEUE = "notification.inventory-rejected";

    @Bean
    DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Queue reservedQueue() {
        return new Queue(RESERVED_QUEUE, true);
    }

    @Bean
    Queue rejectedQueue() {
        return new Queue(REJECTED_QUEUE, true);
    }

    @Bean
    Binding reservedBinding(Queue reservedQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(reservedQueue).to(notificationExchange).with("inventory.reserved");
    }

    @Bean
    Binding rejectedBinding(Queue rejectedQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(rejectedQueue).to(notificationExchange).with("inventory.rejected");
    }
}
