# ORCHESTRADOR - Serviço de Orquestração

## Descrição

Este microsserviço é o **componente central** no fluxo de processamento da aplicação. É responsável por consumir mensagens do Kafka publicadas pelo Producer, buscar informações adicionais na API do GitHub, persistir os dados no MongoDB e republicá-los em outro tópico Kafka para processamento final pelo serviço de webhook.

**Parte 2 do fluxo integrado de 3 microsserviços:**
1. [Producer Kafka](../swap-producer-kafka/README.md) - Recebe solicitações e publica no Kafka
2. **Orchestrador (este)** - Consome mensagens, processa e persiste no MongoDB
3. [Webhook Publisher](../swap-webhook-publishing/README.md) - Publica informações via webhook

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.3.4
- Spring Data MongoDB
- Apache Kafka e Avro
- Resilience4j (Circuit Breaker)
- Docker e Docker Compose
- Maven

## Pré-requisitos

- Java 17
- Maven
- Docker e Docker Compose (para ambiente de infraestrutura)
- Git

## Como Executar

### 1. Preparando o Ambiente de Infraestrutura

Para executar toda a aplicação integrada usando o Docker Compose da raiz do projeto:

```bash
# Na raiz do projeto
docker-compose up -d
```

Se preferir executar apenas a infraestrutura:

```bash
# No diretório deste projeto
cd docker
docker-compose up -d
```

### 2. Ordem de Execução dos Microsserviços

Para garantir o funcionamento correto da solução completa, inicie os serviços na seguinte ordem:

1. Infraestrutura (Kafka, Schema Registry, MongoDB)
2. [Producer Kafka](../swap-producer-kafka/README.md) - porta 8090
3. **Orchestrador (este serviço)** - porta 8080
4. [Webhook Publisher](../swap-webhook-publishing/README.md) - porta 8099

### 3. Executando Este Serviço

#### Utilizando Maven
```bash
# Compilar o projeto
mvn clean package

# Executar a aplicação
mvn spring-boot:run
```

#### Utilizando JAR
```bash
# Após compilar o projeto
java -jar target/orchestrador-0.0.1-SNAPSHOT.jar
```

#### Utilizando Docker
```bash
# Na raiz deste projeto
docker build -t orchestrador .
docker run -p 8080:8080 -e GITHUB_TOKEN=seu_token_github orchestrador
```

### 4. Verificando o Status

O serviço estará disponível em http://localhost:8080
Endpoints de monitoramento: http://localhost:8080/actuator/health

## Configuração de Tópicos Kafka

Os tópicos Kafka são configurados automaticamente pelo Docker Compose principal. Se estiver executando manualmente, você pode criar os tópicos necessários:

```bash
# Acessar o container do Kafka
docker exec -it kafka /bin/bash

# Criar tópico para receber mensagens do Producer
kafka-topics --create --topic github-issue-topic \
     --bootstrap-server localhost:9092  \
     --partitions 3 --replication-factor 1

# Criar tópico para enviar mensagens ao Webhook Publisher
kafka-topics --create --topic INFO-REPOSITORY-GITHUB-TOPIC \
    --bootstrap-server localhost:9092 \
    --partitions 3 --replication-factor 1 \
    --config retention.ms=86400000
```

## Arquitetura da Solução Completa

```mermaid
flowchart TD
    subgraph "1. Produtor Kafka (porta 8090)"
        A[PUBLISHER.KAFKA] --> B[github-issue-topic]
    end

    B --> C[ORCHESTRADOR]
    
    subgraph "2. Orchestrador (porta 8080)"
        C --> D[MONGODB]
        C --> E[INFO-REPOSITORY-GITHUB-TOPIC]
    end
    
    subgraph "3. Webhook Publisher (porta 8099)"
        E --> F[PUBLISHER.WEBHOOK]
        F --> G[WEBHOOK-API]
    end
```

## API REST

### Buscar Informações do Repositório GitHub
- **URL**: `/info-repository-github/info`
- **Método**: GET
- **Corpo da Requisição**:
```json
{
  "userName": "nome-usuario-github",
  "repository": "nome-repositorio-github"
}
```

## Configurações

As principais configurações podem ser ajustadas no arquivo `application.properties` ou via variáveis de ambiente:

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://user:password@mongodb:27017/admin

# GitHub API
app.client.github.token=${GITHUB_TOKEN:github_token_placeholder}

# Kafka
spring.kafka.bootstrap-servers=kafka:9092
app.kafka.producer.github-info.topic=INFO-REPOSITORY-GITHUB-TOPIC
```

## Variáveis de Ambiente Importantes

- `GITHUB_TOKEN` - Token de autenticação para a API do GitHub
- `SPRING_DATA_MONGODB_URI` - URI de conexão com o MongoDB
- `KAFKA_BOOTSTRAP_SERVERS` - Servidores Kafka

## Links para os Outros Microsserviços

Este serviço faz parte de uma solução composta por três microsserviços:

1. [Publisher Kafka](../swap-producer-kafka/README.md) - Ponto de entrada que recebe requisições REST e publica no Kafka
2. **Orchestrador (este)** - Middleware que processa dados, persiste no MongoDB e publica em outro tópico
3. [Webhook Publisher](../swap-webhook-publishing/README.md) - Serviço final que consome do Kafka e publica via webhook

## Troubleshooting

Se encontrar problemas:

1. Verifique se o MongoDB está acessível e com as credenciais corretas
2. Confirme se o token do GitHub é válido e tem permissões suficientes
3. Certifique-se de que o Producer Kafka esteja enviando mensagens no formato correto
4. Verifique os logs da aplicação para identificar erros específicos
