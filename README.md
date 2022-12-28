# Platform Builders: Desafio Técnico

## 1. Contexto

O objetivo proposto era a criação de uma aplicação capaz de receber um código de boleto bancário e data de pagamente, para que então calculássemos o valor do juros e devolvêssemos a informação ao usuário.

Mais detalhes a respeito do desafio podem ser visto [aqui](https://platformbuilders.notion.site/Desafio-T-cnico-483464fe010e4122b88499f4b3d625d9).

## 2. Sobre o projeto

O projeto em questão foi desenvolvido em **Java 18** (*18.0.2-open*), **Spring Boot** (*3.0.1*), e uma série de outras bibliotecas auxiliares conforme listadas abaixo.

| Grupo | Artefato | Escopo |
| -------- | -------- | -------- |
| org.springframework.boot | spring-boot-starter-data-jpa | *Compilação* |
| org.springframework.boot | spring-boot-starter-web | *Compilação* |
| com.mashape.unirest | unirest-java | *Compilação* |
| com.google.code.gson | gson | *Compilação* |
| com.h2database | h2 | *Execução* |
| org.springframework.boot     | spring-boot-starter-test | *Testes* |
| org.junit.jupiter | junit-jupiter-api | *Testes* |
| org.apache.commons | commons-lang3 | *Testes* |

### 2.1. Banco de dados

Como um dos requisitos do projeto era justamente a persistência dos cálculos em um banco de dados, me aproveitei das abstrações do **Hibernate** para utilizar um banco de dados em memória (**H2**), apenas a fim de demonstrações da prática.

No caso do surgimento da necessidade, bastaria substituir os drivers do **H2** pelo banco de dados desejado. Importante também mencionar que o **JPA** está configurado com a propriedade `spring.jpa.hibernate.ddl-auto` para `create-drop`, que também precisa ser alterado no caso de uma eventual troca de banco.

Graças a propriedade `spring.h2.console.enabled` habilitada, é possível verificar os cálculos sendo persistidos através de uma interface gráfica fornecida pela própria biblioteca.

A **URL** para acessar a interfáce gráfica do **H2** é: http://localhost:8080/h2-console

Uma vez na tela, basta recuperar as credenciais de acesso disponíveis no arquivo `src/main/resources/application.properties`, preencher os campos e acessar.

### 2.2. Sobre o Ambiente

#### 2.2.1. Requisitos mínimos
- **Java** *18.0.2-open* e **Apache Maven** *3.8.6*
- ... ou **Docker 20.10.22**

#### 2.2.2. Subindo com Docker

Se você possuir o Docker instalado, uma vez que você estiver na raíz do projeto, tudo o que precisa fazer é digitar o comando abaixo.

```bash
docker build --tag challenge:1.0 .
```

Feito isso, o **Docker** irá iniciar o processo de download das bibliotecas, compilação do projeto, e geração do **JAR** executável. O processo deve levar em torno de dois minutos para se concluir.

Uma vez que o **Docker** exiba a mensagem "*Successfully tagged challenge:1.0*", basta iniciar o contâiner com o seguinte comando, substituindo os pontos indicados:

```bash
docker run --name {{nome-container}} --env CLIENT_ID={{client-id}} --env CLIENT_SECRET={{client-secret}} -p 8080:8080 challenge:1.0
```

1. **Nome do contâiner** a ser gerado: `{{nome-container}}`;
2. **Chave do cliente** da *API de Boletos*: `{{client-id}}`;
3. **Senha do cliente** da *API de Boletos*: `{{client-secret}}`;

> A **chave** e **senha** da "API de Boletos" está disponível no primeiro link informado neste documento, onde há mais informações e detalhes sobre esta mesma aplicação.

Feito isto, a aplicação já deve estar funcionando.

#### 2.2.3. Subindo com Java e Maven

Uma vez que você estiver na raíz do projeto, digite o seguinte comando:

```bash
./mvnw -Dmaven.test.skip clean install
```

Após a instalação das dependências do projeto, e compilação do mesmo, se você acessar a pasta `/target`, provavelmente irá ver um arquivo chamado `challenge-1.0.jar`.

Nesta mesma pasta, então, rode o seguinte comando, substituindo os pontos indicados:

```bash
java -DclientId=$CLIENT_ID -DclientSecret=$CLIENT_SECRET -Dfile.encoding=UTF-8 -jar challenge-1.0.jar
```

1. **Chave do cliente** da *API de Boletos*: `$CLIENT_ID`;
2. **Senha do cliente** da *API de Boletos*: `$CLIENT_SECRET`;

> A **chave** e **senha** da "API de Boletos" está disponível no primeiro link informado neste documento, onde há mais informações e detalhes sobre esta mesma aplicação.

### 2.3. Endpoints

Para entender melhor como funciona a aplicação e sua API, por favor, leia a documentação disponível em `src/main/resources/documentation.yaml`.
