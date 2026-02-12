package com.autoflex.inventory_api.service;

import com.autoflex.inventory_api.dto.ProductionSuggestionDTO;
import com.autoflex.inventory_api.model.Product;
import com.autoflex.inventory_api.model.ProductRecipe;
import com.autoflex.inventory_api.model.RawMaterial;
import com.autoflex.inventory_api.repository.ProductRepository;
import com.autoflex.inventory_api.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductService productService;

    private RawMaterial wood;
    private RawMaterial metal;

    @BeforeEach
    void setUp() {
        wood = new RawMaterial();
        wood.setId(1L);
        wood.setName("Wood");
        wood.setQuantity(100);

        metal = new RawMaterial();
        metal.setId(2L);
        metal.setName("Metal");
        metal.setQuantity(50);
    }

    @Test
    void testCalculateProductionPlan_PrioritizesHigherValue() {
        // Product A: Value 50, needs 10 wood
        Product productA = new Product();
        productA.setId(1L);
        productA.setName("Product A");
        productA.setValue(new BigDecimal("50.00"));
        
        ProductRecipe recipeA = new ProductRecipe();
        recipeA.setRawMaterial(wood);
        recipeA.setQuantityNeeded(10);
        productA.getRecipes().add(recipeA);

        // Product B: Value 100, needs 20 wood
        Product productB = new Product();
        productB.setId(2L);
        productB.setName("Product B");
        productB.setValue(new BigDecimal("100.00"));

        ProductRecipe recipeB = new ProductRecipe();
        recipeB.setRawMaterial(wood);
        recipeB.setQuantityNeeded(20);
        productB.getRecipes().add(recipeB);

        when(productRepository.findAll()).thenReturn(List.of(productA, productB));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(wood));

        List<ProductionSuggestionDTO> suggestions = productService.calculateProductionPlan();

        // 100 wood available. 
        // Product B is prioritized (Value 100). Max 100/20 = 5 units.
        // Wood remaining: 100 - (5 * 20) = 0.
        // Product A: 0 wood left. 0 units.

        assertEquals(1, suggestions.size());
        assertEquals("Product B", suggestions.get(0).productName());
        assertEquals(5, suggestions.get(0).quantityToProduce());
    }

    @Test
    void testCalculateProductionPlan_MultipleMaterials() {
        // Product: Value 100, needs 10 wood and 10 metal
        Product product = new Product();
        product.setId(1L);
        product.setName("Chair");
        product.setValue(new BigDecimal("100.00"));

        ProductRecipe r1 = new ProductRecipe();
        r1.setRawMaterial(wood);
        r1.setQuantityNeeded(10);

        ProductRecipe r2 = new ProductRecipe();
        r2.setRawMaterial(metal);
        r2.setQuantityNeeded(10);

        product.getRecipes().addAll(List.of(r1, r2));

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(wood, metal));

        // wood: 100, metal: 50
        // Limit is metal: 50 / 10 = 5 units
        
        List<ProductionSuggestionDTO> suggestions = productService.calculateProductionPlan();

        assertEquals(1, suggestions.size());
        assertEquals(5, suggestions.get(0).quantityToProduce());
    }
}
