package com.institution.management.academic_api.services.academic;

import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.service.academic.DepartmentServiceImpl;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.exception.type.institution.InstitutionNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    @DisplayName("Deve criar um departamento com sucesso quando os dados são válidos")
    void create_deveCriarDepartamento_quandoDadosValidos() {
        var institution = new Institution();
        institution.setId(1L);

        var requestDto = new DepartmentRequestDto("Ciência da Computação", "DCC", 1L);
        var newDepartment = new Department();
        var savedDepartment = new Department();
        var responseDto = new DepartmentDetailsDto(1L, "Ciência da Computação", "DCC", null);

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(departmentRepository.existsByNameAndInstitution(requestDto.name(), institution)).thenReturn(false);
        when(departmentMapper.toEntity(requestDto)).thenReturn(newDepartment);
        when(departmentRepository.save(newDepartment)).thenReturn(savedDepartment);
        when(departmentMapper.toDetailsDto(savedDepartment)).thenReturn(responseDto);

        DepartmentDetailsDto result = departmentService.create(requestDto);

        assertNotNull(result); // O resultado não pode ser nulo
        assertEquals("Ciência da Computação", result.name());
        verify(departmentRepository, times(1)).save(newDepartment);
    }

    @Test
    @DisplayName("Deve lançar EntityExistsException ao tentar criar um departamento duplicado")
    void create_deveLancarExcecao_quandoDepartamentoJaExiste() {
        // Arrange
        var institution = new Institution();
        institution.setId(1L);
        var requestDto = new DepartmentRequestDto("Ciência da Computação", "DCC", 1L);

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        when(departmentRepository.existsByNameAndInstitution(requestDto.name(), institution)).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> {
            departmentService.create(requestDto);
        });

        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar InstitutionNotFoundException quando a instituição não existe")
    void create_deveLancarExcecao_quandoInstituicaoNaoExiste() {
        var requestDto = new DepartmentRequestDto("Ciência da Computação", "DCC", 99L);

        when(institutionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstitutionNotFoundException.class, () -> {
            departmentService.create(requestDto);
        });

        verify(departmentRepository, never()).existsByNameAndInstitution(any(), any());
        verify(departmentRepository, never()).save(any());
    }
}