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

    // Listar todos
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Criar Produto com receita
    @Transactional
    public Product create(ProductRequestDTO dto) {
        // 1. Cria a entidade Produto baseada no DTO
        Product product = new Product();
        product.setName(dto.name());
        product.setValue(dto.value());

        // 2. Cria a lista de receitas (relacionamento)
        List<ProductRecipe> recipes = new ArrayList<>();

        // Se houver receitas no DTO, convertemos para entidades
        if (dto.recipes() != null) {
            recipes = dto.recipes().stream().map(itemDto -> {
                RawMaterial rm = rawMaterialRepository.findById(itemDto.rawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Raw Material not found with ID: " + itemDto.rawMaterialId()));

                // Cria o item da receita
                ProductRecipe recipe = new ProductRecipe();
                recipe.setProduct(product);
                recipe.setRawMaterial(rm);
                recipe.setQuantityNeeded(itemDto.quantityNeeded());

                return recipe;
            }).collect(Collectors.toList());
        }
        // 3. Define as receitas no produto
        product.setRecipes(recipes);

        // 4. Salva o produto. O CascadeType.ALL vai salvar as receitas automaticamente
        return productRepository.save(product);
    }

    // ... métodos anteriores ...

    @Transactional
    public List<ProductionSuggestionDTO> calculateProductionPlan() {
        // 1. Buscar todos os produtos e criar uma lista mutável
        List<Product> products = new ArrayList<>(productRepository.findAll());

        // 2. Ordenar pelo MAIOR valor (Requisito de priorização)
        products.sort((p1, p2) -> p2.getValue().compareTo(p1.getValue()));

        // 3. Mapear o estoque atual de matérias primas para um Mapa
        Map<Long, Integer> stockMap = new HashMap<>();
        rawMaterialRepository.findAll().forEach(rm -> stockMap.put(rm.getId(), rm.getQuantity()));

        List<ProductionSuggestionDTO> suggestions = new ArrayList<>();

        // 4. Iterar sobre os produtos
        for (Product product : products) {
            if (product.getRecipes().isEmpty()) continue;

            int maxProducible = Integer.MAX_VALUE;
            boolean hasValidRecipe = false;

            // Encontrar o ingrediente limitante
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

            // Se for possível produzir pelo menos 1
            if (hasValidRecipe && maxProducible > 0 && maxProducible != Integer.MAX_VALUE) {
                // Subtrair do estoque simulado
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
