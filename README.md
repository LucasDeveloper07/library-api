# Library API

API REST para gerenciamento de **livros** e **autores**, desenvolvida com **Java 21**, **Spring Boot** e **PostgreSQL**. O projeto conta com autenticação e autorização via **Spring Security**, **OAuth2** (login social com Google) e tokens **JWT**, além de um **Authorization Server** próprio, que está em desenvolvimento.

> **Status: em desenvolvimento.** Novas funcionalidades e melhorias estão sendo adicionadas continuamente.

---

## Funcionalidades

- CRUD completo de **livros**, **autores** e cadastro de **usuários** e **clients** OAuth2
- Pesquisa de livros com **filtros combináveis** (ISBN, título, nome do autor, gênero e ano de publicação) e **paginação**, usando *JPA Specifications*
- Pesquisa de autores por nome e nacionalidade
- **Autenticação e autorização** com Spring Security:
    - Login com usuário e senha (senhas criptografadas com BCrypt)
    - Login social com **Google (OAuth2)**
    - Tokens **JWT** (validade de 60 minutos)
    - Controle de acesso por perfil (`OPERADOR` e `GERENTE`)
- **Authorization Server** (Spring Authorization Server) com clients cadastrados no banco de dados
- **Validações** de entrada com Bean Validation (incluindo validação de ISBN)
- **Regras de negócio**:
    - Não permite cadastrar ISBN duplicado
    - Não permite cadastrar autor duplicado (mesmo nome, data de nascimento e nacionalidade)
    - Preço é obrigatório para livros publicados a partir de 2020
- **Tratamento global de erros** com respostas padronizadas
- **Auditoria** automática de data de cadastro e de atualização
- Padrão **DTO** com mapeamento via **MapStruct**

---

##  Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot |
| Persistência | Spring Data JPA, Hibernate |
| Banco de dados | PostgreSQL (em container Docker) |
| Segurança | Spring Security, OAuth2 Client, Spring Authorization Server, JWT |
| Validação | Bean Validation (Hibernate Validator) |
| Mapeamento | MapStruct |
| Produtividade | Lombok |
| Build | Maven |
| Contêineres | Docker |
| Administração do banco | pgAdmin 4 |

---

## Estrutura do projeto

````
src/main/java/io/github/LucasDeveloper07/libraryapi
├── config/          # Configurações de segurança e Authorization Server
├── controller/      # Endpoints REST
│   ├── dto/         # Objetos de entrada e saída
│   ├── mappers/     # Mapeamento DTO <-> Entidade (MapStruct)
│   └── common/      # Tratamento global de exceções
├── exceptions/      # Exceções de negócio
├── model/           # Entidades JPA
├── repository/      # Repositórios e Specifications
├── security/        # Autenticação, login social e clients OAuth2
├── service/         # Regras de negócio
└── validator/       # Validadores de regras de negócio
````

---

## Pré-requisitos

