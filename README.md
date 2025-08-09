````markdown name=README.md
# Microservices

Este projeto implementa uma arquitetura de microserviços utilizando Java, Spring Cloud, Eureka, Gateway e componentes de autorização e recursos.

## Descrição

O objetivo deste projeto é demonstrar uma arquitetura de microserviços moderna, escalável e resiliente, aproveitando o ecossistema Spring para facilitar o desenvolvimento e a integração dos serviços. O projeto implementa discovery service, API Gateway, autenticação/autorização e serviços de recursos independentes.

## Principais Tecnologias

- **Java**: Linguagem principal para desenvolvimento dos microserviços.
- **Spring Cloud**: Framework para facilitar a construção de sistemas distribuídos.
- **Eureka**: Serviço de discovery para registro e localização automática dos microserviços.
- **Spring Cloud Gateway**: API Gateway para roteamento e filtragem das requisições.
- **Spring Security**: Gerenciamento de autenticação e autorização.
- **Docker** (opcional): Para facilitar o deploy e orquestração dos serviços.

## Estrutura do Projeto

```plaintext
.
├── eureka-server/         # Serviço de discovery
├── api-gateway/           # Gateway de API
├── auth-service/          # Serviço de autenticação/autorização
├── resource-service-1/    # Exemplo de serviço de recurso
├── resource-service-2/    # Outro serviço de recurso
└── README.md
```

## Como Executar

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/LucasDiasJorge/Microservices.git
   cd Microservices
   ```

2. **Suba o Eureka Server:**
   ```bash
   cd eureka-server
   ./mvnw spring-boot:run
   ```

3. **Suba o API Gateway:**
   ```bash
   cd ../api-gateway
   ./mvnw spring-boot:run
   ```

4. **Suba o Auth Service:**
   ```bash
   cd ../auth-service
   ./mvnw spring-boot:run
   ```

5. **Suba os serviços de recursos:**
   ```bash
   cd ../resource-service-1
   ./mvnw spring-boot:run

   cd ../resource-service-2
   ./mvnw spring-boot:run
   ```

> **Dica:** Você pode usar Docker Compose para facilitar o processo de execução dos serviços, caso disponível.

## Funcionalidades

- Registro e descoberta automática de serviços via Eureka.
- Gateway de API centralizado para roteamento de requisições.
- Autenticação e autorização centralizadas.
- Serviços independentes e escaláveis.

## Contribuição

Sinta-se à vontade para abrir issues ou pull requests para sugerir melhorias ou reportar bugs.

## Licença

Este projeto está sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

---
Desenvolvido por [Lucas Dias Jorge](https://github.com/LucasDiasJorge)
````
