#!/bin/bash

# Espera o Kafka estar pronto antes de criar os tópicos
KAFKA_READY=false
while [ "$KAFKA_READY" = false ]; do
  if echo > /dev/tcp/kafka/9092; then
    KAFKA_READY=true
  else
    echo "Esperando o Kafka ficar pronto..."
    sleep 5
  fi
done

# Cria o tópico com dados para busca na api
kafka-topics --create --topic USER-REPOSITORY-GITHUB-TOPIC --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

# Cria o tópico com informações de repositórios, com delay de 1 dia para entrega
kafka-topics --create --topic INFO-REPOSITORY-GITHUB-TOPIC --bootstrap-server localhost:9092 \
    --partitions 1 --replication-factor 1 \
    --config retention.ms=15

echo "Tópico 'USER_REPOSITORY_GITHUB_TOPIC' criado com sucesso!"
