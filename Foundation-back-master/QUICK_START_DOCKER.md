# 🚀 Démarrage Rapide Docker - Foundation Sociale

## 📋 Prérequis

- **Docker Desktop** installé et démarré
- **Ports disponibles** : 3307, 8762, 8888, 9999, 8081-8086, 4200, 8087

## ⚡ Démarrage en 3 étapes

### 1. Naviguer vers le répertoire backend
```bash
cd Foundation-back-master
```

### 2. Construire et démarrer tous les services
```bash
docker compose build
docker compose up -d
```

### 3. Vérifier que tout fonctionne
```bash
docker compose ps
```

## 🌐 URLs d'accès

| Service | URL | Description |
|---------|-----|-------------|
| **Eureka Dashboard** | http://localhost:8762 | Service discovery |
| **Config Service** | http://localhost:8888/actuator/health | Health check |
| **Gateway** | http://localhost:9999/actuator/health | Health check |
| **Frontend** | http://localhost:4200 | Application Angular |
| **Adminer** | http://localhost:8087 | Gestion MySQL |

## 🔍 Vérification des services

### Voir le statut de tous les services
```bash
docker compose ps
```

### Voir les logs en temps réel
```bash
docker compose logs -f
```

### Voir les logs d'un service spécifique
```bash
docker compose logs -f gateway-service
docker compose logs -f discovery-service
docker compose logs -f auth-service
```

## 🧪 Tests automatiques

### Script Bash (Linux/Mac)
```bash
chmod +x test-docker.sh
./test-docker.sh
```

### Script PowerShell (Windows)
```powershell
.\test-docker.ps1
```

## 🛠️ Commandes utiles

### Redémarrer un service
```bash
docker compose restart gateway-service
```

### Reconstruire et redémarrer
```bash
docker compose up --build -d
```

### Arrêter tous les services
```bash
docker compose down
```

### Nettoyer complètement
```bash
docker compose down -v --remove-orphans
```

## 🔧 Dépannage

### Service ne démarre pas
1. Vérifier les logs : `docker compose logs [service-name]`
2. Vérifier les dépendances : `docker compose ps`
3. Vérifier les ports disponibles

### Problème de base de données
```bash
# Accéder à MySQL
docker exec -it mysql-dock mysql -u foundation -p

# Vérifier la base
SHOW DATABASES;
USE foundation;
SHOW TABLES;
```

### Problème de réseau
```bash
# Vérifier le réseau Docker
docker network ls
docker network inspect foundation-back-master_backend
```

## 📊 Monitoring

### Healthchecks des services
- **MySQL** : `mysqladmin ping` toutes les 10s
- **Discovery** : `curl http://localhost:8762/` toutes les 10s
- **Config** : `curl http://localhost:8888/actuator/health` toutes les 10s
- **Gateway** : `curl http://localhost:9999/actuator/health` toutes les 10s
- **Services** : `curl http://localhost:808X/actuator/health` toutes les 10s
- **Frontend** : `curl http://localhost:4200/` toutes les 15s

### Ordre de démarrage automatique
```
MySQL (3307:3306) → Discovery (8762) → Config (8888) → Gateway (9999) → Services (8081-8086) → Frontend (4200)
```

## 🎯 Prochaines étapes

1. **Vérifier Eureka** : http://localhost:8762
2. **Tester le Gateway** : http://localhost:9999/actuator/health
3. **Accéder au Frontend** : http://localhost:4200
4. **Gérer la base** : http://localhost:8087

## 📞 Support

En cas de problème :
1. Vérifier les logs : `docker compose logs -f`
2. Vérifier le statut : `docker compose ps`
3. Consulter la section dépannage ci-dessus
4. Vérifier que Docker Desktop est démarré