- [Java 21](https://adoptium.net/)
- [Docker](https://www.docker.com/) (para o PostgreSQL e o pgAdmin)
- Maven (ou use o Maven Wrapper `./mvnw` incluído no projeto)
- Credenciais OAuth2 do Google (para o login social): [Google Cloud Console](https://console.cloud.google.com/apis/credentials)

---

## Como executar

### 1. Clone o repositório

````bash
git clone https://github.com/LucasDeveloper07/[NOME-DO-REPOSITORIO].git
cd [NOME-DO-REPOSITORIO]
````

### 2. Baixe as imagens e suba o PostgreSQL e o pgAdmin com Docker

Baixe as imagens do **PostgreSQL** e do **pgAdmin 4** (interface web para visualizar os dados do banco):

````bash
docker pull postgres:16
docker pull dpage/pgadmin4
````

Crie uma rede para que os dois containers se comuniquem:

````bash
docker network create library-network
````

Suba o **PostgreSQL**:

````bash
docker run --name library-db \
  --network library-network \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=sua_senha \
  -e POSTGRES_DB=library \
  -p 5432:5432 \
  -v library-data:/var/lib/postgresql/data \
  -d postgres:16
````

Suba o **pgAdmin 4**:

````bash
docker run --name library-pgadmin \
  --network library-network \
  -e PGADMIN_DEFAULT_EMAIL=admin@admin.com \
  -e PGADMIN_DEFAULT_PASSWORD=admin \
  -p 5050:80 \
  -d dpage/pgadmin4
````

| Serviço | Endereço | Credenciais |
|---|---|---|
| PostgreSQL | `localhost:5432` | usuário `postgres`, senha `sua_senha`, banco `library` |
| pgAdmin 4 | http://localhost:5050 | e-mail `admin@admin.com`, senha `admin` |

> Ajuste usuário, senha, nome do banco e versão conforme o seu ambiente. O volume `library-data` mantém os dados salvos mesmo que o container seja removido.

**Visualizando os dados no pgAdmin**

1. Acesse http://localhost:5050 e faça login.
2. Clique em **Add New Server**.
3. Na aba **General**, dê um nome ao servidor (ex.: `library-db`).
4. Na aba **Connection**, preencha:
    - **Host name/address:** `library-db` (nome do container do PostgreSQL, pois os dois estão na mesma rede Docker)
    - **Port:** `5432`
    - **Maintenance database:** `library`
    - **Username:** `postgres`
    - **Password:** `sua_senha`
5. Salve e navegue em **Servers > library-db > Databases > library > Schemas > public > Tables**. Clique com o botão direito em uma tabela e escolha **View/Edit Data > All Rows**.

Para parar e iniciar os containers novamente:

````bash
docker stop library-db library-pgadmin
docker start library-db library-pgadmin
````

### 3. Crie as tabelas

O projeto usa `ddl-auto: none`, ou seja, o Hibernate **não** cria as tabelas automaticamente. Execute o script abaixo no banco antes de iniciar a aplicação (pode ser pelo Query Tool do pgAdmin):

````sql
CREATE TABLE usuario (
    id       uuid PRIMARY KEY,
    login    varchar(255),
    senha    varchar(255),
    email    varchar(255),
    roles    varchar[]
);

CREATE TABLE client (
    id            uuid PRIMARY KEY,
    client_id     varchar(255),
    client_secret varchar(255),
    redirect_uri  varchar(255),
    scope         varchar(255)
);

CREATE TABLE autor (
    id               uuid PRIMARY KEY,
    nome             varchar(100) NOT NULL,
    data_nascimento  date NOT NULL,
    nacionalidade    varchar(50) NOT NULL,
    data_cadastro    timestamp,
    data_atualizacao timestamp,
    id_usuario       uuid REFERENCES usuario(id)
);

CREATE TABLE livro (
    id               uuid PRIMARY KEY,
    isbn             varchar(20) NOT NULL,
    titulo           varchar(150) NOT NULL,
    data_publicacao  date,
    genero           varchar(30) NOT NULL,
    preco            numeric(18,2),
    id_autor         uuid REFERENCES autor(id),
    data_cadastro    timestamp,
    data_atualizacao timestamp,
    id_usuario       uuid REFERENCES usuario(id)
);
````

### 4. Configure as variáveis de ambiente

A aplicação lê suas configurações sensíveis de variáveis de ambiente (nenhuma credencial fica no código):

| Variável | Descrição | Exemplo |
|---|---|---|
| `DATABASE_URL` | URL de conexão JDBC | `jdbc:postgresql://localhost:5432/library` |
| `DATABASE_USERNAME` | Usuário do banco | `postgres` |
| `DATABASE_PASSWORD` | Senha do banco | `sua_senha` |
| `GOOGLE_CLIENT_ID` | Client ID do Google OAuth2 | `xxxx.apps.googleusercontent.com` |
| `GOOGLE_CLIENT_SECRET` | Client Secret do Google OAuth2 | `xxxx` |

**Linux / macOS:**

````bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/library
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=sua_senha
export GOOGLE_CLIENT_ID=seu_client_id
export GOOGLE_CLIENT_SECRET=seu_client_secret
````

**Windows (PowerShell):**

````powershell
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/library"
$env:DATABASE_USERNAME="postgres"
$env:DATABASE_PASSWORD="sua_senha"
$env:GOOGLE_CLIENT_ID="seu_client_id"
$env:GOOGLE_CLIENT_SECRET="seu_client_secret"
````

### 5. Execute a aplicação

````bash
./mvnw spring-boot:run
````

A API ficará disponível em `http://localhost:8080`.

---

## Autenticação e autorização

| Perfil | Permissões |
|---|---|
| `GERENTE` | Acesso total: livros, autores e clients |
| `OPERADOR` | Consulta, cadastro, atualização e exclusão de livros; consulta de autores |

- O cadastro de usuários (`POST /usuarios`) é público.
- Todos os demais endpoints exigem autenticação (Basic, login por formulário, login com Google ou token JWT).
- No login social com Google, um usuário novo é criado automaticamente com o perfil `OPERADOR`.
- O **Authorization Server** utiliza os fluxos `authorization_code` e `client_credentials`, com clients cadastrados na tabela `client` (via `POST /clients`, restrito a `GERENTE`).

---

## Endpoints

### Livros (`/livros`)

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| `POST` | `/livros` | Cadastra um livro | OPERADOR, GERENTE |
| `GET` | `/livros/{id}` | Detalhes de um livro | OPERADOR, GERENTE |
| `GET` | `/livros` | Pesquisa com filtros e paginação | OPERADOR, GERENTE |
| `PUT` | `/livros/{id}` | Atualiza um livro | OPERADOR, GERENTE |
| `DELETE` | `/livros/{id}` | Remove um livro | OPERADOR, GERENTE |

**Parâmetros da pesquisa** (todos opcionais): `isbn`, `titulo`, `nome-autor`, `genero`, `ano-publicacao`, `pagina` (padrão `0`) e `tamanho-pagina` (padrão `10`).

Gêneros disponíveis: `FICCAO`, `FANTASIA`, `MISTERIO`, `ROMANCE`, `BIOGRAFIA`, `CIENCIA`.

### Autores (`/autores`)

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| `POST` | `/autores` | Cadastra um autor | GERENTE |
| `GET` | `/autores/{id}` | Detalhes de um autor | OPERADOR, GERENTE |
| `GET` | `/autores` | Pesquisa por `nome` e `nacionalidade` | OPERADOR, GERENTE |
| `PUT` | `/autores/{id}` | Atualiza um autor | GERENTE |
| `DELETE` | `/autores/{id}` | Remove um autor | GERENTE |

### Usuários e clients

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/usuarios` | Cadastra um usuário | Público |
| `POST` | `/clients` | Cadastra um client OAuth2 | GERENTE |

---

## Exemplos de requisição

**Cadastrar autor**

````http
POST /autores
Content-Type: application/json

{
  "nome": "Machado de Assis",
  "dataNascimento": "1839-06-21",
  "nacionalidade": "Brasileira"
}
````

**Cadastrar livro**

````http
POST /livros
Content-Type: application/json

{
  "isbn": "978-85-359-0277-5",
  "titulo": "Dom Casmurro",
  "dataPublicacao": "1899-01-01",
  "genero": "ROMANCE",
  "preco": 39.90,
  "idAutor": "id-do-autor-cadastrado"
}
````

**Pesquisar livros**

````http
GET /livros?titulo=casmurro&genero=ROMANCE&pagina=0&tamanho-pagina=10
````

**Exemplo de resposta de erro** (`422 Unprocessable Content`)

````json
{
  "status": 422,
  "mensagem": "Erro de validação.",
  "erros": [
    {
      "campo": "preco",
      "erro": "Para livros com o ano de publicação a partir de 2020, o preço é obrigatório."
    }
  ]
}
````

### Códigos de resposta

| Status | Situação |
|---|---|
| `201 Created` | Recurso criado (header `Location` com a URL do novo recurso) |
| `204 No Content` | Atualização ou exclusão realizada com sucesso |
| `400 Bad Request` | Operação não permitida |
| `403 Forbidden` | Usuário sem permissão para a operação |
| `404 Not Found` | Recurso não encontrado |
| `409 Conflict` | Registro duplicado (ISBN ou autor) |
| `422 Unprocessable Content` | Erro de validação |

---

## 🧪 Testes

O projeto possui testes de repositório com JUnit para as operações de autores e livros (salvar, atualizar, listar, excluir e consultas personalizadas).

````bash
./mvnw test
````

---

##  Próximos passos

- [ ] Finalizar a implementação do Authorization Server
- [ ] Documentar a API com Swagger/OpenAPI
- [ ] Ampliar a cobertura de testes (serviços e controllers)
- [ ] Criar um `docker-compose.yml` para subir a aplicação, o banco e o pgAdmin com um único comando
- [ ] Usar migrations de banco de dados (Flyway)
- [ ] Configurar uma chave RSA fixa para assinatura dos tokens JWT

---

## Autor

**Lucas Felipe da Silva Araujo**
Estudante de Análise e Desenvolvimento de Sistemas (Fatec Ipiranga) | Back-end Java

[![LinkedIn](https://img.shields.io/badge/LinkedIn-lucasfelipedev-0A66C2?logo=linkedin&logoColor=white)](https://www.linkedin.com/in/lucasfelipedev)
[![GitHub](https://img.shields.io/badge/GitHub-LucasDeveloper07-181717?logo=github&logoColor=white)](https://github.com/LucasDeveloper07)