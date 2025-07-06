-- This file will be executed when PostgreSQL container starts for the first time

-- Create database if it doesn't exist (PostgreSQL will create it based on POSTGRES_DB env var)

-- Set timezone
SET timezone = 'UTC';

\echo 'Database ms_fisio initialized successfully!'
