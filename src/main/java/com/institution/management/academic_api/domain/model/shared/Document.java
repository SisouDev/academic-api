package com.institution.management.academic_api.domain.model.shared;

import com.institution.management.academic_api.domain.model.enums.common.DocumentType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private String number;
}
