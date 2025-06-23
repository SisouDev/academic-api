package com.institution.management.academic_api.services.institution;

import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.institution.InstitutionMapper;
import com.institution.management.academic_api.application.service.institution.InstitutionServiceImpl;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.exception.type.institution.InstitutionAlreadyExistsException;
import com.institution.management.academic_api.exception.type.institution.InstitutionCannotBeDeletedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceImplTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private InstitutionMapper institutionMapper;

    @InjectMocks
    private InstitutionServiceImpl institutionService;

    @Test
    @DisplayName("Deve criar uma instituição com sucesso quando o registerId é único")
    void create_deveCriarInstituicao_quandoRegisterIdUnico() {
        var requestDto = new CreateInstitutionRequestDto("Nova Instituição", "11.111.111/0001-11", null);
        var newInstitution = new Institution();
        var savedInstitution = new Institution();
        var responseDto = new InstitutionDetailsDto(1L, "Nova Instituição", "11.111.111/0001-11", null, null, null, null, null);

        when(institutionRepository.findByRegisterId(requestDto.registerId())).thenReturn(Optional.empty());
        when(institutionMapper.toEntity(requestDto)).thenReturn(newInstitution);
        when(institutionRepository.save(newInstitution)).thenReturn(savedInstitution);
        when(institutionMapper.toDetailsDto(savedInstitution)).thenReturn(responseDto);

        InstitutionDetailsDto result = institutionService.create(requestDto);

        assertNotNull(result);
        assertEquals("Nova Instituição", result.name());
        verify(institutionRepository, times(1)).save(newInstitution);
    }

    @Test
    @DisplayName("Deve lançar InstitutionAlreadyExistsException ao tentar criar instituição com registerId duplicado")
    void create_deveLancarExcecao_quandoRegisterIdJaExiste() {
        var requestDto = new CreateInstitutionRequestDto("Nova Instituição", "11.111.111/0001-11", null);

        when(institutionRepository.findByRegisterId(requestDto.registerId())).thenReturn(Optional.of(new Institution()));

        assertThrows(InstitutionAlreadyExistsException.class, () -> {
            institutionService.create(requestDto);
        });

        verify(institutionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar uma instituição com sucesso quando ela não tem dependências")
    void delete_deveDeletarInstituicao_quandoNaoHaDependencias() {
        var institutionToDelete = new Institution();
        institutionToDelete.setId(1L);
        institutionToDelete.setDepartments(Collections.emptyList());
        institutionToDelete.setMembers(Collections.emptyList());

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institutionToDelete));
        doNothing().when(institutionRepository).delete(institutionToDelete);

        institutionService.delete(1L);

        verify(institutionRepository, times(1)).delete(institutionToDelete);
    }

    @Test
    @DisplayName("Deve lançar InstitutionCannotBeDeletedException ao tentar deletar instituição com dependências")
    void delete_deveLancarExcecao_quandoHaDependencias() {
        var institutionToDelete = new Institution();
        institutionToDelete.setId(1L);
        institutionToDelete.setDepartments(List.of(new Department()));

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institutionToDelete));

        assertThrows(InstitutionCannotBeDeletedException.class, () -> {
            institutionService.delete(1L);
        });

        verify(institutionRepository, never()).delete(any());
    }
}