package com.autoflex.inventory_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecipeItemDTO(
        @NotNull(message = "O ID da matéria prima é obrigatório")
        Long rawMaterialId,

        @NotNull(message = "A quantidade necessária é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantityNeeded
) {}
