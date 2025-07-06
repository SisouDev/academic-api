package com.institution.management.academic_api.application.service.auth;

import com.institution.management.academic_api.application.dto.auth.LoginRequestDto;
import com.institution.management.academic_api.application.dto.auth.LoginResponseDto;
import com.institution.management.academic_api.application.dto.user.UserSummaryDto;
import com.institution.management.academic_api.domain.model.enums.common.NotificationType;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import com.institution.management.academic_api.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final NotificationService notificationService;


    @Transactional
    @LogActivity("Fez login.")
    public LoginResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.login(),
                        request.password()
                )
        );

        var user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação bem-sucedida."));

        var jwtToken = jwtService.generateToken(user);

        var userSummary = new UserSummaryDto(
                user.getId(),
                user.getLogin(),
                user.getPerson().getFirstName() + " " + user.getPerson().getLastName()
        );

        notificationService.createNotification(
                user,
                "Bem-vindo(a) de volta, " + user.getPerson().getFirstName() + "!",
                "/",
                NotificationType.GENERAL_INFO
        );

        return new LoginResponseDto(jwtToken, userSummary);
    }
}