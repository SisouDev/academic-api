package com.institution.management.academic_api.application.mapper.simple.teacher;

import com.institution.management.academic_api.application.dto.student.StudentTeacherNoteDto;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherNoteRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherNoteDto;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.teacher.TeacherNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TeacherNoteMapper {

    @Mapping(target = "authorName", expression = "java(note.getAuthor().getFirstName() + \" \" + note.getAuthor().getLastName())")
    @Mapping(target = "createdAt", expression = "java(note.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern(\"dd/MM/yyyy HH:mm\")))")
    @Mapping(target = "enrollmentId", source = "enrollment.id")
    TeacherNoteDto toDto(TeacherNote note);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    TeacherNote toEntity(CreateTeacherNoteRequestDto requestDto);

    @Mapping(target = "authorName", source = "author", qualifiedByName = "authorToFullName")
    @Mapping(target = "subjectName", source = "enrollment.courseSection.subject.name")
    StudentTeacherNoteDto toStudentDto(TeacherNote teacherNote);

    @Named("authorToFullName")
    default String authorToFullName(Person author) {
        if (author == null) {
            return null;
        }
        return author.getFirstName() + " " + author.getLastName();
    }

}