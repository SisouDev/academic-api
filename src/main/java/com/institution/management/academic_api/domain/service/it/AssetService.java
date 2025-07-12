package com.institution.management.academic_api.domain.service.it;

import com.institution.management.academic_api.application.dto.it.AssetDetailsDto;
import com.institution.management.academic_api.application.dto.it.CreateAssetRequestDto;

import java.util.List;

public interface AssetService {
    AssetDetailsDto create(CreateAssetRequestDto dto);

    AssetDetailsDto updateStatus(Long assetId, String status);

    void assignAsset(Long assetId, Long employeeId);

    void returnAssetToStock(Long assetId);

    AssetDetailsDto findById(Long id);

    List<AssetDetailsDto> findAll();
}
