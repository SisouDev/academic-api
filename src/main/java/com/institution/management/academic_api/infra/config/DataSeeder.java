package com.institution.management.academic_api.infra.config;

import com.institution.management.academic_api.application.dto.academic.AcademicTermDetailsDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentDetailsDto;
import com.institution.management.academic_api.application.dto.academic.DepartmentRequestDto;
import com.institution.management.academic_api.application.dto.common.AddressDto;
import com.institution.management.academic_api.application.dto.common.DocumentDto;
import com.institution.management.academic_api.application.dto.course.*;
import com.institution.management.academic_api.application.dto.employee.CreateEmployeeRequestDto;
import com.institution.management.academic_api.application.dto.employee.EmployeeResponseDto;
import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.dto.student.*;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.service.common.SeedingService;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.entities.teacher.Teacher;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.common.RoleName;
import com.institution.management.academic_api.domain.model.enums.employee.JobPosition;
import com.institution.management.academic_api.domain.model.enums.teacher.AcademicDegree;
import com.institution.management.academic_api.domain.repository.academic.AcademicTermRepository;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import com.institution.management.academic_api.domain.repository.common.PersonRepository;
import com.institution.management.academic_api.domain.repository.common.RoleRepository;
import com.institution.management.academic_api.domain.repository.course.CourseRepository;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.course.SubjectRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.institution.InstitutionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import com.institution.management.academic_api.domain.repository.teacher.TeacherRepository;
import com.institution.management.academic_api.domain.service.academic.AcademicTermService;
import com.institution.management.academic_api.domain.service.academic.DepartmentService;
import com.institution.management.academic_api.domain.service.course.CourseSectionService;
import com.institution.management.academic_api.domain.service.course.CourseService;
import com.institution.management.academic_api.domain.service.course.SubjectService;
import com.institution.management.academic_api.domain.service.employee.EmployeeService;
import com.institution.management.academic_api.domain.service.student.AssessmentDefinitionService;
import com.institution.management.academic_api.domain.service.student.AssessmentService;
import com.institution.management.academic_api.domain.service.student.EnrollmentService;
import com.institution.management.academic_api.domain.service.student.StudentService;
import com.institution.management.academic_api.domain.service.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final InstitutionRepository institutionRepository;
    private final PersonRepository personRepository;
    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final SubjectRepository subjectRepository;
    private final SubjectService subjectService;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final AcademicTermRepository academicTermRepository;
    private final AcademicTermService academicTermService;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionService courseSectionService;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;
    private final EmployeeRepository employeeRepository;
    private final AssessmentService assessmentService;
    private final AssessmentDefinitionRepository assessmentDefinitionRepository;
    private final AssessmentRepository assessmentRepository;
    private final SeedingService seedingService;
    private final AssessmentDefinitionService assessmentDefinitionService;


    @Override
    public void run(String... args) throws Exception {
        log.info("Executando DataSeeder para o perfil 'dev'...");

        Institution defaultInstitution = seedInstitution();

        if (defaultInstitution != null) {

            Map<String, Teacher> teachers = seedTeachers(defaultInstitution);
            Map<String, Student> students = seedStudents(defaultInstitution);
            Map<String, Employee> employees = seedEmployees(defaultInstitution);

            Map<String, Department> departments = seedDepartments(defaultInstitution);
            if (departments != null && !departments.isEmpty()) {
                Map<String, Course> courses = seedCourses(departments);

                if (courses != null && !courses.isEmpty()) {
                    Map<String, Subject> subjects = seedSubjects(courses);
                    Map<String, AcademicTerm> academicTerms = seedAcademicTerms(defaultInstitution);

                    if (academicTerms != null && !academicTerms.isEmpty() &&
                            subjects != null && !subjects.isEmpty() &&
                            teachers != null && !teachers.isEmpty()) {
                        seedingService.seedTeacherSubjectRelations(teachers, subjects);
                        Map<String, CourseSection> courseSections = seedCourseSections(academicTerms, subjects, teachers);

                        if (courseSections != null && !courseSections.isEmpty() &&
                                students != null && !students.isEmpty()) {
                            Map<String, Enrollment> enrollments = seedEnrollments(students, courseSections);

                            Map<String, AssessmentDefinition> assessmentDefinitions = seedAssessmentDefinitions(courseSections);
                            seedingService.seedAssessmentsAndAttendance(enrollments, assessmentDefinitions);
                        }
                    }
                }
            }
        }

        log.info("DataSeeder finalizado com sucesso!");
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            log.info("Criando perfis (Roles) padrão...");
            roleRepository.save(new Role(RoleName.ROLE_USER));
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            roleRepository.save(new Role(RoleName.ROLE_MANAGER));
            roleRepository.save(new Role(RoleName.ROLE_TEACHER));
            roleRepository.save(new Role(RoleName.ROLE_STUDENT));
            roleRepository.save(new Role(RoleName.ROLE_EMPLOYEE));
            roleRepository.save(new Role(RoleName.ROLE_FINANCE));
        }
    }

    private Institution seedInstitution() {
        if (!personRepository.existsByEmail("admin@instituicao.com")) {
            log.info("Criando instituição e administrador padrão...");

            Institution institution = institutionRepository.findByRegisterId("00.000.000/0001-00")
                    .orElseGet(() -> {
                        Institution defaultInstitution = new Institution();
                        defaultInstitution.setName("Instituição Padrão de Testes");
                        defaultInstitution.setRegisterId("00.000.000/0001-00");
                        return institutionRepository.save(defaultInstitution);
                    });

            var adminRequest = new CreateInstitutionAdminRequestDto(
                    institution.getId(),
                    "Admin",
                    "do Sistema",
                    "admin@instituicao.com",
                    "99999-9999",
                    new DocumentDto("OTHER", "000000000")
            );

            employeeService.createAdmin(adminRequest);

            log.info("Usuário 'admin' com senha '{}' criado com sucesso.", adminRequest.document().number());
            return institution;
        }
        return institutionRepository.findByRegisterId("00.000.000/0001-00")
                .orElseGet(() -> {
                    log.info("Criando instituição padrão...");
                    Institution defaultInstitution = new Institution();
                    defaultInstitution.setName("Instituição Padrão de Testes");
                    defaultInstitution.setRegisterId("00.000.000/0001-00");

                    defaultInstitution.setCreatedAt(LocalDateTime.now());

                    return institutionRepository.save(defaultInstitution);
                });
    }

    private void seedAdminUser(Institution institution) {
        if (personRepository.existsByEmail("admin@instituicao.com")) {
            log.info("Usuário admin padrão já existe. Nenhuma ação necessária.");
            return;
        }

        log.info("Criando administrador padrão...");

        var adminRequest = new CreateInstitutionAdminRequestDto(
                institution.getId(),
                "Admin",
                "do Sistema",
                "admin@instituicao.com",
                "99999-9999",
                new DocumentDto("OTHER", "admin123")
        );

        try {
            employeeService.createAdmin(adminRequest);
            log.info("Usuário 'admin@instituicao.com' criado com sucesso. A senha padrão é o número do documento: '{}'", adminRequest.document().number());
        } catch (Exception e) {
            log.error("Erro ao criar o usuário administrador padrão: {}", e.getMessage());
        }
    }

    private Map<String, Department> seedDepartments(Institution institution) {
        log.info("Iniciando seeding de Departamentos...");

        Map<String, Department> createdDepartments = new HashMap<>();

        createdDepartments.put("CS", createDepartmentIfNotExists("Ciência da Computação", "DCC", institution));
        createdDepartments.put("EE", createDepartmentIfNotExists("Engenharia Elétrica", "DEE", institution));
        createdDepartments.put("LT", createDepartmentIfNotExists("Letras", "DL", institution));
        createdDepartments.put("MT", createDepartmentIfNotExists("Matemática", "DMT", institution));
        createdDepartments.put("BIO", createDepartmentIfNotExists("Biologia", "DBIO", institution));
        createdDepartments.put("FIS", createDepartmentIfNotExists("Física", "DFIS", institution));
        createdDepartments.put("QUI", createDepartmentIfNotExists("Química", "DQUI", institution));
        createdDepartments.put("HIS", createDepartmentIfNotExists("História", "DHIS", institution));
        createdDepartments.put("GEO", createDepartmentIfNotExists("Geografia", "DGEO", institution));
        createdDepartments.put("ADM", createDepartmentIfNotExists("Administração", "DADM", institution));
        createdDepartments.put("DIR", createDepartmentIfNotExists("Direito", "DDIR", institution));
        createdDepartments.put("PSI", createDepartmentIfNotExists("Psicologia", "DPSI", institution));
        createdDepartments.put("TI", createDepartmentIfNotExists("Tecnologia da Informação", "DTI", institution));
        createdDepartments.put("HR", createDepartmentIfNotExists("Recursos Humanos", "DHR", institution));


        log.info("Seeding de Departamentos finalizado.");
        return createdDepartments;
    }

    private Department createDepartmentIfNotExists(String name, String acronym, Institution institution) {
        return departmentRepository.findByNameAndInstitution(name, institution)
                .orElseGet(() -> {
                    log.info("Criando departamento de exemplo: '{}'...", name);

                    var departmentRequest = new DepartmentRequestDto(
                            name,
                            acronym,
                            institution.getId()
                    );

                    DepartmentDetailsDto createdDepartmentDto = departmentService.create(departmentRequest);
                    log.info("Departamento '{}' criado com sucesso.", createdDepartmentDto.name());

                    return departmentRepository.findById(createdDepartmentDto.id())
                            .orElseThrow(() -> new IllegalStateException("O departamento acabou de ser criado, mas não foi encontrado."));
                });
    }


    private Map<String, Course> seedCourses(Map<String, Department> departments) {
        log.info("Iniciando seeding de Cursos...");
        Map<String, Course> createdCourses = new HashMap<>();

        Department csDepartment = departments.get("CS");
        if (csDepartment != null) {
            createdCourses.put("SE", createCourseIfNotExists(
                    "Engenharia de Software",
                    "Um curso focado na construção de sistemas de software robustos e de alta qualidade.",
                    10,
                    csDepartment
            ));
            createdCourses.put("DS", createCourseIfNotExists(
                    "Ciência de Dados",
                    "Curso que aborda a análise de grandes volumes de dados, estatística e machine learning.",
                    8,
                    csDepartment
            ));
            createdCourses.put("SI", createCourseIfNotExists(
                    "Sistemas de Informação",
                    "Formação voltada para a gestão de tecnologia da informação, infraestrutura e análise de sistemas.",
                    8,
                    csDepartment
            ));

            createdCourses.put("CC", createCourseIfNotExists(
                    "Ciência da Computação",
                    "Curso que oferece base sólida em algoritmos, estruturas de dados, computação teórica e desenvolvimento de software.",
                    8,
                    csDepartment
            ));

        }

        Department eeDepartment = departments.get("EE");
        if (eeDepartment != null) {
            createdCourses.put("ENE", createCourseIfNotExists(
                    "Engenharia Eletrônica",
                    "Foco em circuitos eletrônicos, microcontroladores, e sistemas embarcados.",
                    10,
                    eeDepartment
            ));

            createdCourses.put("ENP", createCourseIfNotExists(
                    "Engenharia de Potência",
                    "Voltado para sistemas de geração, transmissão e distribuição de energia elétrica.",
                    10,
                    eeDepartment
            ));
        }


        Department lettersDepartment = departments.get("LT");
        if (lettersDepartment != null) {
            createdCourses.put("LC", createCourseIfNotExists(
                    "Literatura Clássica",
                    "Estudo das grandes obras literárias da antiguidade greco-romana.",
                    8,
                    lettersDepartment
            ));
            createdCourses.put("LP", createCourseIfNotExists(
                    "Linguística e Português",
                    "Curso que aprofunda no estudo da língua portuguesa e suas estruturas linguísticas.",
                    8,
                    lettersDepartment
            ));
        }

        Department mtDepartment = departments.get("MT");
        if (mtDepartment != null) {
            createdCourses.put("MAT", createCourseIfNotExists(
                    "Matemática Pura",
                    "Exploração teórica da matemática, incluindo álgebra abstrata, análise e topologia.",
                    8,
                    mtDepartment
            ));

            createdCourses.put("MAP", createCourseIfNotExists(
                    "Matemática Aplicada",
                    "Foco em resolver problemas práticos em engenharia, física e finanças com modelagem matemática.",
                    8,
                    mtDepartment
            ));
        }

        Department bioDepartment = departments.get("BIO");
        if (bioDepartment != null) {
            createdCourses.put("BIO", createCourseIfNotExists(
                    "Biologia",
                    "Curso dedicado ao estudo dos seres vivos, ecologia, genética e evolução.",
                    8,
                    bioDepartment
            ));

            createdCourses.put("MBIO", createCourseIfNotExists(
                    "Microbiologia",
                    "Foco no estudo de microrganismos e suas aplicações na saúde, alimentos e meio ambiente.",
                    8,
                    bioDepartment
            ));
        }
        Department psiDepartment = departments.get("PSI");
        if (psiDepartment != null) {
            createdCourses.put("PSICO", createCourseIfNotExists(
                    "Psicologia",
                    "Estudo do comportamento humano, processos mentais e práticas clínicas.",
                    10,
                    psiDepartment
            ));

            createdCourses.put("PSIEDU", createCourseIfNotExists(
                    "Psicologia Educacional",
                    "Curso voltado à aplicação da psicologia no contexto da educação e desenvolvimento escolar.",
                    8,
                    psiDepartment
            ));
        }
        Department dirDepartment = departments.get("DIR");
        if (dirDepartment != null) {
            createdCourses.put("DIR", createCourseIfNotExists(
                    "Direito",
                    "Formação completa em legislação, jurisprudência, ética e práticas jurídicas.",
                    10,
                    dirDepartment
            ));

            createdCourses.put("DIRPEN", createCourseIfNotExists(
                    "Direito Penal",
                    "Curso especializado nas normas do direito penal, criminologia e processo penal.",
                    8,
                    dirDepartment
            ));
        }
        Department quiDepartment = departments.get("QUI");
        if (quiDepartment != null) {
            createdCourses.put("QUI", createCourseIfNotExists(
                    "Química",
                    "Estudo da composição, estrutura e propriedades das substâncias e suas reações.",
                    8,
                    quiDepartment
            ));

            createdCourses.put("QUIIND", createCourseIfNotExists(
                    "Química Industrial",
                    "Foco na aplicação de processos químicos na indústria e no controle de qualidade.",
                    8,
                    quiDepartment
            ));
        }
        Department admDepartment = departments.get("ADM");
        if (admDepartment != null) {
            createdCourses.put("ADM", createCourseIfNotExists(
                    "Administração",
                    "Curso que desenvolve competências em gestão empresarial, finanças e liderança.",
                    8,
                    admDepartment
            ));

            createdCourses.put("ADMPI", createCourseIfNotExists(
                    "Administração Pública e Inovação",
                    "Voltado para a gestão de instituições públicas com foco em inovação e políticas públicas.",
                    8,
                    admDepartment
            ));
        }

        log.info("Seeding de Cursos finalizado.");
        return createdCourses;
    }

    private Course createCourseIfNotExists(String name, String description, int duration, Department department) {
        return courseRepository.findByNameAndDepartment(name, department)
                .orElseGet(() -> {
                    log.info("Criando curso de exemplo: '{}' no departamento '{}'...", name, department.getName());

                    var courseRequest = new CreateCourseRequestDto(
                            name,
                            description,
                            duration,
                            department.getId()
                    );

                    CourseDetailsDto createdCourseDto = courseService.create(courseRequest);
                    log.info("Curso '{}' criado com sucesso.", createdCourseDto.name());

                    return courseRepository.findById(createdCourseDto.id())
                            .orElseThrow(() -> new IllegalStateException("O curso acabou de ser criado, mas não foi encontrado."));
                });
    }



    private Map<String, Subject> seedSubjects(Map<String, Course> courses) {
        log.info("Iniciando seeding de Disciplinas...");
        Map<String, Subject> createdSubjects = new HashMap<>();

        Course seCourse = courses.get("SE");
        if (seCourse != null) {
            createdSubjects.put("ADS", createSubjectIfNotExists("Algoritmos e Estruturas de Dados", 90, seCourse, 1));
            createdSubjects.put("POO", createSubjectIfNotExists("Programação Orientada a Objetos", 90, seCourse, 2));
            createdSubjects.put("DB", createSubjectIfNotExists("Banco de Dados", 72, seCourse, 2));
            createdSubjects.put("ENGSW", createSubjectIfNotExists("Engenharia de Software I", 90, seCourse, 3));
            createdSubjects.put("TESTE", createSubjectIfNotExists("Teste e Validação de Software", 72, seCourse, 4));
            createdSubjects.put("ARQ", createSubjectIfNotExists("Arquitetura de Software", 72, seCourse, 5));
            createdSubjects.put("AGIL", createSubjectIfNotExists("Metodologias Ágeis", 60, seCourse, 6));
        }

        Course dsCourse = courses.get("DS");
        if (dsCourse != null) {
            createdSubjects.put("STAT", createSubjectIfNotExists("Estatística Aplicada", 90, dsCourse, 1));
            createdSubjects.put("PYDS", createSubjectIfNotExists("Python para Ciência de Dados", 90, dsCourse, 2));
            createdSubjects.put("MINDADOS", createSubjectIfNotExists("Mineração de Dados", 72, dsCourse, 3));
            createdSubjects.put("ML", createSubjectIfNotExists("Machine Learning", 72, dsCourse, 4));
            createdSubjects.put("BIGD", createSubjectIfNotExists("Big Data", 72, dsCourse, 5));
            createdSubjects.put("VISDADOS", createSubjectIfNotExists("Visualização de Dados", 60, dsCourse, 6));
        }

        Course lcCourse = courses.get("LC");
        if(lcCourse != null){
            createdSubjects.put("TL", createSubjectIfNotExists("Teoria Literária", 90, lcCourse, 1));
            createdSubjects.put("LATIM", createSubjectIfNotExists("Latim I", 60, lcCourse, 1));
            createdSubjects.put("HISLIT", createSubjectIfNotExists("História da Literatura Clássica", 72, lcCourse, 2));
            createdSubjects.put("TRAG", createSubjectIfNotExists("Tragédia Grega", 60, lcCourse, 3));
            createdSubjects.put("EPOP", createSubjectIfNotExists("Épica e Poesia", 72, lcCourse, 4));
        }

        Course bioCourse = courses.get("BIO");
        if (bioCourse != null) {
            createdSubjects.put("ANAT", createSubjectIfNotExists("Anatomia Humana", 90, bioCourse, 1));
            createdSubjects.put("BOT", createSubjectIfNotExists("Botânica", 72, bioCourse, 2));
            createdSubjects.put("ECO", createSubjectIfNotExists("Ecologia", 72, bioCourse, 3));
            createdSubjects.put("GEN", createSubjectIfNotExists("Genética", 90, bioCourse, 4));
        }

        Course psicoCourse = courses.get("PSICO");
        if (psicoCourse != null) {
            createdSubjects.put("PSIBAS", createSubjectIfNotExists("Fundamentos da Psicologia", 60, psicoCourse, 1));
            createdSubjects.put("DEVHUM", createSubjectIfNotExists("Desenvolvimento Humano", 72, psicoCourse, 2));
            createdSubjects.put("TEORIAS", createSubjectIfNotExists("Teorias da Personalidade", 72, psicoCourse, 3));
            createdSubjects.put("PSICLIN", createSubjectIfNotExists("Psicologia Clínica", 90, psicoCourse, 4));
        }

        Course dirCourse = courses.get("DIR");
        if (dirCourse != null) {
            createdSubjects.put("FILDIR", createSubjectIfNotExists("Filosofia do Direito", 60, dirCourse, 1));
            createdSubjects.put("DIRCON", createSubjectIfNotExists("Direito Constitucional", 90, dirCourse, 1));
            createdSubjects.put("DIRCIV", createSubjectIfNotExists("Direito Civil I", 72, dirCourse, 2));
            createdSubjects.put("DIRADM", createSubjectIfNotExists("Direito Administrativo", 72, dirCourse, 3));
        }

        Course matCourse = courses.get("MAT");
        if (matCourse != null) {
            createdSubjects.put("CALC3", createSubjectIfNotExists("Cálculo III", 90, matCourse, 1));
            createdSubjects.put("LOG", createSubjectIfNotExists("Lógica Matemática", 72, matCourse, 2));
            createdSubjects.put("ALGAB", createSubjectIfNotExists("Álgebra Abstrata", 90, matCourse, 3));
            createdSubjects.put("TOP", createSubjectIfNotExists("Topologia Geral", 72, matCourse, 4));
        }

        Course quiCourse = courses.get("QUI");
        if (quiCourse != null) {
            createdSubjects.put("QUIORG", createSubjectIfNotExists("Química Orgânica I", 72, quiCourse, 1));
            createdSubjects.put("QUIANA", createSubjectIfNotExists("Química Analítica", 72, quiCourse, 2));
            createdSubjects.put("FISQUI", createSubjectIfNotExists("Fisico-Química", 90, quiCourse, 3));
            createdSubjects.put("LAB", createSubjectIfNotExists("Práticas de Laboratório", 60, quiCourse, 4));
        }

        log.info("Seeding de Disciplinas finalizado.");
        return createdSubjects;
    }

    private Subject createSubjectIfNotExists(String name, int workloadHours, Course course, Integer semester) {
        return subjectRepository.findByNameAndCourse(name, course)
                .orElseGet(() -> {
                    log.info("Criando disciplina de exemplo: '{}' para o curso '{}'...", name, course.getName());

                    var subjectRequest = new CreateSubjectRequestDto(
                            name,
                            workloadHours,
                            course.getId(),
                            semester
                    );

                    SubjectDetailsDto createdSubjectDto = subjectService.create(subjectRequest);
                    log.info("Disciplina '{}' criada com sucesso.", createdSubjectDto.name());

                    return subjectRepository.findById(createdSubjectDto.id())
                            .orElseThrow(() -> new IllegalStateException("A disciplina acabou de ser criada, mas não foi encontrada."));
                });
    }

    private Map<String, Teacher> seedTeachers(Institution institution) {
        log.info("Iniciando seeding de Professores...");
        Map<String, Teacher> createdTeachers = new HashMap<>();

        createdTeachers.put("ROSSI", createTeacherIfNotExists(
                "Marco",
                "Rossi",
                "marco.rossi@instituicao.com",
                AcademicDegree.PHD,
                new DocumentDto("NATIONAL_ID", "987654321"),
                institution
        ));

        createdTeachers.put("ALVES", createTeacherIfNotExists(
                "Carla",
                "Alves",
                "carla.alves@instituicao.com",
                AcademicDegree.MASTER,
                new DocumentDto("NATIONAL_ID", "123456789"),
                institution
        ));

        createdTeachers.put("FERREIRA", createTeacherIfNotExists(
                "Renata",
                "Ferreira",
                "renata.ferreira@instituicao.com",
                AcademicDegree.POSTDOC,
                new DocumentDto("NATIONAL_ID", "78945612300"),
                institution
        ));

        createdTeachers.put("SANTOS", createTeacherIfNotExists(
                "Lucas",
                "dos Santos",
                "lucas.santos@instituicao.com",
                AcademicDegree.MASTER,
                new DocumentDto("NATIONAL_ID", "32165498700"),
                institution
        ));

        createdTeachers.put("MARTINS", createTeacherIfNotExists(
                "Patrícia",
                "Martins",
                "patricia.martins@instituicao.com",
                AcademicDegree.BACHELOR,
                new DocumentDto("NATIONAL_ID", "45678912300"),
                institution
        ));

        createdTeachers.put("NOGUEIRA", createTeacherIfNotExists(
                "Eduardo",
                "Nogueira",
                "eduardo.nogueira@instituicao.com",
                AcademicDegree.LICENTIATE,
                new DocumentDto("NATIONAL_ID", "65412378900"),
                institution
        ));

        createdTeachers.put("RAMOS", createTeacherIfNotExists(
                "Tatiane",
                "Ramos",
                "tatiane.ramos@instituicao.com",
                AcademicDegree.SPECIALIZATION,
                new DocumentDto("NATIONAL_ID", "11223344556"),
                institution
        ));

        createdTeachers.put("BARROS", createTeacherIfNotExists(
                "Henrique",
                "Barros",
                "henrique.barros@instituicao.com",
                AcademicDegree.TECHNICAL,
                new DocumentDto("NATIONAL_ID", "99887766554"),
                institution
        ));

        log.info("Seeding de Professores finalizado.");
        return createdTeachers;
    }

    private Teacher createTeacherIfNotExists(String firstName, String lastName, String email, AcademicDegree degree, DocumentDto document, Institution institution) {
        Optional<Person> existingPerson = personRepository.findByEmail(email);
        if (existingPerson.isPresent()) {
            if (existingPerson.get() instanceof Teacher) {
                return (Teacher) existingPerson.get();
            }
            log.warn("Uma pessoa com o e-mail {} já existe, mas não é um professor. Nenhum professor foi criado.", email);
            return null;
        }

        log.info("Criando professor de exemplo: {} {}...", firstName, lastName);

        var teacherRequest = new CreateTeacherRequestDto(
                firstName,
                lastName,
                email,
                degree.name(),
                institution.getId(),
                document
        );

        try {
            TeacherResponseDto createdTeacherDto = teacherService.create(teacherRequest);
            log.info("Professor '{}' e seu usuário correspondente foram criados com sucesso.", createdTeacherDto.getEmail());

            return teacherRepository.findById(createdTeacherDto.getId())
                    .orElseThrow(() -> new IllegalStateException("O professor acabou de ser criado, mas não foi encontrado."));
        } catch (Exception e) {
            log.error("Erro ao criar o professor de exemplo '{}': {}", email, e.getMessage());
            return null;
        }
    }

    private Map<String, Student> seedStudents(Institution institution) {
        log.info("Iniciando seeding de Alunos...");
        Map<String, Student> createdStudents = new HashMap<>();

        createdStudents.put("JOAO", createStudentIfNotExists(
                "João",
                "da Silva",
                "aluno.joao@instituicao.com",
                LocalDate.of(2005, 3, 15),
                new DocumentDto("NATIONAL_ID", "11122233344"),
                new AddressDto("Rua dos Estudantes", "789", null, "Bairro da Escola", "São Paulo", "SP", "02000-000"),
                institution
        ));

        createdStudents.put("MARIA", createStudentIfNotExists(
                "Maria",
                "Oliveira",
                "aluna.maria@instituicao.com",
                LocalDate.of(2006, 8, 22),
                new DocumentDto("NATIONAL_ID", "55566677788"),
                new AddressDto("Avenida do Saber", "101", "Apto 2", "Centro", "Rio de Janeiro", "RJ", "03000-000"),
                institution
        ));

        createdStudents.put("LUCAS", createStudentIfNotExists(
                "Lucas",
                "Pereira",
                "aluno.lucas@instituicao.com",
                LocalDate.of(2005, 12, 3),
                new DocumentDto("NATIONAL_ID", "99988877766"),
                new AddressDto("Rua das Flores", "123", "Casa 1", "Jardim das Rosas", "Belo Horizonte", "MG", "31000-000"),
                institution
        ));

        createdStudents.put("ANA", createStudentIfNotExists(
                "Ana",
                "Souza",
                "aluna.ana@instituicao.com",
                LocalDate.of(2004, 5, 28),
                new DocumentDto("NATIONAL_ID", "22233344455"),
                new AddressDto("Travessa dos Estudantes", "456", null, "Bairro Novo", "Curitiba", "PR", "80000-000"),
                institution
        ));

        createdStudents.put("PEDRO", createStudentIfNotExists(
                "Pedro",
                "Almeida",
                "aluno.pedro@instituicao.com",
                LocalDate.of(2006, 9, 10),
                new DocumentDto("NATIONAL_ID", "12345678900"),
                new AddressDto("Alameda da Juventude", "78", "Bloco C", "Estância", "Porto Alegre", "RS", "90000-000"),
                institution
        ));

        createdStudents.put("JULIA", createStudentIfNotExists(
                "Julia",
                "Lima",
                "aluna.julia@instituicao.com",
                LocalDate.of(2005, 7, 19),
                new DocumentDto("NATIONAL_ID", "09876543211"),
                new AddressDto("Rua do Saber", "15", null, "Vila Esperança", "Salvador", "BA", "40000-000"),
                institution
        ));

        createdStudents.put("RAFAEL", createStudentIfNotExists(
                "Rafael",
                "Fernandes",
                "aluno.rafael@instituicao.com",
                LocalDate.of(2007, 2, 5),
                new DocumentDto("NATIONAL_ID", "33445566778"),
                new AddressDto("Praça da Educação", "88", "Apto 301", "Centro", "Fortaleza", "CE", "60000-000"),
                institution
        ));
        createdStudents.put("CAROLINA", createStudentIfNotExists(
                "Carolina",
                "Mendes",
                "aluna.carolina@instituicao.com",
                LocalDate.of(2004, 11, 11),
                new DocumentDto("NATIONAL_ID", "44556677889"),
                new AddressDto("Rua da Harmonia", "222", null, "Bosque Feliz", "Recife", "PE", "50000-000"),
                institution
        ));

        createdStudents.put("GUSTAVO", createStudentIfNotExists(
                "Gustavo",
                "Ribeiro",
                "aluno.gustavo@instituicao.com",
                LocalDate.of(2006, 1, 30),
                new DocumentDto("NATIONAL_ID", "66778899001"),
                new AddressDto("Estrada do Estudante", "99", "Bloco A", "Jardim Acadêmico", "Florianópolis", "SC", "88000-000"),
                institution
        ));

        createdStudents.put("LARISSA", createStudentIfNotExists(
                "Larissa",
                "Barbosa",
                "aluna.larissa@instituicao.com",
                LocalDate.of(2005, 6, 9),
                new DocumentDto("NATIONAL_ID", "77889900112"),
                new AddressDto("Rua da Luz", "50", null, "Vila do Conhecimento", "Goiânia", "GO", "74000-000"),
                institution
        ));

        createdStudents.put("THIAGO", createStudentIfNotExists(
                "Thiago",
                "Machado",
                "aluno.thiago@instituicao.com",
                LocalDate.of(2004, 10, 4),
                new DocumentDto("NATIONAL_ID", "88990011223"),
                new AddressDto("Rua das Letras", "303", "Casa B", "Bairro Universitário", "Manaus", "AM", "69000-000"),
                institution
        ));

        createdStudents.put("BEATRIZ", createStudentIfNotExists(
                "Beatriz",
                "Costa",
                "aluna.beatriz@instituicao.com",
                LocalDate.of(2007, 3, 25),
                new DocumentDto("NATIONAL_ID", "99001122334"),
                new AddressDto("Alameda do Saber", "12", null, "Zona Escolar", "Natal", "RN", "59000-000"),
                institution
        ));


        log.info("Seeding de Alunos finalizado.");
        return createdStudents;
    }

    private Student createStudentIfNotExists(String firstName, String lastName, String email, LocalDate birthDate, DocumentDto document, AddressDto address, Institution institution) {

        Optional<Person> existingPerson = personRepository.findByEmail(email);
        if (existingPerson.isPresent()) {
            if (existingPerson.get() instanceof Student) {
                return (Student) existingPerson.get();
            }
            log.warn("Uma pessoa com o e-mail {} já existe, mas não é um aluno. Nenhum aluno foi criado.", email);
            return null;
        }

        log.info("Criando aluno de exemplo: {} {}...", firstName, lastName);

        var studentRequest = new CreateStudentRequestDto(
                firstName,
                lastName,
                email,
                birthDate,
                institution.getId(),
                document,
                address
        );

        try {
            StudentResponseDto createdStudentDto = studentService.create(studentRequest);
            log.info("Aluno '{}' e seu usuário correspondente foram criados com sucesso.", createdStudentDto.getEmail());

            return studentRepository.findById(createdStudentDto.getId())
                    .orElseThrow(() -> new IllegalStateException("O aluno acabou de ser criado, mas não foi encontrado."));
        } catch (Exception e) {
            log.error("Erro ao criar o aluno de exemplo '{}': {}", email, e.getMessage());
            return null;
        }
    }

    private Map<String, AcademicTerm> seedAcademicTerms(Institution institution) {
        log.info("Iniciando seeding de Períodos Letivos...");
        Map<String, AcademicTerm> createdTerms = new HashMap<>();

        createdTerms.put("2025_2", createAcademicTermIfNotExists(
                Year.of(2025),
                2,
                LocalDate.of(2025, 8, 1),
                LocalDate.of(2025, 12, 15),
                AcademicTermStatus.ENROLLMENT_OPEN,
                institution
        ));

        createdTerms.put("2026_1", createAcademicTermIfNotExists(
                Year.of(2026),
                1,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 6, 30),
                AcademicTermStatus.ENROLLMENT_OPEN,
                institution
        ));


        log.info("Seeding de Períodos Letivos finalizado.");
        return createdTerms;
    }

    private AcademicTerm createAcademicTermIfNotExists(Year year, int semester, LocalDate startDate, LocalDate endDate, AcademicTermStatus status, Institution institution) {
        return academicTermRepository.findByYearAndSemesterAndInstitution(year, semester, institution)
                .orElseGet(() -> {
                    log.info("Criando período letivo de exemplo: {}/{}...", year, semester);

                    var termRequest = new AcademicTermRequestDto(
                            institution.getId(),
                            year,
                            semester,
                            startDate,
                            endDate,
                            status.name()
                    );

                    AcademicTermDetailsDto createdTermDto = academicTermService.create(termRequest);
                    log.info("Período Letivo {}/{} criado com sucesso.", createdTermDto.year(), createdTermDto.semester());

                    return academicTermRepository.findById(createdTermDto.id())
                            .orElseThrow(() -> new IllegalStateException("O período letivo acabou de ser criado, mas não foi encontrado."));
                });
    }

    private Map<String, CourseSection> seedCourseSections(Map<String, AcademicTerm> terms, Map<String, Subject> subjects, Map<String, Teacher> teachers) {
        log.info("Iniciando seeding de Turmas (Course Sections)...");
        Map<String, CourseSection> createdSections = new HashMap<>();

        AcademicTerm term2025_2 = terms.get("2025_2");
        Teacher profRossi = teachers.get("ROSSI");
        Teacher profaAlves = teachers.get("ALVES");
        Subject pooSubject = subjects.get("POO");
        Subject dbSubject = subjects.get("DB");
        Subject tlSubject = subjects.get("TL");

        if (term2025_2 != null && profRossi != null && pooSubject != null) {
            createdSections.put("POO_A", createCourseSectionIfNotExists(
                    "Turma A - Noturno",
                    "Sala B-101",
                    pooSubject,
                    term2025_2,
                    profRossi
            ));
        }

        if (term2025_2 != null && profaAlves != null && dbSubject != null) {
            createdSections.put("DB_A", createCourseSectionIfNotExists(
                    "Turma Única - Matutino",
                    "Lab. V",
                    dbSubject,
                    term2025_2,
                    profaAlves
            ));
        }

        if (term2025_2 != null && profaAlves != null && tlSubject != null) {
            createdSections.put("TL_A", createCourseSectionIfNotExists(
                    "Turma A - Noturno",
                    "Auditório C",
                    tlSubject,
                    term2025_2,
                    profaAlves
            ));
        }
        Teacher profFerreira = teachers.get("FERREIRA");
        Teacher profSantos = teachers.get("SANTOS");
        Teacher profMartins = teachers.get("MARTINS");
        Teacher profNogueira = teachers.get("NOGUEIRA");
        Teacher profRamos = teachers.get("RAMOS");
        Teacher profBarros = teachers.get("BARROS");

        Subject adsSubject = subjects.get("ADS");
        Subject statSubject = subjects.get("STAT");
        Subject mlSubject = subjects.get("ML");

        if (term2025_2 != null && profFerreira != null && adsSubject != null) {
            createdSections.put("ADS_B", createCourseSectionIfNotExists(
                    "Turma B - Vespertino",
                    "Sala B-202",
                    adsSubject,
                    term2025_2,
                    profFerreira
            ));
        }

        if (term2025_2 != null && profSantos != null && statSubject != null) {
            createdSections.put("STAT_A", createCourseSectionIfNotExists(
                    "Turma A - Matutino",
                    "Lab. Estatística 1",
                    statSubject,
                    term2025_2,
                    profSantos
            ));
        }

        if (term2025_2 != null && profMartins != null && mlSubject != null) {
            createdSections.put("ML_A", createCourseSectionIfNotExists(
                    "Turma A - Noturno",
                    "Sala de IA",
                    mlSubject,
                    term2025_2,
                    profMartins
            ));
        }

        if (term2025_2 != null && profNogueira != null && tlSubject != null) {
            createdSections.put("TL_B", createCourseSectionIfNotExists(
                    "Turma B - Matutino",
                    "Sala de Literatura",
                    tlSubject,
                    term2025_2,
                    profNogueira
            ));
        }

        if (term2025_2 != null && profRamos != null && adsSubject != null) {
            createdSections.put("ADS_C", createCourseSectionIfNotExists(
                    "Turma C - Noturno",
                    "Sala B-203",
                    adsSubject,
                    term2025_2,
                    profRamos
            ));
        }

        if (term2025_2 != null && profBarros != null && dbSubject != null) {
            createdSections.put("DB_B", createCourseSectionIfNotExists(
                    "Turma B - Vespertino",
                    "Lab. V2",
                    dbSubject,
                    term2025_2,
                    profBarros
            ));
        }


        log.info("Seeding de Turmas finalizado.");
        return createdSections;
    }

    private CourseSection createCourseSectionIfNotExists(String name, String room, Subject subject, AcademicTerm term, Teacher teacher) {
        return courseSectionRepository.findByNameAndAcademicTerm(name, term)
                .orElseGet(() -> {
                    log.info("Criando turma de exemplo: '{}' para a disciplina '{}'...", name, subject.getName());

                    var request = new CreateCourseSectionRequestDto(
                            name,
                            room,
                            subject.getId(),
                            term.getId(),
                            teacher.getId()
                    );

                    CourseSectionDetailsDto createdDto = courseSectionService.create(request);
                    log.info("Turma '{}' criada com sucesso.", createdDto.name());

                    return courseSectionRepository.findById(createdDto.id())
                            .orElseThrow(() -> new IllegalStateException("A turma acabou de ser criada, mas não foi encontrada."));
                });
    }

    private Map<String, Enrollment> seedEnrollments(Map<String, Student> students, Map<String, CourseSection> sections) {
        log.info("Iniciando seeding de Matrículas...");
        Map<String, Enrollment> createdEnrollments = new HashMap<>();

        Student alunoJoao = students.get("JOAO");
        Student alunaMaria = students.get("MARIA");
        Student alunoCarlos = students.get("CARLOS");
        Student alunaAna = students.get("ANA");
        Student alunoLucas = students.get("LUCAS");
        Student alunaFernanda = students.get("FERNANDA");

        CourseSection turmaPOO = sections.get("POO_A");
        CourseSection turmaDB = sections.get("DB_A");
        CourseSection turmaADS_B = sections.get("ADS_B");
        CourseSection turmaSTAT_A = sections.get("STAT_A");
        CourseSection turmaML_A = sections.get("ML_A");
        CourseSection turmaTL_B = sections.get("TL_B");
        CourseSection turmaADS_C = sections.get("ADS_C");
        CourseSection turmaDB_B = sections.get("DB_B");

        if (alunoJoao != null && turmaPOO != null) {
            createdEnrollments.put("JOAO_POO", createEnrollmentIfNotExists(alunoJoao, turmaPOO));
        }
        if (alunaMaria != null && turmaPOO != null) {
            createdEnrollments.put("MARIA_POO", createEnrollmentIfNotExists(alunaMaria, turmaPOO));
        }
        if (alunaMaria != null && turmaDB != null) {
            createdEnrollments.put("MARIA_DB", createEnrollmentIfNotExists(alunaMaria, turmaDB));
        }
        if (alunaFernanda != null && turmaADS_C != null) {
            createdEnrollments.put("FERNANDA_ADS", createEnrollmentIfNotExists(alunaFernanda, turmaADS_C));
        }
        if (alunoCarlos != null && turmaSTAT_A != null) {
            createdEnrollments.put("CARLOS_STAT", createEnrollmentIfNotExists(alunoCarlos, turmaSTAT_A));
        }
        if (alunaAna != null && turmaML_A != null) {
            createdEnrollments.put("ANA_ML", createEnrollmentIfNotExists(alunaAna, turmaML_A));
        }
        if (alunoLucas != null && turmaTL_B != null) {
            createdEnrollments.put("LUCAS_TL", createEnrollmentIfNotExists(alunoLucas, turmaTL_B));
        }
        if (alunoCarlos != null && turmaDB_B != null) {
            createdEnrollments.put("CARLOS_DB", createEnrollmentIfNotExists(alunoCarlos, turmaDB_B));
        }

        createdEnrollments.values().removeIf(Objects::isNull);

        log.info("Seeding de Matrículas finalizado.");
        return createdEnrollments;
    }

    private Enrollment createEnrollmentIfNotExists(Student student, CourseSection courseSection) {
        return enrollmentRepository.findByStudentAndCourseSection(student, courseSection)
                .orElseGet(() -> {
                    log.info("Matriculando o aluno '{}' na turma '{}'...", student.getFirstName(), courseSection.getName());

                    var enrollmentRequest = new CreateEnrollmentRequestDto(
                            student.getId(),
                            courseSection.getId(),
                            LocalDate.now()
                    );

                    try {
                        EnrollmentDetailsDto createdEnrollmentDto = enrollmentService.enrollStudent(enrollmentRequest);
                        log.info("Matrícula para o aluno '{}' na turma '{}' criada com sucesso.",
                                student.getFirstName(), courseSection.getName());
                        return enrollmentRepository.findById(createdEnrollmentDto.id())
                                .orElseThrow(() -> new IllegalStateException("A matrícula acabou de ser criada, mas não foi encontrada."));
                    } catch (Exception e) {
                        log.error("Não foi possível realizar a matrícula de teste para o aluno '{}' na turma '{}': {}",
                                student.getFirstName(), courseSection.getName(), e.getMessage());
                        return null;
                    }
                });
    }

    private Map<String, Employee> seedEmployees(Institution institution) {
        log.info("Iniciando seeding de Funcionários...");
        Map<String, Employee> createdEmployees = new HashMap<>();

        createdEmployees.put("ROBERTO", createEmployeeIfNotExists(
                "Roberto",
                "Gomes",
                "roberto.gomes@instituicao.com",
                JobPosition.COORDINATOR,
                LocalDate.of(2022, 5, 20),
                new DocumentDto("NATIONAL_ID", "33344455566"),
                institution
        ));

        createdEmployees.put("ANA", createEmployeeIfNotExists(
                "Ana",
                "Pereira",
                "ana.pereira@instituicao.com",
                JobPosition.SECRETARY,
                LocalDate.of(2023, 1, 10),
                new DocumentDto("NATIONAL_ID", "77788899900"),
                institution
        ));

        log.info("Seeding de Funcionários finalizado.");
        return createdEmployees;
    }

    private Employee createEmployeeIfNotExists(String firstName, String lastName, String email, JobPosition position, LocalDate hiringDate, DocumentDto document, Institution institution) {
        Optional<Person> existingPerson = personRepository.findByEmail(email);
        if (existingPerson.isPresent()) {
            if (existingPerson.get() instanceof Employee) {
                return (Employee) existingPerson.get();
            }
            log.warn("Uma pessoa com o e-mail {} já existe, mas não é um funcionário. Nenhum funcionário foi criado.", email);
            return null;
        }

        log.info("Criando funcionário de exemplo: {} {}...", firstName, lastName);
        Set<RoleName> rolesForEmployee = new HashSet<>();
        rolesForEmployee.add(RoleName.ROLE_EMPLOYEE);

        var employeeRequest = new CreateEmployeeRequestDto(
                firstName,
                lastName,
                email,
                position.getDisplayName(),
                hiringDate,
                institution.getId(),
                1L,
                document,
                rolesForEmployee
        );

        try {
            EmployeeResponseDto createdEmployeeDto = employeeService.createEmployee(employeeRequest);
            log.info("Funcionário '{}' e seu usuário correspondente foram criados com sucesso.", createdEmployeeDto.getEmail());

            return employeeRepository.findById(createdEmployeeDto.getId())
                    .orElseThrow(() -> new IllegalStateException("O funcionário acabou de ser criado, mas não foi encontrado."));
        } catch (Exception e) {
            log.error("Erro ao criar o funcionário de exemplo '{}': {}", email, e.getMessage());
            return null;
        }
    }

    private Map<String, AssessmentDefinition> seedAssessmentDefinitions(Map<String, CourseSection> courseSections) {
        log.info("Iniciando seeding de Definições de Avaliação...");
        Map<String, AssessmentDefinition> createdDefinitions = new HashMap<>();

        CourseSection pooSection = courseSections.get("POO_A");
        if (pooSection != null) {
            createdDefinitions.put("Prova 1", createAssessmentDefinitionIfNotExists("Prova 1", "EXAM", pooSection, new BigDecimal("40.0")));
            createdDefinitions.put("Projeto Final", createAssessmentDefinitionIfNotExists("Projeto Final", "PROJECT", pooSection, new BigDecimal("60.0")));
        }

        CourseSection bdSection = courseSections.get("DB_A");
        if (bdSection != null) {
            createdDefinitions.put("Projeto Final BD", createAssessmentDefinitionIfNotExists("Projeto Final", "PROJECT", bdSection, new BigDecimal("100.0")));
        }

        return createdDefinitions;

    }

    private AssessmentDefinition createAssessmentDefinitionIfNotExists(String title, String type, CourseSection section, BigDecimal weight) {
        return assessmentDefinitionRepository.findByTitleAndCourseSection(title, section)
                .orElseGet(() -> {
                    log.info("Criando definição de avaliação: {}", title);
                    var request = new CreateAssessmentDefinitionRequestDto(
                            section.getId(),
                            title,
                            type,
                            LocalDate.now().plusMonths(2),
                            weight
                    );
                    AssessmentDefinitionDto dto = assessmentDefinitionService.create(request);
                    return assessmentDefinitionRepository.findById(dto.id()).orElseThrow();
                });
    }

}