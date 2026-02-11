package com.autoflex.inventory_api.dto;

import java.math.BigDecimal;

public record ProductionSuggestionDTO(
        String productName,
        Long productId,
        int quantityToProduce,
        BigDecimal totalValue
) {}
