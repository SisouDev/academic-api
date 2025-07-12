package com.institution.management.academic_api.domain.model.enums.financial;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum TransactionType {
    TUITION_FEE_DEBIT("Tuition fee debit"),
    PAYMENT_CREDIT("Payment credit"),
    LATE_FEE_DEBIT("Late fee debit"),
    SCHOLARSHIP_CREDIT("Scholarship credit");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }
    public static TransactionType fromDisplayName(String text) {
        return Stream.of(TransactionType.values())
                .filter(scope -> scope.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}