# Projeto de Microserviços com Java

Este projeto é uma aplicação que tem como o objetivo de demonstrar como configurar e utilizar tecnologias modernas para criar um sistema escalável e distribuído.

## Estrutura do Projeto

O projeto é dividido em dois microserviços principais:

- **Worker**: Responsável por fornecer informações sobre trabalhadores, como nome e renda diária.
- **Payroll**: Responsável por calcular pagamentos com base nos dados fornecidos pelo serviço Worker.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para criação de aplicações Java.
- **Eureka**: Serviço de descoberta que permite que os microserviços se encontrem e se comuniquem.
- **OpenFeign**: Biblioteca para simplificar a comunicação entre microserviços através de chamadas REST.
- **H2 Database**: Banco de dados em memória para testes. _[help](https://stackoverflow.com/questions/67695069/spring-boot-datasource-initialization-error-with-data-sql-script-after-2-5-0-upg)_
- **Spring Cloud Gateway**: Para gerenciar e rotear requisições entre os microserviços, proporcionando uma única entrada para o sistema. _[help](https://www.youtube.com/watch?v=ju7NTqJxKRs)_
- **Resilience4j**: Para implementar padrões de tolerância a falhas e melhorar a resiliência do sistema. _[help](https://resilience4j.readme.io/docs/getting-started)_

## Futuras Evoluções

  O projeto será expandido com a adição das seguintes tecnologias e funcionalidades:

- **Servidor de Configuração Centralizado**: Para gerenciar a configuração dos microserviços de forma centralizada, permitindo alterações dinâmicas sem necessidade de reinício.
- **OAuth 2**: Para implementar autenticação e autorização, garantindo que os serviços estejam protegidos e apenas acessíveis a usuários autorizados.
- **Docker**: Para implementar facilidades no deploy e uso de contêineres.
- **Kafka**: Para implementar mensageria e programação orientada a eventos entre os serviços.

## Configuração do Ambiente

### Pré-requisitos

- Java 17 ou superior
- Maven
- Docker (opcional)

## Notas finais

Este projeto serve como guia tanto para estudantes de Back End que desejam conhecer o conceito de arquitetura de microsserviços, e para desenvolvedores já alocados no mercado de trabalho, com desejo de replicar os conceitos aqui aplicados ou como forma de referência para a modelagem de um sistema próprio. Sinta-se livre para entrar em contato comigo via Email, Issues ou forks para a atualização, esclarecimentos de dúvidas ou sobre melhorias do projeto.

Foi utilizado como base o curso do Nelio Alves, _[link](https://www.udemy.com/course/microsservicos-java-spring-cloud)_, e toneladas de materiais oficiais das tecnologias. E para finalizar, acredito que esse codebase sejá uma boa atualização do curso mencionado, visto as atualizações das tecnologias e liguagem usada. Vem me dando um bom trabalho manter esse código atualizado e em boa legibilidade e documentado, dito isso, toda ajuda é bem vinda. 

Meus agradecimentos, atenciosamente, Lucas Dias Jorge :)
