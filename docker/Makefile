# Variáveis
DOCKER_COMPOSE = docker compose -f
COMPOSE_FILE = docker-compose.yml

mongodb-up:
        $(DOCKER_COMPOSE) ./mongo/$(COMPOSE_FILE) up -d

mongodb-stop:
        $(DOCKER_COMPOSE) ./mongo/$(COMPOSE_FILE) stop

mongodb-rm:
        $(DOCKER_COMPOSE) ./mongo/$(COMPOSE_FILE) rm -f

kafka-up:
        $(DOCKER_COMPOSE) ./kafka/$(COMPOSE_FILE) up -d

kafka-stop:
        $(DOCKER_COMPOSE) ./kafka/$(COMPOSE_FILE) stop

kafka-rm:
        $(DOCKER_COMPOSE) ./kafka/$(COMPOSE_FILE) rm -f