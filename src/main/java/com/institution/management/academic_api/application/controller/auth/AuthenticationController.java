package com.institution.management.academic_api.application.controller.auth;

import com.institution.management.academic_api.application.dto.auth.LoginRequestDto;
import com.institution.management.academic_api.application.dto.auth.LoginResponseDto;
import com.institution.management.academic_api.application.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}