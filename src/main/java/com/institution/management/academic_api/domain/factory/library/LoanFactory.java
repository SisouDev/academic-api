package com.institution.management.academic_api.domain.factory.library;

import com.institution.management.academic_api.application.dto.library.CreateLoanRequestDto;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.library.LibraryItem;
import com.institution.management.academic_api.domain.model.entities.library.Loan;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.library.LibraryItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LoanFactory {

    private final PersonRepository personRepository;
    private final LibraryItemRepository libraryItemRepository;

    public Loan create(CreateLoanRequestDto dto) {
        Person borrower = personRepository.findById(dto.borrowerId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + dto.borrowerId()));

        LibraryItem item = libraryItemRepository.findById(dto.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Library item not found with id: " + dto.itemId()));

        if (item.getAvailableCopies() <= 0) {
            throw new IllegalStateException("There are no copies available of '" + item.getTitle() + "' for loan.");
        }

        Loan loan = new Loan();
        loan.setBorrower(borrower);
        loan.setItem(item);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(dto.dueDate());
        loan.setStatus(LoanStatus.ACTIVE);

        item.setAvailableCopies(item.getAvailableCopies() - 1);

        return loan;
    }
}