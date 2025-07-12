package com.institution.management.academic_api.application.mapper.simple.library;

import com.institution.management.academic_api.application.dto.library.ReservationDetailsDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.library.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LibraryItemMapper.class, PersonMapper.class})
public interface ReservationMapper {

    @Mapping(target = "status", expression = "java(reservation.getStatus().getDisplayName())")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "person", source = "person")
    ReservationDetailsDto toDetailsDto(Reservation reservation);
}