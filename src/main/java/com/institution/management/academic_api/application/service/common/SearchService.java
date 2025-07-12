package com.institution.management.academic_api.application.service.common;

import com.institution.management.academic_api.application.dto.common.PersonSearchResultDto;
import com.institution.management.academic_api.application.dto.common.SearchResponseDto;
import com.institution.management.academic_api.application.dto.common.SubjectSearchResultDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.application.mapper.simple.course.SubjectMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final PersonRepository personRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final PersonMapper personMapper;
    private final SubjectMapper subjectMapper;

    @Transactional(readOnly = true)
    public SearchResponseDto search(String searchTerm) {
        User currentUser = getCurrentUser();

        List<PersonSearchResultDto> peopleResults = searchPeopleByRole(currentUser, searchTerm);
        List<SubjectSearchResultDto> subjectResults = searchSubjectsByRole(currentUser, searchTerm);

        return new SearchResponseDto(peopleResults, subjectResults);
    }

    private List<PersonSearchResultDto> searchPeopleByRole(User user, String term) {
        if (hasRole(user, RoleName.ROLE_ADMIN) || hasRole(user, RoleName.ROLE_EMPLOYEE)) {
            Specification<Person> spec = PersonSpecification.searchByTerm(term);
            return personRepository.findAll(spec).stream()
                    .map(personMapper::toSearchResultDto)
                    .collect(Collectors.toList());
        }
        if (hasRole(user, RoleName.ROLE_TEACHER)) {
            Specification<Person> spec = PersonSpecification.searchByTerm(term)
                    .and((root, query, cb) -> root.get("personType").in(PersonType.STUDENT, PersonType.EMPLOYEE));
            return personRepository.findAll(spec).stream()
                    .map(personMapper::toSearchResultDto)
                    .collect(Collectors.toList());
        }
        if (hasRole(user, RoleName.ROLE_STUDENT)) {
            Specification<Person> spec = PersonSpecification.searchByTermAndType(term, PersonType.TEACHER, true);
            return personRepository.findAll(spec).stream()
                    .map(personMapper::toSearchResultDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<SubjectSearchResultDto> searchSubjectsByRole(User user, String term) {
        if (hasRole(user, RoleName.ROLE_ADMIN) || hasRole(user, RoleName.ROLE_TEACHER)) {
            return subjectRepository.findByNameContainingIgnoreCase(term).stream()
                    .map(subjectMapper::toSearchResultDto)
                    .collect(Collectors.toList());
        }
        if (hasRole(user, RoleName.ROLE_STUDENT)) {
            return subjectRepository.findEnrolledSubjectsByStudentIdAndName(user.getPerson().getId(), term).stream()
                    .map(subjectMapper::toSearchResultDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(username).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    private boolean hasRole(User user, RoleName roleName) {
        return user.getRoles().stream().anyMatch(role -> role.getName() == roleName);
    }
}