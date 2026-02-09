package com.autoflex.inventory_api.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequestDTO(
        String name,
        BigDecimal value,
        List<RecipeItemDTO> recipes
) {}
