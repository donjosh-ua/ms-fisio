# MS-Fisio Docker Setup

This document explains how to run the MS-Fisio application using Docker and Docker Compose for a complete, independent development environment.

## Prerequisites

- Docker installed on your system
- Docker Compose installed on your system
- Git installed

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd ms-fisio
```

### 2. Start the Application

```bash
# Build and start all services
docker-compose up --build -d

# Or start with pgAdmin for database management
docker-compose --profile dev up --build -d
```

### 3. Verify Everything is Running

```bash
# Check container status
docker-compose ps

# Run validation script (Linux/Mac)
chmod +x scripts/validate.sh
./scripts/validate.sh

# Manual health check
curl http://localhost:8080/actuator/health
```

## Services

### PostgreSQL Database

- **Image**: postgres:17.5-alpine3.22
- **Database**: fisio_db
- **User**: postgres
- **Password**: password
- **Port**: 5432
- **Data persistence**: Volume `postgres_data`

### Spring Boot Application

- **Port**: 8080
- **Environment**: Production-ready with PostgreSQL
- **Auto-restart**: Enabled
- **Health Check**: Available at `/actuator/health`

### pgAdmin (Optional)

- **Port**: 5050
- **Email**: admin@fisio.com
- **Password**: admin
- **Start with**: `docker-compose --profile dev up -d`

## Default Credentials

### Test User (Auto-created)

- **Email**: test@example.com
- **Password**: password123

### pgAdmin

- **Email**: admin@fisio.com
- **Password**: admin

## Access Points

- **Application**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Database Admin**: http://localhost:5050 (with dev profile)

## Common Commands

### Start Services

```bash
# Start in background
docker-compose up -d

# Start with build
docker-compose up --build -d

# Start with pgAdmin
docker-compose --profile dev up -d
```

### Stop Services

```bash
# Stop all services
docker-compose down

# Or use script
./scripts/stop.sh
```

### Reset Everything

```bash
# Remove all data and restart fresh
docker-compose down -v
docker-compose up --build -d

# Or use script
./scripts/reset.sh
```

### View Logs

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs app
docker-compose logs postgres

# Follow logs
docker-compose logs -f app
```

### Database Operations

```bash
# Connect to PostgreSQL
docker-compose exec postgres psql -U postgres -d fisio_db

# Backup database
docker-compose exec postgres pg_dump -U postgres fisio_db > backup.sql

# Restore database
docker-compose exec -T postgres psql -U postgres -d fisio_db < backup.sql
```

## Testing the API

### Login Test

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

## Environment Configuration

The application uses environment variables defined in docker-compose.yml. You can override them by creating a `.env` file:

```env
POSTGRES_PASSWORD=your-secure-password
JWT_SECRET=your-super-secret-jwt-key
GOOGLE_CLIENT_ID=your-google-client-id
```

## Troubleshooting

### Application Won't Start

1. Check if ports are available: `netstat -an | grep 8080`
2. View application logs: `docker-compose logs app`
3. Check database connectivity: `docker-compose exec postgres pg_isready`

### Database Issues

1. Check database logs: `docker-compose logs postgres`
2. Verify database is ready: `docker-compose exec postgres pg_isready -U postgres`
3. Reset database: `docker-compose down -v && docker-compose up -d`

### Permission Issues (Linux/Mac)

```bash
# Make scripts executable
chmod +x scripts/*.sh

# Fix Docker socket permissions
sudo usermod -aG docker $USER
```

### Container Build Issues

```bash
# Clean build
docker-compose down
docker system prune -a
docker-compose up --build
```

## Development Workflow

1. **Start development environment**:

   ```bash
   docker-compose --profile dev up -d
   ```

2. **Make code changes** (container will need rebuild)

3. **Rebuild and restart**:

   ```bash
   docker-compose up --build -d
   ```

4. **Test changes**:

   ```bash
   ./scripts/validate.sh
   ```

5. **View logs**:
   ```bash
   docker-compose logs -f app
   ```

## Production Considerations

- Change default passwords in `.env` file
- Use proper JWT secret key
- Configure Google OAuth credentials
- Set up proper CORS origins
- Consider using external database
- Implement proper backup strategy
- Monitor application logs

## Scripts

- `scripts/stop.sh` - Stop all services
- `scripts/reset.sh` - Reset entire environment
- `scripts/validate.sh` - Validate all services are running

## Support

If you encounter issues:

1. Check this documentation
2. View container logs: `docker-compose logs`
3. Verify all services are healthy: `./scripts/validate.sh`
4. Reset environment: `./scripts/reset.sh`
