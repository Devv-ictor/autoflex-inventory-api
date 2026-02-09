package com.autoflex.inventory_api.dto;

public record RecipeItemDTO(
        Long rawMaterialId,
        Integer quantityNeeded
) {}
