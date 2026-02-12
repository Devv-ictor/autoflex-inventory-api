# Autoflex Inventory API

Este é um projeto de backend desenvolvido em **Spring Boot** para o gerenciamento de inventário e sugestão de planos de produção. O sistema permite o cadastro de matérias-primas e produtos (com suas respectivas receitas) e sugere uma ordem de produção baseada no estoque disponível e no valor dos produtos.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.2**
- **Spring Data JPA** (Persistência de dados)
- **PostgreSQL** (Banco de dados)
- **Validation** (Validação de dados)
- **Lombok** (Produtividade)
- **SpringDoc OpenAPI (Swagger)** (Documentação da API)
- **Maven** (Gerenciador de dependências)

## 📋 Funcionalidades

- **Matérias-primas**: CRUD completo (Criar, Listar, Buscar por ID, Atualizar e Deletar).
- **Produtos**: 
  - Cadastro de produtos com receitas (vínculo com matérias-primas).
  - Listagem de produtos.
- **Plano de Produção**: Algoritmo que sugere quais produtos produzir e em qual quantidade, priorizando os de maior valor unitário para maximizar o faturamento com o estoque atual.

## 🛠️ Como Executar o Projeto Localmente

### Pré-requisitos
- JDK 21
- Maven 3.x
- Docker e Docker Compose (opcional, para o banco de dados)

### Passos
1. **Clonar o repositório**:
   ```bash
   git clone https://github.com/seu-usuario/autoflex-inventory-api.git
   cd autoflex-inventory-api
   ```

2. **Subir o banco de dados (via Docker)**:
   ```bash
   docker-compose up -d
   ```
   *O banco estará disponível em `localhost:5433`.*

3. **Rodar a aplicação**:
   ```bash
   mvn spring-boot:run
   ```

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, acesse a documentação interativa para testar os endpoints:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **JSON Docs**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## ☁️ Deploy no Railway

Este projeto está configurado para deploy automático no **Railway**. As seguintes variáveis de ambiente são utilizadas:

- `DATABASE_URL`: URL de conexão do PostgreSQL.
- `PGUSER`: Usuário do banco.
- `PGPASSWORD`: Senha do banco.

O projeto utiliza valores padrão caso essas variáveis não estejam presentes, facilitando o desenvolvimento local.

## 🧪 Testes

Para executar os testes unitários:
```bash
mvn test
```

## 🛡️ Tratamento de Erros e Validação

- **Validação**: Todas as entradas via DTO são validadas (Ex: nomes não vazios, valores positivos).
- **Exceções**: Retornos padronizados em caso de erro (404 para recursos não encontrados, 400 para erros de validação).

---
Desenvolvido como parte de um desafio técnico para Desenvolvedor Júnior.
