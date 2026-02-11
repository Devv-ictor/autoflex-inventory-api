package com.autoflex.inventory_api.controller;

import com.autoflex.inventory_api.dto.ProductRequestDTO;
import com.autoflex.inventory_api.dto.ProductionSuggestionDTO;
import com.autoflex.inventory_api.model.Product;
import com.autoflex.inventory_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.create(dto));
    }

    @GetMapping("/production-suggestions")
    public ResponseEntity<List<ProductionSuggestionDTO>> getProductionSuggestions() {
        return ResponseEntity.ok(productService.calculateProductionPlan());
    }
}