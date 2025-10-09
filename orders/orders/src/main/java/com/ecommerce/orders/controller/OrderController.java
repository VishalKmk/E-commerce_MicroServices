package com.ecommerce.orders.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.orders.model.Order;
import com.ecommerce.orders.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order placedOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(placedOrder); // Returns HTTP 200 OK with the order details
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrder() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
