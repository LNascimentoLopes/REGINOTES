# 📝 REGINOTES API - Advanced Spring Boot

![Java](https://img.shields.io/badge/Java-17+-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-Stateless-success)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Relational-blue)
![JWT](https://img.shields.io/badge/JWT-JJWT-orange)

Uma API RESTful completa para gerenciamento de notas e tags, desenvolvida com foco em **Segurança Avançada**, **Performance de Banco de Dados** e **Resiliência**. 

Este projeto não é apenas um CRUD simples, mas uma aplicação que implementa padrões de mercado corporativo para autenticação, validação de dados e tratamento de exceções.

## 🚀 Funcionalidades e Diferenciais de Arquitetura

* **Segurança Stateless Avançada (JWT):** Implementação de Access Tokens de curta duração e Refresh Tokens de longa duração (Token Rotation) com armazenamento em banco de dados.
* **Token Revocation (Blacklist):** Sistema de Logout real. Access tokens invalidados são armazenados em uma tabela de Blacklist no PostgreSQL para bloqueio imediato nas requisições.
* **Limpeza Automatizada (CronJobs):** Tarefa agendada nativa do Spring (`@Scheduled`) para varrer e deletar tokens expirados do banco de dados automaticamente de madrugada.
* **Tratamento Global de Exceções (Para-raios):** Uso de `@RestControllerAdvice` para capturar exceções internas (como Tokens expirados, violações de integridade do banco e erros de validação) e convertê-las em respostas JSON padronizadas (RFC 7807).
* **Fail-Fast & Bean Validation:** Validação rigorosa na porta de entrada da API, incluindo uso de Expressões Regulares (Regex) para validação de formatos específicos (ex: Cores em HEX para Tags).
* **Alta Performance de Banco (PostgreSQL):** * Uso de `UUID` nativo para chaves primárias e estrangeiras.
  * Substituição de consultas de paginação custosas (como `COUNT`) por queries nativas com `EXISTS` para validação de integridade referencial $O(1)$.

## 🛠️ Tecnologias Utilizadas

* **Java 17 / 21**
* **Spring Boot 3** (Web, Data JPA, Security, Validation)
* **PostgreSQL** (Banco de dados relacional)
* **JJWT** (Geração e validação de JSON Web Tokens)

## ⚙️ Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/SeuUsuario/note-taking-api.git](https://github.com/SeuUsuario/note-taking-api.git)
