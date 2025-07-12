package com.institution.management.academic_api.domain.repository.inventory;

import com.institution.management.academic_api.domain.model.entities.inventory.Material;
import com.institution.management.academic_api.domain.model.enums.inventory.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByNameIgnoreCase(String name);

    List<Material> findByType(MaterialType type);
}
