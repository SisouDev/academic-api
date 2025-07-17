package com.institution.management.academic_api.application.service.it;

import com.institution.management.academic_api.application.dto.it.AssetDetailsDto;
import com.institution.management.academic_api.application.dto.it.CreateAssetRequestDto;
import com.institution.management.academic_api.application.mapper.simple.it.AssetMapper;
import com.institution.management.academic_api.application.notifiers.it.AssetNotifier;
import com.institution.management.academic_api.domain.factory.it.AssetFactory;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.it.Asset;
import com.institution.management.academic_api.domain.model.entities.specification.AssetSpecification;
import com.institution.management.academic_api.domain.model.enums.it.AssetStatus;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.it.AssetRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.it.AssetService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;
    private final AssetFactory assetFactory;
    private final AssetMapper assetMapper;
    private final NotificationService notificationService;
    private final AssetNotifier assetNotifier;

    @Override
    @Transactional
    @LogActivity("Cadastrou um novo ativo de TI.")
    public AssetDetailsDto create(CreateAssetRequestDto dto) {
        assetRepository.findByAssetTag(dto.assetTag()).ifPresent(asset -> {
            throw new EntityExistsException("An asset already exists with the asset tag: " + dto.assetTag());
        });
        Asset newAsset = assetFactory.create(dto);
        Asset savedAsset = assetRepository.save(newAsset);
        return assetMapper.toDetailsDto(savedAsset);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou o status de um ativo de TI.")
    public AssetDetailsDto updateStatus(Long assetId, String status) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found with id: " + assetId));
        asset.setStatus(AssetStatus.valueOf(status.toUpperCase()));
        return assetMapper.toDetailsDto(asset);
    }

    @Override
    @Transactional
    @LogActivity("Alocou um ativo de TI a um funcionário.")
    public void assignAsset(Long assetId, Long employeeId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found with id: " + assetId));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        asset.setAssignedTo(employee);
        asset.setStatus(AssetStatus.IN_USE);

        assetNotifier.notifyEmployeeOfAssignment(asset);
    }

    @Override
    @Transactional
    @LogActivity("Devolveu um ativo de TI para o estoque.")
    public void returnAssetToStock(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found with id: " + assetId));

        asset.setAssignedTo(null);
        asset.setStatus(AssetStatus.IN_STOCK);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetDetailsDto findById(Long id) {
        return assetRepository.findById(id)
                .map(assetMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetDetailsDto> findAll() {
        return assetRepository.findAll().stream()
                .map(assetMapper::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDetailsDto> findAll(AssetStatus status, Long assignedToId, Pageable pageable) {
        Specification<Asset> spec = AssetSpecification.filterBy(status, assignedToId);

        Page<Asset> assetsPage = assetRepository.findAll(spec, pageable);

        return assetsPage.map(assetMapper::toDetailsDto);
    }
}
