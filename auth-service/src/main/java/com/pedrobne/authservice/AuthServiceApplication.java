package com.pedrobne.authservice;

import com.pedrobne.authservice.model.User;
import com.pedrobne.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                User vendedor = User.builder()
                        .cpf("11111111111")
                        .name("Vendedor Exemplo")
                        .login("vendedor")
                        .password(encoder.encode("123"))
                        .role(User.Role.VENDEDOR)
                        .build();

                User cliente = User.builder()
                        .cpf("22222222222")
                        .name("Cliente Exemplo")
                        .login("cliente")
                        .password(encoder.encode("123"))
                        .role(User.Role.CLIENTE)
                        .build();

                repository.save(vendedor);
                repository.save(cliente);
            }
        };
    }

}
