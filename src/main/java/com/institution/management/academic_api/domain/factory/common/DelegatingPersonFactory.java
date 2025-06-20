package com.institution.management.academic_api.domain.factory.common;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DelegatingPersonFactory {

    private final List<PersonFactory> factories;

    @Autowired
    public DelegatingPersonFactory(List<PersonFactory> factories) {
        this.factories = factories;
    }

    private Map<PersonType, PersonFactory> factoryCache;

    @PostConstruct
    public void init() {
        factoryCache = new EnumMap<>(PersonType.class);
        for (PersonFactory factory : factories) {
            factoryCache.put(factory.supportedType(), factory);
        }
    }

    public Person create(PersonType personType, Object requestDto) {
        return Optional.ofNullable(factoryCache.get(personType))
                .map(factory -> factory.create(requestDto))
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma fábrica encontrada para o tipo: " + personType));
    }
}