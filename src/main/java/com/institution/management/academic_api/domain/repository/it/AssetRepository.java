package com.institution.management.academic_api.domain.repository.it;

import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.it.Asset;
import com.institution.management.academic_api.domain.model.enums.it.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByAssetTag(String assetTag);

    List<Asset> findByStatus(AssetStatus status);

    List<Asset> findByAssignedToId(Long employeeId);

    long countByAssignedTo(Employee employee);

}
