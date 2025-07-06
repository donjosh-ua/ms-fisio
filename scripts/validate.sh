#!/bin/bash
echo "Validating MS-Fisio Environment..."

# Check if containers are running
echo "Checking containers..."
if ! docker-compose ps | grep -q "Up"; then
    echo "❌ Containers are not running"
    echo "Run 'docker-compose up -d' to start the services"
    exit 1
fi

# Wait a moment for services to stabilize
sleep 5

# Check application health
echo "Checking application health..."
if ! curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "❌ Application health check failed"
    echo "Check application logs with: docker-compose logs app"
    exit 1
fi

# Check database connection
echo "Checking database connection..."
if ! docker-compose exec -T postgres pg_isready -U postgres > /dev/null 2>&1; then
    echo "❌ Database is not ready"
    echo "Check database logs with: docker-compose logs postgres"
    exit 1
fi

echo "✅ All services are running correctly!"
echo ""
echo "🌐 Application: http://localhost:8080"
echo "📊 Health Check: http://localhost:8080/actuator/health"
echo "🗄️  Database Admin: http://localhost:5050 (use --profile dev to start)"
echo "📚 API Documentation: http://localhost:8080/swagger-ui.html"
echo ""
echo "Test login with:"
echo "  Email: test@example.com"
echo "  Password: password123"
