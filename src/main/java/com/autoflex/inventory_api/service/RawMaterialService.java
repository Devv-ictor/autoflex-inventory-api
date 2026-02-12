package com.autoflex.inventory_api.service;

import com.autoflex.inventory_api.exception.ResourceNotFoundException;
import com.autoflex.inventory_api.model.RawMaterial;
import com.autoflex.inventory_api.repository.RawMaterialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {
    private final RawMaterialRepository rawMaterialRepository;

    /**
     * Lists all raw materials in the database.
     */
    public List<RawMaterial> listAll() {
        return rawMaterialRepository.findAll();
    }

    /**
     * Finds a raw material by its ID. Throws ResourceNotFoundException if not found.
     */
    public RawMaterial findById(Long id) {
        return rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with ID: " + id));
    }

    /**
     * Persists a new raw material.
     */
    public RawMaterial create(RawMaterial rawMaterialDetails) {
        return rawMaterialRepository.save(rawMaterialDetails);
    }

    /**
     * Updates an existing raw material.
     */
    @Transactional
    public RawMaterial update(long id, RawMaterial rawMaterialDetails) {
        RawMaterial rawMaterial = findById(id);

        rawMaterial.setName(rawMaterialDetails.getName());
        rawMaterial.setQuantity(rawMaterialDetails.getQuantity());

        return rawMaterialRepository.save(rawMaterial);
    }

    /**
     * Removes a raw material from the database by ID.
     */
    public void delete(long id) {
        rawMaterialRepository.deleteById(id);
    }
}
