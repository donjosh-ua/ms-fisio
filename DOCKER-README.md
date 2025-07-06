# MS-Fisio Docker Setup

This document explains how to run the MS-Fisio application using Docker and Docker Compose.

## Prerequisites

- Docker installed on your system
- Docker Compose installed on your system

## Quick Start

1. **Clone the repository** (if not already done):

   ```bash
   cd ms-fisio
   ```

2. **Build and run the application**:

   ```bash
   docker-compose up --build
   ```

3. **Access the application**:
   - Application: http://localhost:8080
   - Database: localhost:5432

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

## Commands

### Start services in background:

```bash
docker-compose up -d
```

### Stop services:

```bash
docker-compose down
```

### View logs:

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs app
docker-compose logs postgres
```

### Rebuild application:

```bash
docker-compose up --build app
```

### Remove everything (including volumes):

```bash
docker-compose down -v
```

## Environment Variables

The application uses the following environment variables (configured in docker-compose.yml):

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL mode
- `SPRING_JPA_SHOW_SQL`: Enable SQL logging
- `SPRING_JPA_DATABASE_PLATFORM`: Database dialect

## Database Persistence

Database data is persisted in a Docker volume named `postgres_data`. This means your data will survive container restarts.

## Development

For development, you can:

1. **Run only the database**:

   ```bash
   docker-compose up postgres
   ```

   Then run the Spring Boot app locally with the local database configuration.

2. **Override environment variables**:
   Create a `.env` file in the project root:
   ```
   POSTGRES_PASSWORD=your_secure_password
   SPRING_JPA_SHOW_SQL=false
   ```

## Troubleshooting

### Application won't start

- Check if PostgreSQL is ready: `docker-compose logs postgres`
- Ensure port 8080 is not in use

### Database connection issues

- Verify PostgreSQL container is running: `docker-compose ps`
- Check database logs: `docker-compose logs postgres`

### Rebuild from scratch

```bash
docker-compose down -v
docker-compose build --no-cache
docker-compose up
```
