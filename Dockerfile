# syntax=docker/dockerfile:1

# ---------------------------------------------------------------------------
# Stage de build: compila o reactor inteiro uma única vez.
# Todos os stages de runtime abaixo reaproveitam este mesmo stage, então
# "docker compose build" não recompila o projeto por serviço.
# ---------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Os poms vêm primeiro para que o download de dependências fique numa
# camada separada, reaproveitada enquanto nenhum pom mudar.
COPY pom.xml ./
COPY Eureka/pom.xml Eureka/
COPY Config-Server/pom.xml Config-Server/
COPY API-Gateway/pom.xml API-Gateway/
COPY Oauth/Integration/pom.xml Oauth/Integration/
COPY Kafka-Server/Producer/pom.xml Kafka-Server/Producer/
COPY Deprecated-Services/Workers/pom.xml Deprecated-Services/Workers/
COPY Deprecated-Services/Payroll/pom.xml Deprecated-Services/Payroll/
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package -DskipTests

# ---------------------------------------------------------------------------
# Base de runtime: JRE enxuto, usuário sem privilégios, curl para healthcheck.
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre AS runtime
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && useradd --system --uid 1001 --create-home spring
USER spring
WORKDIR /app
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]

FROM runtime AS eureka
COPY --from=build /workspace/Eureka/target/Eureka-1.0.jar /app/app.jar

FROM runtime AS config-server
COPY --from=build /workspace/Config-Server/target/Config-Server-1.0.jar /app/app.jar

FROM runtime AS gateway
COPY --from=build /workspace/API-Gateway/target/gateway-1.0.jar /app/app.jar

FROM runtime AS oauth
COPY --from=build /workspace/Oauth/Integration/target/oauth-1.0.jar /app/app.jar

FROM runtime AS producer
COPY --from=build /workspace/Kafka-Server/Producer/target/Producer-1.0.jar /app/app.jar

FROM runtime AS workers
COPY --from=build /workspace/Deprecated-Services/Workers/target/Workers-1.0.jar /app/app.jar

FROM runtime AS payroll
COPY --from=build /workspace/Deprecated-Services/Payroll/target/Payroll-1.0.jar /app/app.jar
