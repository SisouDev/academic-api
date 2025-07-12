package com.institution.management.academic_api.domain.model.enums.academic;

import lombok.Getter;
import java.util.stream.Stream;

@Getter
public enum LessonContentType {
    TEXT("Text"),
    LINK("Link"),
    IMAGE("Image"),
    VIDEO("Video"),
    ATTACHMENT("Attachment"),
    QUOTE("Quote");

    private final String displayName;

    LessonContentType(String displayName) {
        this.displayName = displayName;
    }

    public static LessonContentType fromDisplayName(String text) {
        return Stream.of(LessonContentType.values())
                .filter(type -> type.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhum tipo de conteúdo com o nome '" + text + "' foi encontrado."));
    }
}