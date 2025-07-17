package com.institution.management.academic_api.domain.service.library;

import com.institution.management.academic_api.application.dto.library.CreateLoanRequestDto;
import com.institution.management.academic_api.application.dto.library.LoanDetailsDto;
import com.institution.management.academic_api.domain.model.enums.library.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoanService {
    LoanDetailsDto create(CreateLoanRequestDto dto);

    LoanDetailsDto returnLoan(Long loanId);

    LoanDetailsDto findById(Long id);

    List<LoanDetailsDto> findByBorrower(Long borrowerId);

    Page<LoanDetailsDto> findAll(LoanStatus status, Pageable pageable);

}
