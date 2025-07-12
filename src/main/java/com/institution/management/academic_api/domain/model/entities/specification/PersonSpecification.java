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
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            String[] searchTerms = searchTerm.toLowerCase().split("\\s+");

            for (String term : searchTerms) {
                String likePattern = "%" + term + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), likePattern),
                        cb.like(cb.lower(root.get("lastName")), likePattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Person> searchByTermAndType(String searchTerm, PersonType type, boolean includeType) {
        Specification<Person> spec = searchByTerm(searchTerm);

        return spec.and((root, query, cb) -> {
            var typePredicate = cb.equal(root.type(), type.getEntityClass());

            return includeType ? typePredicate : cb.not(typePredicate);
        });
    }
}