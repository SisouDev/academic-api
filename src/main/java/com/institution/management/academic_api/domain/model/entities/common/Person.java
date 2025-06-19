package com.institution.management.academic_api.domain.model.entities.common;

import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.shared.Document;

import java.time.LocalDateTime;

public abstract class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private Document document;
    private String email;
    private String phone;

    private PersonStatus status;
    private LocalDateTime createdAt;

    public abstract PersonType getPersonType();

}
