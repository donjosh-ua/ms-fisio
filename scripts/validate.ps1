# PowerShell script to validate MS-Fisio Environment
Write-Host "Validating MS-Fisio Environment..." -ForegroundColor Yellow

# Check if containers are running
Write-Host "Checking containers..." -ForegroundColor Cyan
$containers = docker-compose ps
if ($containers -notmatch "Up") {
    Write-Host "❌ Containers are not running" -ForegroundColor Red
    Write-Host "Run 'docker-compose up -d' to start the services" -ForegroundColor Yellow
    exit 1
}

# Wait a moment for services to stabilize
Start-Sleep -Seconds 5

# Check application health
Write-Host "Checking application health..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 10
    if ($response.StatusCode -ne 200) {
        throw "Health check failed"
    }
} catch {
    Write-Host "❌ Application health check failed" -ForegroundColor Red
    Write-Host "Check application logs with: docker-compose logs app" -ForegroundColor Yellow
    exit 1
}

# Check database connection
Write-Host "Checking database connection..." -ForegroundColor Cyan
$dbCheck = docker-compose exec -T postgres pg_isready -U postgres
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Database is not ready" -ForegroundColor Red
    Write-Host "Check database logs with: docker-compose logs postgres" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ All services are running correctly!" -ForegroundColor Green
Write-Host ""
Write-Host "🌐 Application: http://localhost:8080" -ForegroundColor Cyan
Write-Host "📊 Health Check: http://localhost:8080/actuator/health" -ForegroundColor Cyan
Write-Host "🗄️  Database Admin: http://localhost:5050 (use --profile dev to start)" -ForegroundColor Cyan
Write-Host "📚 API Documentation: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""
Write-Host "Test login with:" -ForegroundColor Yellow
Write-Host "  Email: test@example.com" -ForegroundColor White
Write-Host "  Password: password123" -ForegroundColor White
