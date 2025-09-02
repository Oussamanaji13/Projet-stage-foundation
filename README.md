# Foundation Sociale - Projet Full-Stack

## 🚀 Vue d'ensemble

Ce projet est une plateforme complète de gestion de fondation sociale, construite avec une architecture microservices moderne et une interface utilisateur responsive.

## 🏗️ Architecture

### Backend - Microservices Architecture
- **Discovery Service** : Service de découverte Eureka
- **Config Service** : Service de configuration centralisée
- **Gateway Service** : API Gateway avec routing et filtrage
- **Auth Service** : Service d'authentification et autorisation JWT
- **User Service** : Gestion des utilisateurs et profils
- **Content Service** : Gestion du contenu (actualités, événements, etc.)
- **Social Service** : Gestion des interactions sociales et demandes
- **Notification Service** : Service de notifications par email
- **Admin Service** : Interface d'administration

### Frontend - Angular Application
- Interface utilisateur moderne et responsive
- Gestion des utilisateurs et des rôles
- Tableau de bord administratif
- Gestion du contenu et des événements
- Système de notifications

## 🛠️ Technologies Utilisées

### Backend
- **Java 17** avec **Spring Boot 3.x**
- **Spring Cloud** pour les microservices
- **Spring Security** avec JWT
- **Spring Data JPA** avec **Hibernate**
- **Maven** pour la gestion des dépendances
- **Docker** pour la conteneurisation
- **Eureka** pour la découverte de services
- **Config Server** pour la configuration centralisée

### Frontend
- **Angular 17** avec **TypeScript**
- **Tailwind CSS** pour le styling
- **Angular Material** pour les composants UI
- **RxJS** pour la gestion des observables

### Infrastructure
- **Docker Compose** pour l'orchestration
- **Nginx** comme reverse proxy
- **PostgreSQL** comme base de données principale

## 📁 Structure du Projet

```
OUSS/
├── Foundation-back-master/          # Backend microservices
│   ├── admin-service/
│   ├── auth-service/
│   ├── config-service/
│   ├── content-service/
│   ├── discovery-service/
│   ├── gateway-service/
│   ├── notification-service/
│   ├── social-service/
│   ├── user-service/
│   └── Docker-compose.yml
├── frontend/                        # Application Angular
│   └── fondation-sociale-frontend/
├── package.json                     # Dépendances Node.js
└── README.md                       # Ce fichier
```

## 🚀 Démarrage Rapide

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+
- Node.js 18+
- Docker et Docker Compose
- PostgreSQL

### Backend
```bash
cd Foundation-back-master
# Démarrer les services avec Docker Compose
docker-compose up -d

# Ou démarrer individuellement chaque service
cd discovery-service
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend/fondation-sociale-frontend
npm install
ng serve
```

## 🔧 Configuration

### Variables d'environnement
Créez un fichier `.env` à la racine du projet :
```env
# Base de données
DB_HOST=localhost
DB_PORT=5432
DB_NAME=foundation_db
DB_USERNAME=foundation_user
DB_PASSWORD=foundation_pass

# JWT
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000

# Email
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password
```

## 📚 Documentation API

La documentation Swagger est disponible pour chaque service :
- **Gateway Service** : http://localhost:8080/swagger-ui.html
- **Auth Service** : http://localhost:8081/swagger-ui.html
- **User Service** : http://localhost:8082/swagger-ui.html
- **Content Service** : http://localhost:8083/swagger-ui.html
- **Social Service** : http://localhost:8084/swagger-ui.html
- **Notification Service** : http://localhost:8085/swagger-ui.html

## 🐳 Docker

### Construire et démarrer tous les services
```bash
docker-compose up --build
```

### Arrêter tous les services
```bash
docker-compose down
```

### Voir les logs
```bash
docker-compose logs -f [service-name]
```

## 🧪 Tests

### Backend
```bash
# Tests unitaires
./mvnw test

# Tests d'intégration
./mvnw verify
```

### Frontend
```bash
# Tests unitaires
ng test

# Tests e2e
ng e2e
```

## 📊 Monitoring

- **Eureka Dashboard** : http://localhost:8761
- **Actuator Endpoints** : http://localhost:8080/actuator

## 🤝 Contribution

1. Fork le projet
2. Créez une branche feature (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📝 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👥 Auteurs

- **Oussama Najii** - Développeur Full-Stack

## 🙏 Remerciements

- Spring Boot Team
- Angular Team
- Tous les contributeurs open source

---

**Note** : Ce projet est en cours de développement. Certaines fonctionnalités peuvent ne pas être encore implémentées.
