package com.autoflex.inventory_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="raw_materials")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;
}