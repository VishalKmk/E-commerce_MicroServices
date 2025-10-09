package com.ecommerce.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.orders.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
