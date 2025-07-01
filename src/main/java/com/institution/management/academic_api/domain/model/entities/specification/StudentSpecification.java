package com.institution.management.academic_api.domain.model.entities.specification;

import com.institution.management.academic_api.domain.model.entities.student.Student;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {
    public static Specification<Student> filterBy(String searchTerm, Long institutionId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (institutionId != null) {
                predicates.add(cb.equal(root.get("institution").get("id"), institutionId));
            }

            if (searchTerm != null && !searchTerm.isBlank()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), likePattern),
                        cb.like(cb.lower(root.get("lastName")), likePattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
