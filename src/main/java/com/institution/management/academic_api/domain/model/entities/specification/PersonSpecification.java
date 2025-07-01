package com.institution.management.academic_api.domain.model.entities.specification;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class PersonSpecification {

    private static final Logger log = LoggerFactory.getLogger(PersonSpecification.class);

    public static Specification<Person> searchByTerm(String searchTerm) {
        return (root, query, cb) -> {
            log.info("Executando especificação de busca com o termo: '{}'", searchTerm);

            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            String[] searchTerms = searchTerm.toLowerCase().split("\\s+");

            for (String term : searchTerms) {
                String likePattern = "%" + term + "%";
                log.info("Criando predicado para o termo: '{}' com o padrão LIKE: '{}'", term, likePattern);

                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), likePattern),
                        cb.like(cb.lower(root.get("lastName")), likePattern)
                ));
            }

            log.info("Total de predicados a serem combinados com AND: {}", predicates.size());
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<Person> searchByTermAndType(String searchTerm, PersonType type, boolean includeType) {
        Specification<Person> spec = searchByTerm(searchTerm);
        return spec.and((root, query, cb) -> {
            if (includeType) {
                return cb.equal(root.get("personType"), type);
            } else {
                return cb.notEqual(root.get("personType"), type);
            }
        });
    }
}