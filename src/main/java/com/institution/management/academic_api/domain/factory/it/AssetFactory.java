package com.institution.management.academic_api.domain.factory.it;

import com.institution.management.academic_api.application.dto.it.CreateAssetRequestDto;
import com.institution.management.academic_api.application.mapper.simple.it.AssetMapper;
import com.institution.management.academic_api.domain.model.entities.it.Asset;
import com.institution.management.academic_api.domain.model.enums.it.AssetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AssetFactory {

    private final AssetMapper assetMapper;

    public Asset create(CreateAssetRequestDto dto) {
        Asset asset = assetMapper.toEntity(dto);

        asset.setCreatedAt(LocalDateTime.now());
        asset.setStatus(AssetStatus.IN_STOCK);
        asset.setAssignedTo(null);

        return asset;
    }
}