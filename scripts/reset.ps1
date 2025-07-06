# PowerShell script to reset MS-Fisio Application
Write-Host "Resetting MS-Fisio Application (removes all data)..." -ForegroundColor Yellow
docker-compose down -v
Write-Host "Building and starting fresh containers..." -ForegroundColor Yellow
docker-compose up --build -d
Write-Host "Application reset complete!" -ForegroundColor Green
Write-Host "Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 30
Write-Host "Application ready at http://localhost:8080" -ForegroundColor Green
