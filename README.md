# 🎓 Academic API

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-success?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Relational%20DB-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue?logo=docker)
![Build](https://img.shields.io/badge/Build-Maven-yellow?logo=apachemaven)

> API RESTful segura, escalável e moderna, desenvolvida em **Java + Spring Boot**, para gerenciar cursos, estudantes, matrículas e usuários em plataformas educacionais.

---

## 🚀 Visão Geral

O projeto **Academic API** é um backend educacional desenvolvido com foco em escalabilidade, segurança e boas práticas. Ele permite o controle de usuários, papéis, cursos e matrículas com autenticação via **JWT** e controle de acesso por papéis (**RBAC**).

---

## 🧰 Tecnologias Principais

| Camada             | Ferramenta                  |
|--------------------|-----------------------------|
| Linguagem          | Java 17                     |
| Framework          | Spring Boot 3.x             |
| Banco de Dados     | PostgreSQL                  |
| Segurança          | Spring Security + JWT       |
| Persistência       | Spring Data JPA + Hibernate |
| Build              | Maven                       |
| Containerização    | Docker + Docker Compose     |
| Utilitários        | Lombok, MapStruct           |
| Testes             | JUnit, Mockito              |

---

## ✨ Funcionalidades

- ✅ Autenticação e autorização com **JWT**
- 🔐 Controle de acesso baseado em **roles** (ADMIN, STUDENT, TEACHER)
- 📚 CRUD completo de **cursos**
- 👨‍🎓 Gerenciamento de **alunos** e **matrículas**
- 📦 Deploy local via **Docker Compose**
- 💬 Tratamento de exceções com respostas padronizadas
- 📄 Separação clara entre **entidades, DTOs, serviços, controllers e repositórios**

---

## 📦 Estrutura do Projeto

```
academic-api/
├── src/
│   └── main/
│       ├── java/com/sisoudev/academic/
│       │   ├── config/
│       │   ├── security/
│       │   ├── user/
│       │   ├── course/
│       │   ├── student/
│       │   ├── enrollment/
│       │   └── exception/
│       └── resources/
│           └── application.yml
├── Dockerfile
├── compose.yaml
├── .env.example
└── pom.xml
```

---

## ⚙️ Como executar localmente

### Pré-requisitos

- Java 17 ou superior
- Apache Maven
- Docker + Docker Compose

### Passo a passo

```bash
# Clone o repositório
git clone https://github.com/SisouDev/academic-api.git
cd academic-api

# Crie o arquivo de ambiente
cp .env.example .env

# Configure seu .env (usuário/senha do banco e JWT_SECRET)

# Execute com Docker Compose
docker-compose up --build
```

A API estará disponível em: [http://localhost:8080](http://localhost:8080)

---

## 🔐 Autenticação

- Registro: `POST /api/auth/register`
- Login: `POST /api/auth/login` → retorna um token JWT
- Use o token nos headers:
```http
Authorization: Bearer <seu_token>
```

---

## 🧪 Endpoints principais

| Método | Rota                                 | Função                                     | Permissão    |
|--------|--------------------------------------|--------------------------------------------|--------------|
| POST   | /api/auth/register                   | Registrar usuário                          | Público      |
| POST   | /api/auth/login                      | Autenticar e gerar JWT                     | Público      |
| GET    | /api/courses                         | Listar cursos                              | Todos        |
| POST   | /api/courses                         | Criar curso                                | ADMIN        |
| GET    | /api/students/{id}                  | Ver perfil do aluno                        | SELF/ADMIN   |
| POST   | /api/students/{sid}/enroll/{cid}    | Matricular aluno em curso                  | ADMIN        |
| GET    | /api/students/{id}/courses          | Listar cursos de um aluno                  | SELF/ADMIN   |

---

## 🛡️ Segurança

- Senhas são armazenadas com hash (BCrypt)
- JWT assinado com chave secreta (`JWT_SECRET`)
- Permissões baseadas em papéis (ROLE_ADMIN, ROLE_STUDENT, etc.)

---

## 🧠 Conceitos aplicados

- Programação orientada a objetos
- Arquitetura em camadas
- API RESTful
- Boas práticas com DTOs e services
- Testes automatizados
- Clean Code
- Princípios SOLID

---

## 📚 Futuras melhorias

- Integração com frontend (React)
- Painel de administração com estatísticas
- Swagger/OpenAPI para documentação automatizada
- Upload de arquivos e imagens de perfil
- Internacionalização (i18n)

---

## 👩‍💻 Desenvolvido por

**Maria Karolina (SisouDev)**  
📍 Ciência da Computação — UFJF  
🔗 [LinkedIn](https://www.linkedin.com/in/sisoudev) • [GitHub](https://github.com/SisouDev) • 💌 karolinasingulani2@gmail.com
