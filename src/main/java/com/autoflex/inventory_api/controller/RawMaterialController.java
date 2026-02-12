package com.autoflex.inventory_api.controller;

import com.autoflex.inventory_api.model.RawMaterial;
import com.autoflex.inventory_api.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows cross-origin requests for testing purposes
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    /**
     * Lists all raw materials.
     */
    @GetMapping
    public ResponseEntity<List<RawMaterial>> getAll() {
        return ResponseEntity.ok(rawMaterialService.listAll());
    }

    /**
     * Finds a specific raw material by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RawMaterial> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rawMaterialService.findById(id));
    }

    /**
     * Registers a new raw material.
     */
    @PostMapping
    public ResponseEntity<RawMaterial> create(@Valid @RequestBody RawMaterial rawMaterial) {
        return ResponseEntity.ok(rawMaterialService.create(rawMaterial));
    }

    /**
     * Updates an existing raw material.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RawMaterial> update(@PathVariable Long id, @Valid @RequestBody RawMaterial rawMaterial) {
        return ResponseEntity.ok(rawMaterialService.update(id, rawMaterial));
    }

    /**
     * Deletes a raw material by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}