package com.autoflex.inventory_api.controller;

import com.autoflex.inventory_api.dto.ProductRequestDTO;
import com.autoflex.inventory_api.dto.ProductionSuggestionDTO;
import com.autoflex.inventory_api.model.Product;
import com.autoflex.inventory_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows cross-origin requests for testing purposes
public class ProductController {

    private final ProductService productService;

    /**
     * Retrieves all products registered in the system.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    /**
     * Creates a new product with its recipe.
     */
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.create(dto));
    }

    /**
     * Suggests a production plan based on current stock levels and product values.
     */
    @GetMapping("/production-suggestions")
    public ResponseEntity<List<ProductionSuggestionDTO>> getProductionSuggestions() {
        return ResponseEntity.ok(productService.calculateProductionPlan());
    }
}