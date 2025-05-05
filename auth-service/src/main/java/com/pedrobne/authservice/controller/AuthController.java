package com.pedrobne.authservice.controller;

import com.pedrobne.authservice.dto.AuthValidationResponse;
import com.pedrobne.authservice.dto.AuthenticatedUserDTO;
import com.pedrobne.authservice.dto.LoginRequestDTO;
import com.pedrobne.authservice.dto.UserResponseDTO;
import com.pedrobne.authservice.repository.UserRepository;
import com.pedrobne.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public List<UserResponseDTO> listUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getCpf(),
                        user.getName(),
                        user.getLogin(),
                        user.getRole().name()
                ))
                .toList();
    }

    @PostMapping("/autenticar")
    public ResponseEntity<AuthenticatedUserDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/renovar-ticket")
    public ResponseEntity<Boolean> renewToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.renewToken(token));
    }

    @GetMapping("/validar-token")
    public ResponseEntity<AuthValidationResponse> validateToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateToken(token));
    }
}
