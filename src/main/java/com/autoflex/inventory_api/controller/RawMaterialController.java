package com.autoflex.inventory_api.controller;

import com.autoflex.inventory_api.model.RawMaterial;
import com.autoflex.inventory_api.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/raw-materials")
public class RawMaterialController {

    @Autowired
    private RawMaterialRepository repository;

    @GetMapping
    public List<RawMaterial> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity
    }
}
