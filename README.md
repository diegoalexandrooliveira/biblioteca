# Biblioteca

Software para gerenciar uma biblioteca.

Aplicação desenvolvida usando as tecnologias/ferramentas:

* Java 11

* Spring Boot

* PostgreSQL

* ElasticSearch

* Redis

* Redhat Keycloak

* Confluent Kafka

* Docker

* Kubernetes

## Principais funcionalidades

#### Seção do administrador

##### Gerenciamento de usuários

Cadastrar, inativar, consultar

##### Gerenciamento de livros

Incluir, alterar informações, excluir, incluir cópias, remover cópias

##### Gerenciamento de empréstimos

Consultar empréstimos, devolução, criar empréstimo para um usuário, aprovar empréstimo

#### Seção do usuário

##### Meu perfil

Alterar dados, inativar conta

##### Meus empréstimos

Consultar empréstimos, solicitar empréstimo, desistir de um empréstimo

##### Livros

Buscar livros disponíveis

## Contextos e responsabilidades

Aqui serão descritos os serviços e suas funcionalidades

#### Clientes (microservice-clientes)

- Um administrador pode consultar, cadastrar e inativar os usuários da biblioteca
- Um cliente pode ver seus dados, alterar e inativar sua conta

#### Livros (microservice-livros)

- Um administrador pode consultar todos os livros, incluir, alterar, excluir. Outra responsabilidade deste microserviço é a possibilidade de incluir e remover cópias de um livro

#### Empréstimos (microservice-emprestimos)

- Um administrador pode consultar todos empréstimos, realizar devolução, criar um empréstimo para um cliente e aprovar os empréstimos solicitados
- Um cliente pode buscar livros disponíveis, consultar seus empréstimos, solicitar empréstimo e disistir de um empréstimo ainda não efetivado
- 
