package com.autoflex.inventory_api.service;

import com.autoflex.inventory_api.dto.ProductRequestDTO;
import com.autoflex.inventory_api.dto.RecipeItemDTO;
import com.autoflex.inventory_api.model.Product;
import com.autoflex.inventory_api.model.ProductRecipe;
import com.autoflex.inventory_api.model.RawMaterial;
import com.autoflex.inventory_api.repository.ProductRepository;
import com.autoflex.inventory_api.repository.RawMaterialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    // Listar todos
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Criar Produto com receita
    @Transactional
    
}
