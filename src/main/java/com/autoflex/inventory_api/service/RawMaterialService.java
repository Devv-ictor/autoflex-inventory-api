package com.autoflex.inventory_api.service;

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

    // Listar todos os materiais
    public List<RawMaterial> listAll() {
        return rawMaterialRepository.findAll();
    }

    // Buscar por ID
    public RawMaterial findById(Long id) {
        return rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material not found"));
    }

    // Criar novo material
    public RawMaterial create(RawMaterial rawMaterialDetails) {
        return rawMaterialRepository.save(rawMaterialDetails);
    }

    // Atualizar material
    @Transactional
    public RawMaterial update(long id, RawMaterial rawMaterialDetails) {
        RawMaterial rawMaterial = findById(id);

        rawMaterial.setName(rawMaterialDetails.getName());
        rawMaterial.setQuantity(rawMaterialDetails.getQuantity());

        return rawMaterialRepository.save(rawMaterial);
    }

    // Deletar material
    public void delete(long id) {
        rawMaterialRepository.deleteById(id);
    }
}
