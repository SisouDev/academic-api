package com.institution.management.academic_api.domain.model.enums.inventory;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum MaterialType {
    STATIONERY("Stationery"),
    CLEANING("Cleaning Supplies"),
    ELECTRONICS("Electronics"),
    FURNITURE("Furniture"),
    PRINTING("Printing Material"),
    TEACHING_SUPPLIES("Teaching Supplies"),
    LAB_MATERIALS("Laboratory Materials"),
    ART_SUPPLIES("Art Supplies"),
    IT_SUPPORT("IT Support"),
    SAFETY_EQUIPMENT("Safety Equipment"),
    TOOLS("Tools and Maintenance"),
    OTHER("Other");

    private final String displayName;

    MaterialType(String displayName) {
        this.displayName = displayName;
    }

    public static MaterialType fromDisplayName(String text) {
        return Stream.of(MaterialType.values())
                .filter(e -> e.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
