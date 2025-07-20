package com.institution.management.academic_api.application.service.common;

import com.institution.management.academic_api.application.dto.common.CreateSalaryStructureRequestDto;
import com.institution.management.academic_api.application.dto.common.SalaryStructureDto;
import com.institution.management.academic_api.application.dto.common.UpdateSalaryStructureRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.SalaryStructureMapper;
import com.institution.management.academic_api.domain.factory.common.SalaryStructureFactory;
import com.institution.management.academic_api.domain.model.entities.common.SalaryStructure;
import com.institution.management.academic_api.domain.repository.common.SalaryStructureRepository;
import com.institution.management.academic_api.domain.service.common.SalaryStructureService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalaryStructureServiceImpl implements SalaryStructureService {

    private final SalaryStructureRepository repository;
    private final SalaryStructureFactory factory;
    private final SalaryStructureMapper mapper;

    @Override
    @Transactional
    public SalaryStructureDto create(CreateSalaryStructureRequestDto dto) {
        SalaryStructure newStructure = factory.create(dto);
        SalaryStructure savedStructure = repository.save(newStructure);
        return mapper.toDto(savedStructure);
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryStructureDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Estrutura salarial não encontrada com o ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryStructureDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public SalaryStructureDto update(Long id, UpdateSalaryStructureRequestDto dto) {
        SalaryStructure structure = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estrutura salarial não encontrada com o ID: " + id));

        mapper.updateFromDto(dto, structure);

        SalaryStructure updatedStructure = repository.save(structure);
        return mapper.toDto(updatedStructure);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Estrutura salarial não encontrada com o ID: " + id);
        }
        repository.deleteById(id);
    }
}