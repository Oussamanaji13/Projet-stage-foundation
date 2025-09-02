# Foundation Sociale - Spring Boot Microservices Backend

## 🏗️ Architecture

This is a microservices-based backend for the Fondation Sociale portal, built with Spring Boot and Spring Cloud.

### Services Overview

| Service | Port | Description |
|---------|------|-------------|
| **Discovery Service** | 8762 | Eureka Server for service discovery |
| **Config Service** | 8888 | Spring Cloud Config Server |
| **Gateway Service** | 9999 | API Gateway (Zuul/Spring Cloud Gateway) |
| **Auth Service** | 8081 | Authentication & Authorization |
| **User Service** | 8082 | User management |
| **Content Service** | 8083 | Content management |
| **Social Service** | 8084 | Social features |
| **Notification Service** | 8085 | Notifications |
| **Admin Service** | 8086 | Admin panel |
| **MySQL Database** | 3307 | Database |

## 🚀 Quick Start

### Prerequisites

- **Docker Desktop** (with Docker Compose)
- **Java 17** (for local development)
- **Maven** (for local development)

### 🐳 Docker Compose avec Eureka 8762 + MySQL 8 (Recommandé)

Cette configuration utilise :
- **Eureka Server** sur le port **8762**
- **MySQL 8** sur le port **3307** (hôte) → **3306** (conteneur)
- **Healthchecks** avec dépendances conditionnelles
- **Spring Boot 3** + **Spring Cloud 2022.0.4**

#### Démarrage rapide :

1. **Naviguer vers le répertoire backend :**
   ```bash
   cd Foundation-back-master
   ```

2. **Construire et démarrer tous les services :**
   ```bash
   docker compose build
   docker compose up -d
   ```

3. **Vérifier le statut des services :**
   ```bash
   docker compose ps
   ```

4. **Voir les logs en temps réel :**
   ```bash
   docker compose logs -f
   ```

#### URLs utiles :

- **Eureka Dashboard** : http://localhost:8762
- **Config Service Health** : http://localhost:8888/actuator/health
- **Gateway Health** : http://localhost:9999/actuator/health
- **Services Health** :
  - Auth : http://localhost:8081/actuator/health
  - User : http://localhost:8082/actuator/health
  - Social : http://localhost:8084/actuator/health
  - Notification : http://localhost:8085/actuator/health
  - Admin : http://localhost:8086/actuator/health
- **Frontend (dev)** : http://localhost:4200
- **Adminer (MySQL)** : http://localhost:8087

#### Ordre de démarrage automatique :

```
MySQL (3307:3306) → Discovery (8762) → Config (8888) → Gateway (9999) → Services (8081-8086) → Frontend (4200)
```

### Option 2: Docker Compose (Ancienne méthode)

1. **Navigate to the backend directory:**
   ```bash
   cd Foundation-back-master
   ```

2. **Start all services:**
   ```bash
   docker-compose up -d
   ```

3. **Check service status:**
   ```bash
   docker-compose ps
   ```

4. **View logs:**
   ```bash
   docker-compose logs -f [service-name]
   ```

### Option 2: Local Development

1. **Build all services:**
   ```bash
   # Build each service
   cd discovery-service && ./mvnw clean package -DskipTests && cd ..
   cd config-service && ./mvnw clean package -DskipTests && cd ..
   cd gateway-service && ./mvnw clean package -DskipTests && cd ..
   cd auth-service && ./mvnw clean package -DskipTests && cd ..
   cd user-service && ./mvnw clean package -DskipTests && cd ..
   cd content-service && ./mvnw clean package -DskipTests && cd ..
   cd social-service && ./mvnw clean package -DskipTests && cd ..
   cd notification-service && ./mvnw clean package -DskipTests && cd ..
   cd admin-service && ./mvnw clean package -DskipTests && cd ..
   ```

2. **Start services in order:**
   ```bash
   # Start MySQL first
   docker run -d --name mysql-dock -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=foundation -p 3307:3306 mysql:8.0
   
   # Start services
   java -jar discovery-service/target/*.jar
   java -jar config-service/target/*.jar
   java -jar gateway-service/target/*.jar
   # ... continue with other services
   ```

## 🔧 Configuration

### Environment Variables

Les services utilisent les variables d'environnement suivantes :

