# 📝 REGINOTES - Full-Cycle Application

Uma aplicação robusta e de ponta a ponta (Full-Cycle) para gerenciamento de notas e tags, composta por um **Backend escalável em Java 21**, um **Frontend reativo em Angular** e uma infraestrutura moderna focada em **Segurança Avançada**, **Práticas DevOps (CI/CD)** e **Containers**.

Este projeto simula o cenário real do mercado corporativo, integrando desenvolvimento de software, segurança rígida, automação de esteiras e arquitetura baseada em containers.

## 🚀 Funcionalidades e Diferenciais de Arquitetura

### 🧠 Backend & Segurança Avançada (Spring Security + JWT)
* **Domínio da Filter Chain:** Implementação customizada dos filtros do Spring Security para validação e interceptação de requisições via **JWT (JSON Web Tokens)**. Gerenciamento intencional do contexto de segurança, superando as abstrações padrão do ecossistema Spring.
* **Segurança Stateless:** Mecanismo de autenticação robusto com validação rigorosa de tokens na porta de entrada da API.
* **Limpeza Automatizada (CronJobs):** Tarefa agendada nativa do Spring (`@Scheduled`) configurada via expressões **Cron** para executar rotinas automáticas de manutenção no banco de dados em background.
* **Tratamento Global de Exceções:** Uso de `@RestControllerAdvice` para capturar exceções internas (erros de autenticação, violações de integridade) e convertê-las em respostas JSON padronizadas (RFC 7807), evitando o vazamento de stacktraces para o cliente.

### 🛢️ Alta Performance de Banco (PostgreSQL)
* Uso de `UUID` nativo para chaves primárias e estrangeiras, garantindo segurança contra enumeração de IDs e melhor distribuição em sistemas distribuídos.
* Substituição de consultas de paginação custosas por queries nativas otimizadas e uso de `EXISTS` para validação de integridade referencial em tempo constante $O(1)$.

### 🎨 Frontend Reativo (Angular)
* Interface modular e componentizada desenvolvida com **Angular**, consumindo a API RESTful de forma assíncrona.
* Gerenciamento de estado local para filtros avançados de notas baseados em múltiplas Tags dinâmicas.
* Sistema de rotas protegido por Guards, garantindo que telas internas só sejam acessadas por usuários autenticados.

### 🌐 DevOps, Containerização e CI/CD
* **Paridade de Ambiente com Docker:** Uso de Docker e Docker Compose para orquestrar múltiplos containers (Aplicação Spring Boot e Banco de Dados PostgreSQL). Garante que o projeto rode de forma idêntica em desenvolvimento, homologação e produção.
* **Documentação Viva (OpenAPI / Swagger):** Contrato de API gerado dinamicamente e interface interativa embarcada com cadeados de autenticação ativados para testes de endpoints em tempo real.
* **Esteira de CI/CD (GitHub Actions):** Automação completa do ciclo de vida do software. Cada push engatilha uma esteira que executa de forma automática o processo de **Build, Teste e Deploy**, garantindo a confiabilidade do código antes de ir para o ambiente de execução.

## 🛠️ Tecnologias Utilizadas

* **Java 21** & **Spring Boot 3** (Web, Data JPA, Security, Validation)
* **Angular** (Frontend SPA)
* **PostgreSQL** (Banco de dados relacional robusto)
* **JJWT** (Geração e validação de tokens de segurança)
* **SpringDoc OpenAPI / Swagger** (Documentação interativa da API)
* **Docker & Docker Compose** (Conteinerização e orquestração)
* **GitHub Actions** (Automação de CI/CD)

## ⚙️ Como Executar o Projeto

A forma padrão e recomendada de executar toda a infraestrutura da aplicação (banco de dados + backend) é através de containers Docker.

**1. Clone o repositório:**
```bash
git clone [https://github.com/SeuUsuario/reginotes.git](https://github.com/SeuUsuario/reginotes.git)
cd reginotes
