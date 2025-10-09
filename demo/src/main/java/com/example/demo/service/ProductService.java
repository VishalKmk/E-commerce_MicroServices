package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found!"));
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            if (updatedProduct.getProductName() != null && !updatedProduct.getProductName().isEmpty()) {
                product.setProductName(updatedProduct.getProductName());
            }
            if (updatedProduct.getPrice() != null) {
                product.setPrice(updatedProduct.getPrice());
            }
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public String deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found!");
        }
        productRepository.deleteById(id);
        return "Product has been deleted";
    }
}
