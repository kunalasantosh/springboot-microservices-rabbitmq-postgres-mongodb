package com.example.orders.service;

import com.example.common.events.OrderCreatedEvent;
import com.example.orders.domain.OrderEntity;
import com.example.orders.dto.CreateOrderRequest;
import com.example.orders.dto.OrderResponse;
import com.example.orders.messaging.OrderEventPublisher;
import com.example.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity();
        order.setProductCode(request.productCode());
        order.setQuantity(request.quantity());
        order.setUnitPrice(request.unitPrice());
        order.setTotalPrice(request.unitPrice().multiply(BigDecimal.valueOf(request.quantity())));
        order.setCustomerEmail(request.customerEmail());
        order.setStatus("CREATED");

        OrderEntity saved = orderRepository.save(order);

        eventPublisher.publish(new OrderCreatedEvent(
                saved.getId(),
                saved.getProductCode(),
                saved.getQuantity(),
                saved.getTotalPrice(),
                saved.getCustomerEmail()
        ));

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(OrderEntity entity) {
        return new OrderResponse(
                entity.getId(),
                entity.getProductCode(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getTotalPrice(),
                entity.getCustomerEmail(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
