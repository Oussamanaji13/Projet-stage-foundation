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

### Option 1: Docker Compose (Recommended)

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

The services use the following environment variables:

- `SPRING_PROFILES_ACTIVE=docker` - Activates Docker profile
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` - Eureka server URL
- `SPRING_CLOUD_CONFIG_URI` - Config server URL
- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password

### Database Configuration

- **Host:** `mysql-dock` (Docker) or `localhost` (Local)
- **Port:** `3306` (internal) / `3307` (external)
- **Database:** `foundation`
- **Username:** `foundation`
- **Password:** `foundation123`

## 📊 Monitoring & Health Checks

### Service Health Endpoints

Each service provides health check endpoints:

- Discovery Service: http://localhost:8762/actuator/health
- Config Service: http://localhost:8888/actuator/health
- Gateway Service: http://localhost:9999/actuator/health
- Auth Service: http://localhost:8081/actuator/health
- User Service: http://localhost:8082/actuator/health
- Content Service: http://localhost:8083/actuator/health
- Social Service: http://localhost:8084/actuator/health
- Notification Service: http://localhost:8085/actuator/health
- Admin Service: http://localhost:8086/actuator/health

### Eureka Dashboard

- **URL:** http://localhost:8762
- **Description:** Service discovery dashboard showing all registered services

## 🛠️ Development Commands

### Docker Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# Rebuild and start
docker-compose up --build -d

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f gateway-service

# Check service status
docker-compose ps

# Access MySQL
docker exec -it mysql-dock mysql -u foundation -p
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
