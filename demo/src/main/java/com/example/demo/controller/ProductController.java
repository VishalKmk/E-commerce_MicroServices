package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no products exist
        }
        return ResponseEntity.ok(products); // 200 OK with product list
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = Optional.ofNullable(productService.getProductById(id));
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found if missing
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.status(201).body(savedProduct); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product updated = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(updated); // 200 OK after update
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        String result = productService.deleteProductById(id);
        return ResponseEntity.ok(result); // 200 OK with confirmation message
    }
}
