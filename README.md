# Sistema de Gerenciamento de Horas Trabalhadas - API RESTful

Este projeto é uma API RESTful para um sistema de gerenciamento de horas trabalhadas, desenvolvido com Spring Boot, Java 17, Postgres e Spring Security.

## Tecnologias Utilizadas

- **Java 17:** Linguagem de programação utilizada.
- **Spring Boot:** Framework para criação de aplicações Java.
- **PostgreSQL:** Banco de dados relacional.
- **Spring Security:** Framework para autenticação e autorização.

## Funcionalidades

- **Usuário Básico:** Acesso ás suas solicitações de horas extras.
- **Usuário Administrador:** Acesso à todas as solicitações das filiais, tabela de horas válidas.

## Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/joaoparqum/back-sght

2. Configure o banco de dados no application.properties:
     ```   
     spring.datasource.url=jdbc:postgresql://localhost:5432/sght-db
     spring.datasource.username=seu-usuario
     spring.datasource.password=sua-senha
  
