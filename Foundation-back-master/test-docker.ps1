# Script de test PowerShell pour la stack Docker Foundation Sociale
# Teste tous les services et leurs healthchecks

Write-Host "🚀 Test de la stack Docker Foundation Sociale" -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan

# Fonction pour tester un endpoint
function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Url,
        [string]$ExpectedStatus = ""
    )
    
    Write-Host "Testing $Name... " -NoNewline
    
    try {
        $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 10 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ OK" -ForegroundColor Green
            return $true
        } else {
            Write-Host "❌ FAILED (Status: $($response.StatusCode))" -ForegroundColor Red
            return $false
        }
    } catch {
        Write-Host "❌ FAILED" -ForegroundColor Red
        return $false
    }
}

# Fonction pour tester un service Docker
function Test-Service {
    param(
        [string]$ServiceName,
        [int]$Port,
        [string]$HealthPath = ""
    )
    
    Write-Host "Testing $ServiceName (port $Port)... " -NoNewline
    
    try {
        $url = "http://localhost:$Port$HealthPath"
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 10 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ OK" -ForegroundColor Green
            return $true
        } else {
            Write-Host "❌ FAILED (Status: $($response.StatusCode))" -ForegroundColor Red
            return $false
        }
    } catch {
        Write-Host "❌ FAILED" -ForegroundColor Red
        return $false
    }
}

Write-Host ""
Write-Host "📊 Vérification du statut Docker Compose..." -ForegroundColor Yellow
docker compose ps

Write-Host ""
Write-Host "🔍 Test des healthchecks..." -ForegroundColor Yellow

# Test Discovery Service
$discoveryOk = Test-Service "Discovery Service" 8762 "/"

# Test Config Service
$configOk = Test-Service "Config Service" 8888 "/actuator/health"

# Test Gateway Service
$gatewayOk = Test-Service "Gateway Service" 9999 "/actuator/health"

# Test des microservices
$authOk = Test-Service "Auth Service" 8081 "/actuator/health"
$userOk = Test-Service "User Service" 8082 "/actuator/health"
$socialOk = Test-Service "Social Service" 8084 "/actuator/health"
$notificationOk = Test-Service "Notification Service" 8085 "/actuator/health"
$adminOk = Test-Service "Admin Service" 8086 "/actuator/health"

# Test Frontend
$frontendOk = Test-Service "Frontend" 4200 "/"

# Test Adminer
$adminerOk = Test-Service "Adminer" 8087 "/"

Write-Host ""
Write-Host "🌐 Test d'accès aux URLs principales..." -ForegroundColor Yellow

# Test Eureka Dashboard
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8762" -UseBasicParsing -TimeoutSec 10
    if ($response.Content -match "Eureka") {
        Write-Host "✅ Eureka Dashboard accessible" -ForegroundColor Green
    } else {
        Write-Host "❌ Eureka Dashboard inaccessible" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Eureka Dashboard inaccessible" -ForegroundColor Red
}

# Test Frontend
try {
    $response = Invoke-WebRequest -Uri "http://localhost:4200" -UseBasicParsing -TimeoutSec 10
    if ($response.Content -match "html|angular") {
        Write-Host "✅ Frontend accessible" -ForegroundColor Green
    } else {
        Write-Host "❌ Frontend inaccessible" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Frontend inaccessible" -ForegroundColor Red
}

Write-Host ""
Write-Host "📋 Résumé des tests..." -ForegroundColor Cyan
Write-Host "======================" -ForegroundColor Cyan

# Compter les services qui fonctionnent
$totalServices = 10
$workingServices = 0

if ($discoveryOk) { $workingServices++ }
if ($configOk) { $workingServices++ }
if ($gatewayOk) { $workingServices++ }
if ($authOk) { $workingServices++ }
if ($userOk) { $workingServices++ }
if ($socialOk) { $workingServices++ }
if ($notificationOk) { $workingServices++ }
if ($adminOk) { $workingServices++ }
if ($frontendOk) { $workingServices++ }
if ($adminerOk) { $workingServices++ }

Write-Host "Services fonctionnels: $workingServices/$totalServices" -ForegroundColor Yellow

if ($workingServices -eq $totalServices) {
    Write-Host "🎉 Tous les services fonctionnent correctement !" -ForegroundColor Green
} else {
    Write-Host "⚠️  Certains services ont des problèmes. Vérifiez les logs avec 'docker compose logs -f'" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "🔧 Commandes utiles :" -ForegroundColor Cyan
Write-Host "  - Voir les logs : docker compose logs -f" -ForegroundColor White
Write-Host "  - Redémarrer : docker compose restart" -ForegroundColor White
Write-Host "  - Arrêter : docker compose down" -ForegroundColor White
Write-Host "  - Reconstruire : docker compose up --build -d" -ForegroundColor White

