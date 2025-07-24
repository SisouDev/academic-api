package com.institution.management.academic_api.application.mapper.simple.financial;

import com.institution.management.academic_api.application.dto.dashboard.director.TransactionDetailDto;
import com.institution.management.academic_api.application.dto.financial.CreateFinancialTransactionRequestDto;
import com.institution.management.academic_api.application.dto.financial.FinancialTransactionDetailsDto;
import com.institution.management.academic_api.application.dto.financial.FinancialTransactionSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.financial.FinancialTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface FinancialTransactionMapper {

    @Mapping(target = "type", expression = "java(transaction.getType().getDisplayName())")
    @Mapping(target = "student", source = "person")
    FinancialTransactionDetailsDto toDetailsDto(FinancialTransaction transaction);

    @Mapping(target = "type", expression = "java(transaction.getType().getDisplayName())")
    FinancialTransactionSummaryDto toSummaryDto(FinancialTransaction transaction);

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "personName", expression = "java(transaction.getPerson().getFirstName() + \" \" + transaction.getPerson().getLastName())")
    @Mapping(target = "personEmail", source = "person.email")
    TransactionDetailDto toDetailDto(FinancialTransaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FinancialTransaction toEntity(CreateFinancialTransactionRequestDto dto);
}