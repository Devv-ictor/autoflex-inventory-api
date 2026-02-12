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
@CrossOrigin(origins = "*")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    // GET - Listar tudo
    @GetMapping
    public ResponseEntity<List<RawMaterial>> getAll() {
        return ResponseEntity.ok(rawMaterialService.listAll());
    }

    // GET - Buscar um
    @GetMapping("/{id}")
    public ResponseEntity<RawMaterial> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rawMaterialService.findById(id));
    }

    // POST - Criar
    @PostMapping
    public ResponseEntity<RawMaterial> create(@Valid @RequestBody RawMaterial rawMaterial) {
        return ResponseEntity.ok(rawMaterialService.create(rawMaterial));
    }

    // PUT - Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<RawMaterial> update(@PathVariable Long id, @Valid @RequestBody RawMaterial rawMaterial) {
        return ResponseEntity.ok(rawMaterialService.update(id, rawMaterial));
    }

    // DELETE - Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}