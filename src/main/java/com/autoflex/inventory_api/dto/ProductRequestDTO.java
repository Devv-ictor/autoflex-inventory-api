package com.autoflex.inventory_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for receiving product creation requests.
 */
public record ProductRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório")
        String name,

        @NotNull(message = "O valor do produto é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal value,

        List<RecipeItemDTO> recipes
) {}
