package com.gimmesomepeace.recipes.service;

import com.gimmesomepeace.recipes.dto.request.LoginRequest;
import com.gimmesomepeace.recipes.dto.request.RegistrationRequest;
import com.gimmesomepeace.recipes.dto.response.LoginResponse;
import com.gimmesomepeace.recipes.dto.response.UserResponse;
import com.gimmesomepeace.recipes.dto.response.UserShortResponse;
import com.gimmesomepeace.recipes.exception.AuthError;
import com.gimmesomepeace.recipes.exception.LoginAlreadyExistsException;
import com.gimmesomepeace.recipes.exception.LoginFailedException;
import com.gimmesomepeace.recipes.model.Role;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.UserRepository;
import com.gimmesomepeace.recipes.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserResponse register(RegistrationRequest request) {
        if (userRepository.existsByLogin(request.login())) {
            throw new LoginAlreadyExistsException(request.login());
        }
        User user = User.builder()
                .name(request.name())
                .login(request.login())
                .passwordHash(encoder.encode(request.password()))
                .build();

        user = userRepository.save(user);
        return UserResponse.from(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLogin(request.login()).orElseThrow(
                () -> new LoginFailedException(AuthError.INVALID_CREDENTIALS)
        );
        if (!encoder.matches(request.password(), user.getPasswordHash())) {
            throw new LoginFailedException(AuthError.INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(user);
        // TODO: Сделать норм вывод
        return new LoginResponse(token, "Bearer", 50, UserShortResponse.from(user));
    }
}
