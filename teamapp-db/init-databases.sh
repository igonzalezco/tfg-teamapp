#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE localdb;
    CREATE DATABASE demodb;
EOSQL

# Ejecutar create en devdb
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=demodb -f /sql/teamapp.sql
# Ejecutar create en localdb
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=localdb -f /sql/teamapp.sql

# Ejecutar populate en devdb
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=demodb -f /sql/teamapp-populate.sql
# Ejecutar populate en localdb
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=localdb -f /sql/teamapp-populate.sql