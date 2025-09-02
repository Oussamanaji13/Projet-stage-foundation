#!/bin/bash

# Script de test pour la stack Docker Foundation Sociale
# Teste tous les services et leurs healthchecks

echo "🚀 Test de la stack Docker Foundation Sociale"
echo "=============================================="

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Fonction pour tester un endpoint
test_endpoint() {
    local name=$1
    local url=$2
    local expected_status=$3
    
    echo -n "Testing $name... "
    
    if curl -s -f "$url" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ OK${NC}"
        return 0
    else
        echo -e "${RED}❌ FAILED${NC}"
        return 1
    fi
}

# Fonction pour tester un service Docker
test_service() {
    local service_name=$1
    local port=$2
    local health_path=$3
    
    echo -n "Testing $service_name (port $port)... "
    
    if curl -s -f "http://localhost:$port$health_path" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ OK${NC}"
        return 0
    else
        echo -e "${RED}❌ FAILED${NC}"
        return 1
    fi
}

echo ""
echo "📊 Vérification du statut Docker Compose..."
docker compose ps

echo ""
echo "🔍 Test des healthchecks..."

# Test MySQL
test_endpoint "MySQL" "http://localhost:3307" ""

# Test Discovery Service
test_service "Discovery Service" "8762" "/"

# Test Config Service
test_service "Config Service" "8888" "/actuator/health"

# Test Gateway Service
test_service "Gateway Service" "9999" "/actuator/health"

# Test des microservices
test_service "Auth Service" "8081" "/actuator/health"
test_service "User Service" "8082" "/actuator/health"
test_service "Social Service" "8084" "/actuator/health"
test_service "Notification Service" "8085" "/actuator/health"
test_service "Admin Service" "8086" "/actuator/health"

# Test Frontend
test_service "Frontend" "4200" "/"

# Test Adminer
test_service "Adminer" "8087" "/"

echo ""
echo "🌐 Test d'accès aux URLs principales..."

# Test Eureka Dashboard
if curl -s "http://localhost:8762" | grep -q "Eureka"; then
    echo -e "${GREEN}✅ Eureka Dashboard accessible${NC}"
else
    echo -e "${RED}❌ Eureka Dashboard inaccessible${NC}"
fi

# Test Frontend
if curl -s "http://localhost:4200" | grep -q "html\|angular"; then
    echo -e "${GREEN}✅ Frontend accessible${NC}"
else
    echo -e "${RED}❌ Frontend inaccessible${NC}"
fi

echo ""
echo "📋 Résumé des tests..."
echo "======================"

# Compter les services qui fonctionnent
total_services=10
working_services=0

for port in 3307 8762 8888 9999 8081 8082 8084 8085 8086 4200 8087; do
    if [ "$port" = "3307" ]; then
        # MySQL n'a pas d'endpoint HTTP, on vérifie juste qu'il écoute
        if netstat -an 2>/dev/null | grep -q ":$port.*LISTEN" || ss -tuln 2>/dev/null | grep -q ":$port"; then
            working_services=$((working_services + 1))
        fi
    else
        if curl -s -f "http://localhost:$port" > /dev/null 2>&1; then
            working_services=$((working_services + 1))
        fi
    fi
done

echo "Services fonctionnels: $working_services/$total_services"

if [ $working_services -eq $total_services ]; then
    echo -e "${GREEN}🎉 Tous les services fonctionnent correctement !${NC}"
else
    echo -e "${YELLOW}⚠️  Certains services ont des problèmes. Vérifiez les logs avec 'docker compose logs -f'${NC}"
fi

echo ""
echo "🔧 Commandes utiles :"
echo "  - Voir les logs : docker compose logs -f"
echo "  - Redémarrer : docker compose restart"
echo "  - Arrêter : docker compose down"
echo "  - Reconstruire : docker compose up --build -d"

