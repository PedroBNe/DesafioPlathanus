# Concessionária de Automóveis — Sistema de Vendas

## Estrutura

- api-gateway/ # Roteamento e autenticação das requisições
- auth-service/ # Autenticação e gestão de usuários
- config-server/ # Configurações centralizadas (Spring Cloud Config)
- sales-service/ # Gerenciamento de carrinho e vendas
- vehicle-service/ # Gestão dos veículos disponíveis
- service-discovery/ # Registro de serviços (Eureka Server)
- frontend/ # Interface do usuário (Angular)

## Iniciar os microsserviços em ordem:

- 1° config-server
- 2° service-discovery (Eureka)
- 3° auth-service
- 4° vehicle-service
- 5° sales-service
- 6° api-gateway

- 7° front-end
