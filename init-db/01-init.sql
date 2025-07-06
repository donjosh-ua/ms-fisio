-- This file will be executed when PostgreSQL container starts for the first time

-- Set timezone
SET timezone = 'UTC';

-- Enable extensions for better functionality
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create application user for better security practices
-- (Optional: uncomment for production with separate user)
-- CREATE USER IF NOT EXISTS app_user WITH PASSWORD 'app_password';
-- GRANT ALL PRIVILEGES ON DATABASE fisio_db TO app_user;

\echo 'Database ms_fisio initialized successfully!'
\echo 'Extensions enabled: uuid-ossp'
\echo 'Timezone set to UTC'
