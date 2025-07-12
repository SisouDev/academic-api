package com.institution.management.academic_api.application.mapper.simple.library;

import com.institution.management.academic_api.application.dto.library.LoanDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.library.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LibraryItemMapper.class, PersonMapper.class})
public interface LoanMapper {

    @Mapping(target = "status", expression = "java(loan.getStatus().getDisplayName())")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "borrower", source = "borrower")
    LoanDetailsDto toDetailsDto(Loan loan);
}