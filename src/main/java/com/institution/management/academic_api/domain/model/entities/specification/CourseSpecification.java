package com.institution.management.academic_api.domain.model.entities.specification;

import com.institution.management.academic_api.domain.model.entities.course.Course;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {
    public static Specification<Course> searchByTerm(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), likePattern);
        };
    }
}