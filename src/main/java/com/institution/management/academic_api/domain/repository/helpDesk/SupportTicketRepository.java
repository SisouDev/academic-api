package com.institution.management.academic_api.domain.repository.helpDesk;

import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.helpDesk.SupportTicket;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketPriority;
import com.institution.management.academic_api.domain.model.enums.helpDesk.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long>, JpaSpecificationExecutor<SupportTicket> {
    List<SupportTicket> findByRequesterId(Long requesterId);

    List<SupportTicket> findByAssigneeId(Long assigneeId);

    List<SupportTicket> findByStatus(TicketStatus status);

    long countByStatus(TicketStatus status);

    long countByAssigneeAndStatus(Person assignee, TicketStatus status);


    long countByStatusAndPriority(TicketStatus ticketStatus, TicketPriority ticketPriority);
}
