package com.institution.management.academic_api.domain.model.entities.specification;

import com.institution.management.academic_api.domain.model.entities.helpDesk.SupportTicket;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SupportTicketSpecification {
    public static Specification<SupportTicket> filterBy(TicketStatus status, Long assigneeId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (assigneeId != null) {
                predicates.add(criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}