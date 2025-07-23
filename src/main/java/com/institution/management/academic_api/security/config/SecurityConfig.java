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

                        // 2. REGRAS DE GESTÃO PARA RH E FINANCEIRO
                        .requestMatchers("/api/v1/employees/**").hasAnyRole("ADMIN", "HR_ANALYST", "FINANCE_MANAGER", "FINANCE_ASSISTANT")
                        .requestMatchers("/api/v1/hr/**").hasAnyRole("ADMIN", "HR_ANALYST")
                        .requestMatchers("/api/v1/payroll/**").hasAnyRole("ADMIN", "FINANCE_MANAGER", "FINANCE_ASSISTANT")
                        .requestMatchers("/api/v1/purchase-orders/**").hasAnyRole("ADMIN", "FINANCE_MANAGER", "FINANCE_ASSISTANT")
                        .requestMatchers("/api/v1/purchase-requests/**").hasAnyRole("ADMIN", "FINANCE_MANAGER", "FINANCE_ASSISTANT")
                        .requestMatchers("/api/v1/scholarships/**").hasAnyRole("ADMIN", "FINANCE_MANAGER")
                        .requestMatchers("/api/v1/salary-structures/**").hasAnyRole("ADMIN", "HR_ANALYST")

                        // 3. REGRAS DE GESTÃO PARA OUTROS CARGOS
                        .requestMatchers("/api/v1/leave-requests/**").hasAnyRole("ADMIN", "HR_ANALYST", "MANAGER")
                        .requestMatchers("/api/v1/internal-requests/**").hasAnyRole("ADMIN", "SECRETARY", "MANAGER")
                        .requestMatchers("/api/v1/calendar/events/**").hasAnyRole("ADMIN", "SECRETARY", "MANAGER")

                        .requestMatchers(HttpMethod.PATCH, "/api/v1/support-tickets/**").hasAnyRole("ADMIN", "TECHNICIAN")
                        .requestMatchers("/api/v1/lessons/**", "/api/v1/lesson-contents/**", "/api/v1/lesson-plans/**", "/api/v1/gradebook/**").hasRole("TEACHER")

                        .requestMatchers("/api/v1/it/**").hasAnyRole("ADMIN", "TECHNICIAN", "MANAGER")


                        // 4. REGRAS GENÉRICAS PARA USUÁRIOS AUTENTICADOS (Qualquer um logado pode fazer)
                        .requestMatchers(HttpMethod.POST, "/api/v1/support-tickets").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/leave-requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/purchase-requests").authenticated() // Permitindo qualquer funcionário criar a requisição simples
                        .requestMatchers(HttpMethod.GET).authenticated() // Permite que todos os GETs passem, a segurança será no @PreAuthorize do controller

                        // 5. REGRA FINAL DE FALLBACK (Qualquer outra coisa não permitida acima, só para ADMIN)
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