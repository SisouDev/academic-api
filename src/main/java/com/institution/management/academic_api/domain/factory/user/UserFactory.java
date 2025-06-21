package com.institution.management.academic_api.domain.factory.user;
import com.institution.management.academic_api.application.dto.user.CreateUserRequestDto;
import com.institution.management.academic_api.application.mapper.simple.user.UserMapper;
import com.institution.management.academic_api.domain.model.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User create(CreateUserRequestDto requestDto) {

        User user = userMapper.toEntity(requestDto);

        String hashedPassword = passwordEncoder.encode(requestDto.password());
        user.setPasswordHash(hashedPassword);
        user.setActive(true);

        return user;
    }
}