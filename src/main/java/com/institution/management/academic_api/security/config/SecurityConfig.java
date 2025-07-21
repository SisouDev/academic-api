package com.institution.management.academic_api.security.config;

import com.institution.management.academic_api.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/uploads/**", "/ws/**").permitAll()

                        // 2. REGRAS ESPECÍFICAS POR ENDPOINT (As mais importantes vêm aqui)
                        // RH & Financeiro
                        .requestMatchers("/api/v1/employees/**").hasAnyRole("ADMIN", "HR_ANALYST", "FINANCE")
                        .requestMatchers("/api/v1/leave-requests/**").hasAnyRole("HR_ANALYST", "MANAGER", "ADMIN")

                        // Docente
                        .requestMatchers("/api/v1/assessments/**").hasRole("TEACHER")
                        .requestMatchers("/api/v1/announcements/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers("/api/v1/lessons/**").hasRole("TEACHER")
                        .requestMatchers("/api/v1/lesson-contents/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/enrollments/attendance", "/api/v1/teacher-notes").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/teachers/{id}/status").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/teacher-notes/**").hasRole("TEACHER")
                        .requestMatchers("/api/v1/assessment-definitions/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers("/api/v1/lesson-plans/**").hasAnyRole("TEACHER", "ADMIN")

                        // TI
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/support-tickets/**").hasAnyRole("TECHNICIAN", "ADMIN")

                        // 3. REGRAS GENÉRICAS PARA USUÁRIOS AUTENTICADOS (Menos específicas)
                        .requestMatchers(HttpMethod.POST, "/api/v1/leave-requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/support-tickets").authenticated()
                        .requestMatchers("/api/v1/meetings/**").authenticated()
                        .requestMatchers("/api/v1/internal-requests/**").authenticated()

                        // 4. REGRA GENÉRICA FINAL PARA QUALQUER GET
                        .requestMatchers(HttpMethod.GET).authenticated()

                        // 5. REGRA DE FALLBACK (Qualquer outra coisa não permitida acima, só para ADMIN)
                        .anyRequest().hasRole("ADMIN")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}