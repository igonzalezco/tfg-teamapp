#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE localdb;
    CREATE DATABASE demodb;
EOSQL

# Ejecutar script en devdb
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=demodb < /docker-entrypoint-initdb.d/teamapp.sql
# Ejecutar script en localdb
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=localdb < /docker-entrypoint-initdb.d/teamapp.sql