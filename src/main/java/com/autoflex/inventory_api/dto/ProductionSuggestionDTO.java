package com.autoflex.inventory_api.dto;

import java.math.BigDecimal;

/**
 * DTO representing a suggestion for production, including total potential revenue.
 */
public record ProductionSuggestionDTO(
        String productName,
        Long productId,
        int quantityToProduce,
        BigDecimal totalValue
) {}
