# Digital Bank API

Esta é uma API (simplificada) para gerenciamento de contas bancárias, transferências e consultas de extrato, desenvolvida com foco em **alta performance, consistência de dados e escalabilidade**.

---

## Stack Utilizada

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.x
* **Banco de Dados:** PostgreSQL
* **Versionamento de Banco:** Liquibase
* **Containerização:** Docker & Docker Compose
* **Testes:** JUnit e Mockito
* **Documentação:** Swagger/OpenAPI

---

## Decisões de Design e Arquitetura

A arquitetura foi criada seguindo princípios de **Clean Architecture** e **DDD (Domain-Driven Design)**, priorizando o desacoplamento e a testes.

### 1. Camadas e Responsabilidades
* **Domain:** centralizei as regras de negócio (como validação de saldo e débitos/créditos) na própria entidade Account. Isso garante que a integridade dos dados seja responsabilidade da entidade.
* **Service:** O TransferService é responsável apenas por orquestrar a transação: buscar dados, chamar a lógica do domínio e persistir.
* **API/Controller:** DTOs com Records, para imutabilidade e contrato limpo.

### 2. Consistência e Concorrência
* **Optimistic Locking:** Implementei o controle de versão (@Version) na entidade Account. Isso previne Lost Update em cenários de alta concorrência sem travar o banco de dados, o que aumentaria a latência.
* **Atomicidade:** O @Transactional garante que débito, crédito e histórico sejam sempre gravados juntos.
* **Idempotência:** Implementei uma chave unica na tabela transaction para evitar operações duplicadas.
  * **Resiliencia:** Adicionei uma estratégia de retry para o envio de notificações, caso a API externa falhe.

### 3. Persistência
* **Liquibase:** Utilizei o Liquibase para versionamento do banco de dados (PostgreSQL). Isso permite uma evolução controlada do banco e reproduzível em qualquer ambiente.
* **Índices:** Criei um índice composto em Transaction (account_id, created_at) para consultas de extrato mais performáticas em grande volume.

### 4. Tratamento de erros
* Implementei um GlobalExceptionHandler que captura exceções de negócio e de infraestrutura e traduz exceções em status semânticos: 409 Conflict para concorrência e 422 Unprocessable Entity para regras de negócio.

---

## ⚙️ Como Rodar o Projeto

Após os próximos passos a aplicação estará disponível em http://localhost:8080.

### 1. Banco de dados
O projeto utiliza Docker apenas para o banco de dados. Com ele instalado execute:
```bash
docker-compose up -d
```
Após isso estará disponível na porta 5432.

### 2. Rodar a Aplicação (Spring Boot)
Com o banco ativo, você pode subir a aplicação via Maven ou IDE:
```bash
mvn clean install
mvn spring-boot:run
```
O Liquibase detectará o banco e executará todas as migrações (criação de tabelas, índices e dados iniciais) automaticamente no primeiro log da aplicação.

### 3. Swagger/OpenAPI
Com a aplicação rodando, o swagger estará disponível em:

* http://localhost:8080/swagger-ui/index.html

### 3. Execução dos testes

Você pode executar os testes via IDE ou utilizando o comando:
```bash
mvn test
```

---