- `SPRING_PROFILES_ACTIVE=docker` - Active le profil Docker
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8762/eureka/` - URL du serveur Eureka
- `SPRING_CLOUD_CONFIG_URI=http://config-service:8888` - URL du serveur de configuration
- `SPRING_CONFIG_IMPORT=configserver:http://config-service:8888` - Import moderne Spring Boot 3
- `SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/foundation?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` - URL de connexion MySQL
- `SPRING_DATASOURCE_USERNAME=foundation` - Nom d'utilisateur MySQL
- `SPRING_DATASOURCE_PASSWORD=foundation123` - Mot de passe MySQL
- `SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect` - Dialecte Hibernate MySQL
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver` - Driver MySQL

### Configuration Spring Boot 3

- **Java Version** : 17
- **Spring Boot** : 3.1.4
- **Spring Cloud** : 2022.0.4
- **Actuator** : Activé sur tous les services pour les healthchecks
- **MySQL Driver** : mysql-connector-j (runtime scope)
- **Healthchecks** : Via `/actuator/health` sur chaque service

### Database Configuration

- **Host:** `mysql-dock` (Docker) or `localhost` (Local)
- **Port:** `3306` (internal) / `3307` (external)
- **Database:** `foundation`
- **Username:** `foundation`
- **Password:** `foundation123`

## 📊 Monitoring & Health Checks

### Service Health Endpoints

Chaque service fournit des endpoints de health check :

- **Discovery Service** : http://localhost:8762/actuator/health
- **Config Service** : http://localhost:8888/actuator/health
- **Gateway Service** : http://localhost:9999/actuator/health
- **Auth Service** : http://localhost:8081/actuator/health
- **User Service** : http://localhost:8082/actuator/health
- **Content Service** : http://localhost:8083/actuator/health (si réactivé)
- **Social Service** : http://localhost:8084/actuator/health
- **Notification Service** : http://localhost:8085/actuator/health
- **Admin Service** : http://localhost:8086/actuator/health

### Healthchecks Docker

Tous les services ont des healthchecks configurés dans le docker-compose.yml :

- **MySQL** : `mysqladmin ping` toutes les 10s
- **Discovery** : `curl http://localhost:8762/` toutes les 10s
- **Config** : `curl http://localhost:8888/actuator/health` toutes les 10s
- **Gateway** : `curl http://localhost:9999/actuator/health` toutes les 10s
- **Services** : `curl http://localhost:808X/actuator/health` toutes les 10s
- **Frontend** : `curl http://localhost:4200/` toutes les 15s

### Dépendances conditionnelles

Le démarrage respecte l'ordre suivant grâce aux `depends_on: condition: service_healthy` :

```
MySQL → Discovery → Config → Gateway → Services → Frontend
```

### Eureka Dashboard

- **URL:** http://localhost:8762
- **Description:** Service discovery dashboard showing all registered services

## 🛠️ Development Commands

### Docker Commands

```bash
# Construire et démarrer tous les services
docker compose build
docker compose up -d

# Arrêter tous les services
docker compose down

# Reconstruire et démarrer
docker compose up --build -d

# Voir les logs de tous les services
docker compose logs -f

# Voir les logs d'un service spécifique
docker compose logs -f gateway-service
docker compose logs -f discovery-service
docker compose logs -f auth-service

# Vérifier le statut des services
docker compose ps

# Accéder à MySQL
docker exec -it mysql-dock mysql -u foundation -p

# Vérifier les healthchecks
docker compose ps --format "table {{.Name}}\t{{.Status}}\t{{.Ports}}"

# Redémarrer un service spécifique
docker compose restart gateway-service

# Nettoyer les volumes et conteneurs
docker compose down -v --remove-orphans
```

### Database Commands

```bash
# Connect to MySQL
docker exec -it mysql-dock mysql -u foundation -p

# Show databases
SHOW DATABASES;

# Use foundation database
USE foundation;

# Show tables
SHOW TABLES;
```

## 🔍 Troubleshooting

### Common Issues

1. **Port conflicts:**
   - Change ports in `docker-compose.yml`
   - Check if ports are already in use: `netstat -an | findstr :8081`

2. **Service not starting:**
   - Check logs: `docker-compose logs [service-name]`
   - Verify dependencies are running
   - Check health status: `docker-compose ps`

3. **Database connection issues:**
   - Ensure MySQL is running: `docker ps | grep mysql`
   - Check database credentials
   - Verify network connectivity

4. **Build failures:**
   - Clean and rebuild: `./mvnw clean package -DskipTests`
   - Check Java version: `java -version`
   - Verify Maven wrapper exists

### Health Check Commands

```bash
# Check all services health
curl -f http://localhost:8762/actuator/health  # Discovery
curl -f http://localhost:8888/actuator/health  # Config
curl -f http://localhost:9999/actuator/health  # Gateway
curl -f http://localhost:8081/actuator/health  # Auth
curl -f http://localhost:8082/actuator/health  # User
curl -f http://localhost:8083/actuator/health  # Content
curl -f http://localhost:8084/actuator/health  # Social
curl -f http://localhost:8085/actuator/health  # Notification
curl -f http://localhost:8086/actuator/health  # Admin
```

## 📚 API Documentation

See `API_CONTRACTS.md` for detailed API specifications.

## 🏥 Service Dependencies

```
MySQL → Discovery Service → Config Service → Gateway Service
                                    ↓
                    Auth, User, Content, Social, Notification, Admin Services
```

## 🎯 Next Steps

1. **Start the backend:** `docker-compose up -d`
2. **Verify services:** Check health endpoints
3. **Start the frontend:** Navigate to frontend directory and run `npm start`
4. **Test the application:** Access http://localhost:4200

## 📞 Support

For issues or questions:
- Check the troubleshooting section
- Review service logs
- Verify configuration settings
- Ensure all prerequisites are met
