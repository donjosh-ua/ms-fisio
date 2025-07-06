# PowerShell script to stop MS-Fisio Application
Write-Host "Stopping MS-Fisio Application..." -ForegroundColor Yellow
docker-compose down
Write-Host "Application stopped successfully!" -ForegroundColor Green
