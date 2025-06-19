package com.institution.management.academic_api.domain.model.entities.institution;

import com.institution.management.academic_api.domain.model.entities.common.Address;

import java.time.LocalDateTime;

public class Institution {
    private Long id;
    private String name;
    private String registerId;
    private Address address;
    private LocalDateTime createdAt;
}
