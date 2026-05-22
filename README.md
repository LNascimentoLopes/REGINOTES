
# 📝 REGINOTES API - Advanced Spring Boot

Uma API RESTful completa para gerenciamento de notas e tags, desenvolvida com foco em **Segurança Avançada**, **Performance de Banco de Dados**, **Resiliência** e **Práticas DevOps**.

Este projeto não é apenas um CRUD simples, mas uma aplicação que implementa padrões do mercado corporativo para autenticação, validação de dados, documentação e infraestrutura.

## 🚀 Funcionalidades e Diferenciais de Arquitetura

* **Paridade de Ambiente (Containerização com Docker):** Uso de Docker e Docker Compose. **Por que isso importa no mercado?** Garante que o projeto rode de forma idêntica na sua máquina, no servidor de testes e na nuvem, eliminando o clássico gargalo do "na minha máquina funciona".
* **Documentação Viva (OpenAPI / Swagger):** Contrato de API gerado dinamicamente e interface interativa embarcada. **Por que isso importa no mercado?** Elimina a dependência de PDFs ou coleções defasadas do Postman. Times de Front-End e QA (Quality Assurance) podem entender e testar a API imediatamente através do próprio navegador.
* **Segurança Stateless Avançada (JWT):** Implementação de Access Tokens de curta duração e Refresh Tokens de longa duração (Token Rotation) com armazenamento em banco de dados.
* **Token Revocation (Blacklist):** Sistema de Logout real. Access tokens invalidados são armazenados em uma tabela de Blacklist no PostgreSQL para bloqueio imediato nas requisições.
* **Limpeza Automatizada (CronJobs):** Tarefa agendada nativa do Spring (`@Scheduled`) para varrer e deletar tokens expirados do banco de dados automaticamente de madrugada.
* **Tratamento Global de Exceções (Para-raios):** Uso de `@RestControllerAdvice` para capturar exceções internas (como Tokens expirados, violações de integridade do banco e erros de validação) e convertê-las em respostas JSON padronizadas (RFC 7807).
* **Fail-Fast & Bean Validation:** Validação rigorosa na porta de entrada da API, incluindo uso de Expressões Regulares (Regex) para validação de formatos específicos (ex: Cores em HEX para Tags).
* **Alta Performance de Banco (PostgreSQL):**
* Uso de `UUID` nativo para chaves primárias e estrangeiras.
* Substituição de consultas de paginação custosas (como `COUNT`) por queries nativas com `EXISTS` para validação de integridade referencial em tempo constante $O(1)$.



## 🛠️ Tecnologias Utilizadas

* **Java 17 / 21**
* **Spring Boot 3** (Web, Data JPA, Security, Validation)
* **PostgreSQL** (Banco de dados relacional)
* **JJWT** (Geração e validação de JSON Web Tokens)
* **SpringDoc OpenAPI** (Integração e UI do Swagger)
* **Docker & Docker Compose** (Orquestração local e conteinerização)

## ⚙️ Como Executar o Projeto

No mercado, a forma padrão de executar infraestrutura (banco de dados + aplicação) é através de containers. Siga os passos abaixo:

**1. Clone o repositório:**

```bash
git clone https://github.com/SeuUsuario/note-taking-api.git
cd note-taking-api

```

**2. Configure as Variáveis de Ambiente:**
Crie um arquivo chamado `.env` na raiz do projeto (onde fica o `docker-compose.yml`) e adicione as credenciais. **Por que fazer isso?** Nunca chumbamos credenciais no código-fonte por questões graves de segurança cibernética.

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/notes_db
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password

```

**3. Suba a infraestrutura com Docker:**
No terminal, execute o comando abaixo para compilar a aplicação e iniciar os containers do PostgreSQL e do Spring Boot juntos:

```bash
docker-compose up --build

```

*(Para rodar em segundo plano, adicione `-d` ao final do comando).*

**4. Acesse a Aplicação e a Documentação:**
Com os containers rodando, o Spring Boot estará disponível na porta `8080`.

* **Health Check / API Base:** `http://localhost:8080`
* **Swagger UI (Documentação Interativa):** `http://localhost:8080/swagger-ui.html`

*(A partir do Swagger, você pode criar usuários, fazer login para gerar o JWT, colocar o token no botão "Authorize" e testar todos os endpoints sem precisar de ferramentas externas).*
