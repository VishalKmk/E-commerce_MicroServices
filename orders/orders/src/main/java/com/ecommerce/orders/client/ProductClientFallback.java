package com.ecommerce.orders.client;

import org.springframework.stereotype.Component;

import com.ecommerce.orders.dto.ProductResponse;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductResponse getProductById(Long id) {
        System.err.println("FALLBACK: Product Service is unavailable. Returning default response.");

        // Return a default response when Product Service is down
        ProductResponse fallbackResponse = new ProductResponse();
        fallbackResponse.setId(id);
        fallbackResponse.setProductName("Product Unavailable");
        fallbackResponse.setPrice(0.0);

        return fallbackResponse;
    }
}