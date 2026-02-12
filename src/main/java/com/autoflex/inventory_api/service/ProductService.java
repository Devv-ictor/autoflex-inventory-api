package com.autoflex.inventory_api.service;

import com.autoflex.inventory_api.dto.ProductRequestDTO;
import com.autoflex.inventory_api.dto.ProductionSuggestionDTO;
import com.autoflex.inventory_api.exception.ResourceNotFoundException;
import com.autoflex.inventory_api.model.Product;
import com.autoflex.inventory_api.model.ProductRecipe;
import com.autoflex.inventory_api.model.RawMaterial;
import com.autoflex.inventory_api.repository.ProductRepository;
import com.autoflex.inventory_api.repository.RawMaterialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    /**
     * Lists all products.
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Creates a new product and associates its recipe items.
     */
    @Transactional
    public Product create(ProductRequestDTO dto) {
        // 1. Create the Product entity from the DTO
        Product product = new Product();
        product.setName(dto.name());
        product.setValue(dto.value());

        // 2. Map the recipe items from the DTO to the ProductRecipe entity
        List<ProductRecipe> recipes = new ArrayList<>();

        if (dto.recipes() != null) {
            recipes = dto.recipes().stream().map(itemDto -> {
                RawMaterial rm = rawMaterialRepository.findById(itemDto.rawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Raw Material not found with ID: " + itemDto.rawMaterialId()));

                ProductRecipe recipe = new ProductRecipe();
                recipe.setProduct(product);
                recipe.setRawMaterial(rm);
                recipe.setQuantityNeeded(itemDto.quantityNeeded());

                return recipe;
            }).collect(Collectors.toList());
        }
        // 3. Set the recipes to the product
        product.setRecipes(recipes);

        // 4. Save the product (CascadeType.ALL ensures recipes are saved too)
        return productRepository.save(product);
    }

    /**
     * Calculates an optimized production plan based on available stock.
     * Higher value products are prioritized to maximize potential revenue.
     */
    @Transactional
    public List<ProductionSuggestionDTO> calculateProductionPlan() {
        // 1. Fetch all products and create a mutable list for sorting
        List<Product> products = new ArrayList<>(productRepository.findAll());

        // 2. Sort by HIGHEST value (Prioritization requirement)
        products.sort((p1, p2) -> p2.getValue().compareTo(p1.getValue()));

        // 3. Map current raw material stock for quick lookup and simulation
        Map<Long, Integer> stockMap = new HashMap<>();
        rawMaterialRepository.findAll().forEach(rm -> stockMap.put(rm.getId(), rm.getQuantity()));

        List<ProductionSuggestionDTO> suggestions = new ArrayList<>();

        // 4. Iterate over products to simulate production
        for (Product product : products) {
            if (product.getRecipes().isEmpty()) continue;

            int maxProducible = Integer.MAX_VALUE;
            boolean hasValidRecipe = false;

            // Find the limiting ingredient for this product
            for (ProductRecipe recipe : product.getRecipes()) {
                Long materialId = recipe.getRawMaterial().getId();
                int needed = recipe.getQuantityNeeded();
                if (needed <= 0) continue;
                hasValidRecipe = true;
                int available = stockMap.getOrDefault(materialId, 0);

                int possible = available / needed;

                if (possible < maxProducible) {
                    maxProducible = possible;
                }
            }

            // If production is possible for at least 1 unit
            if (hasValidRecipe && maxProducible > 0 && maxProducible != Integer.MAX_VALUE) {
                // Deduct from simulated stock
                for (ProductRecipe recipe : product.getRecipes()) {
                    Long materialId = recipe.getRawMaterial().getId();
                    int totalNeeded = recipe.getQuantityNeeded() * maxProducible;

                    stockMap.put(materialId, stockMap.get(materialId) - totalNeeded);
                }

                BigDecimal totalValue = product.getValue().multiply(BigDecimal.valueOf(maxProducible));
                suggestions.add(new ProductionSuggestionDTO(
                        product.getName(),
                        product.getId(),
                        maxProducible,
                        totalValue
                ));
            }
        }

        return suggestions;
    }
}
