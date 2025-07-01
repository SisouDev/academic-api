package com.institution.management.academic_api.application.service.common;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.specification.PersonSpecification;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.model.enums.common.PersonType;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PersonRepository personRepository;
    private final SubjectRepository subjectRepository;
    private final PersonMapper personMapper;
    private final UserRepository userRepository;


    public Page<PersonSummaryDto> searchPeople(String searchTerm, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Page.empty();
        }


        String username = authentication.getName();

        User currentUser = userRepository.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado no banco de dados: " + username));

        if (isAdmin(currentUser)) {
            Specification<Person> spec = PersonSpecification.searchByTerm(searchTerm);
            return personRepository.findAll(spec, pageable).map(personMapper::toSummaryDto);

        } else if (isEmployee(currentUser)) {
            Specification<Person> spec = PersonSpecification.searchByTerm(searchTerm);
            return personRepository.findAll(spec, pageable).map(personMapper::toSummaryDto);

        } else if (isStudent(currentUser)) {
            Specification<Person> spec = PersonSpecification.searchByTermAndType(searchTerm, PersonType.STUDENT, false);
            return personRepository.findAll(spec, pageable).map(personMapper::toSummaryDto);
        }

        return Page.empty();
    }

    private boolean isAdmin(User user) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ROLE_ADMIN);
    }

    private boolean isEmployee(User user) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ROLE_EMPLOYEE);
    }

    private boolean isStudent(User user) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ROLE_USER);
    }
}