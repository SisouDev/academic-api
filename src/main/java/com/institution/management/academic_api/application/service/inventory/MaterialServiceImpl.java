package com.institution.management.academic_api.application.service.inventory;

import com.institution.management.academic_api.application.dto.inventory.CreateMaterialRequestDto;
import com.institution.management.academic_api.application.dto.inventory.MaterialDetailsDto;
import com.institution.management.academic_api.application.dto.inventory.UpdateMaterialRequestDto;
import com.institution.management.academic_api.application.mapper.simple.inventory.MaterialMapper;
import com.institution.management.academic_api.application.notifiers.inventory.MaterialNotifier;
import com.institution.management.academic_api.domain.factory.inventory.MaterialFactory;
import com.institution.management.academic_api.domain.model.entities.inventory.Material;
import com.institution.management.academic_api.domain.repository.inventory.MaterialRepository;
import com.institution.management.academic_api.domain.service.inventory.MaterialService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialFactory materialFactory;
    private final MaterialMapper materialMapper;
    private final MaterialNotifier materialNotifier;

    @Override
    @Transactional
    @LogActivity("Cadastrou um novo material no catálogo.")
    public MaterialDetailsDto create(CreateMaterialRequestDto dto) {
        materialRepository.findByNameIgnoreCase(dto.name()).ifPresent(m -> {
            throw new EntityExistsException("There is already a material with the name: " + dto.name());
        });

        Material newMaterial = materialFactory.create(dto);
        Material savedMaterial = materialRepository.save(newMaterial);

        materialNotifier.notifyAdminOfNewMaterial(savedMaterial);

        return materialMapper.toDetailsDto(savedMaterial);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um material do catálogo.")
    public MaterialDetailsDto update(Long id, UpdateMaterialRequestDto dto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));

        materialMapper.updateFromDto(dto, material);
        return materialMapper.toDetailsDto(material);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um material do catálogo.")
    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new EntityNotFoundException("Material not found with id: " + id);
        }
        materialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialDetailsDto findById(Long id) {
        return materialRepository.findById(id)
                .map(materialMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDetailsDto> findAll() {
        return materialRepository.findAll().stream()
                .map(materialMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}
