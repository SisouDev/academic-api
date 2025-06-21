package com.institution.management.academic_api.domain.factory.academic;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentFactory {

    private final DepartmentMapper departmentMapper;

    public Department create(DepartmentRequestDto requestDto) {
        return departmentMapper.toEntity(requestDto);
    }
}