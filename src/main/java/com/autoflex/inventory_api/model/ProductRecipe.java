package com.autoflex.inventory_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="product_recipes")

public class ProductRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(name = "quantity_needed", nullable = false)
    private Integer quantityNeeded;
}
