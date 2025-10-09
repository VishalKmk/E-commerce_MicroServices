package com.ecommerce.orders.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.orders.dto.ProductResponse;

@FeignClient(name = "demo", path = "/products", fallback = ProductClientFallback.class // Add fallback
)
public interface ProductClient {

    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);
}