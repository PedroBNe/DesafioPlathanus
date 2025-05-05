package com.pedrobne.authservice.service;

import com.pedrobne.authservice.dto.AuthValidationResponse;
import com.pedrobne.authservice.dto.AuthenticatedUserDTO;
import com.pedrobne.authservice.dto.LoginRequestDTO;
import com.pedrobne.authservice.model.User;
import com.pedrobne.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticatedUserDTO authenticate(LoginRequestDTO request) {
        log.info("[AuthService] Tentando autenticar login: {}", request.login());

        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> {
                    log.warn("[AuthService] Usuário não encontrado para login: {}", request.login());
                    return new RuntimeException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("[AuthService] Senha inválida para login: {}", request.login());
            throw new RuntimeException("Invalid credentials");
        }

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        log.info("[AuthService] Usuário autenticado: {} | Token: {}", user.getLogin(), token);

        return new AuthenticatedUserDTO(
                user.getLogin(),
                user.getName(),
                token,
                user.getRole() == User.Role.VENDEDOR,
                true
        );
    }

    public boolean renewToken(String token) {
        log.info("[AuthService] Solicitando renovação do token: {}", token);

        return userRepository.findByToken(token)
                .map(user -> {
                    if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
                        log.warn("[AuthService] Token expirado: {}", token);
                        return false;
                    }
                    user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
                    userRepository.save(user);
                    log.info("[AuthService] Token renovado com sucesso: {}", token);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("[AuthService] Token não encontrado para renovação: {}", token);
                    return false;
                });
    }

    public AuthValidationResponse validateToken(String token) {
        log.info("[AuthService] Validando token: {}", token);

        return userRepository.findByToken(token)
                .map(user -> {
                    log.info("[AuthService] Token encontrado para login: {}", user.getLogin());
                    if (user.getTokenExpiration().isAfter(LocalDateTime.now())) {
                        log.info("[AuthService] Token válido até: {}", user.getTokenExpiration());
                        return new AuthValidationResponse(
                                user.getLogin(),
                                user.getName(),
                                user.getRole().name(),
                                true
                        );
                    } else {
                        log.warn("[AuthService] Token expirado para login: {}", user.getLogin());
                        throw new RuntimeException("Expired token");
                    }
                })
                .orElseThrow(() -> {
                    log.warn("[AuthService] Token inválido ou não encontrado: {}", token);
                    return new RuntimeException("Invalid or expired token");
                });
    }
}