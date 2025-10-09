package com.ecommerce.orders.service;

import com.ecommerce.orders.client.ProductClient;
import com.ecommerce.orders.dto.ProductResponse;
import com.ecommerce.orders.model.Order;
import com.ecommerce.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    public Order placeOrder(Order order) {
        ProductResponse product = getProductById(order.getProductId());

        if (product == null) {
            throw new RuntimeException("Unable to fetch product details.");
        }

        if ("Product Unavailable".equals(product.getProductName())) {
            throw new RuntimeException("Product Service is currently unavailable. Try again later.");
        }

        if (product.getPrice() == null || product.getPrice() <= 0.0) {
            throw new RuntimeException("Invalid product price. Order cannot be placed.");
        }

        double totalPrice = product.getPrice() * order.getQuantity();
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    private ProductResponse getProductById(Long productId) {
        try {
            return productClient.getProductById(productId);
        } catch (Exception e) {
            System.err.println("Error fetching product via Feign: " + e.getMessage());
            return null;
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}