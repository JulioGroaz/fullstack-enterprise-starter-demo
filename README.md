# Fullstack Enterprise Starter

Generic fullstack monorepo template with Spring Boot backend and Angular frontend.

## Structure
- `backend/` Spring Boot 3 (Java 17) API with JWT auth, JPA, Flyway, and OpenAPI
- `frontend/` Angular 17+ standalone app with login, profile, and admin users pages
- `docker-compose.yml` local stack (MySQL, backend, frontend)

## Backend

### Prereqs
- Java 17+ (set `JAVA_HOME`)
- Maven 3.9+ (optional, Maven Wrapper included)

### Local database (MariaDB/MySQL)
1) Start MariaDB/MySQL on `localhost:3306`.
2) Create the database:
```
CREATE DATABASE starterdb;
```
Optional: create a dedicated DB user (replace values):
```
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'app1234';
GRANT ALL PRIVILEGES ON starterdb.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
```
3) Run the schema script (creates tables and roles):
```
mariadb -u root -p starterdb < backend/src/main/resources/db/migration/V1__init.sql
```
Use `mysql` instead of `mariadb` if you are on MySQL.

If you want to manage schema manually (no Flyway), set:
```
SPRING_FLYWAY_ENABLED=false
```
If you keep `ddl-auto: validate`, the tables must already exist. To skip validation, set:
```
SPRING_JPA_HIBERNATE_DDL_AUTO=none
```

### Run
PowerShell:
```
cd backend
$env:DB_USER = "appuser"         # or "root"
$env:DB_PASSWORD = "app1234"     # or "root"
$env:JWT_SECRET = "devsecretchangeitdevsecretchangeit"
.\mvnw.cmd spring-boot:run
```

macOS/Linux:
```
cd backend
DB_USER="appuser" DB_PASSWORD="app1234" JWT_SECRET="devsecretchangeitdevsecretchangeit" ./mvnw spring-boot:run
```

### Environment variables
- `DB_HOST` (default: localhost)
- `DB_PORT` (default: 3306)
- `DB_NAME` (default: starterdb)
- `DB_USER` (default: root)
- `DB_PASSWORD` (default: root)
- `JWT_SECRET` (required, use 32+ chars for HS256)
- `JWT_EXP_MINUTES` (default: 60)
- `SEED_ADMIN` (optional, true/false)
- `ADMIN_EMAIL` (required when SEED_ADMIN=true)
- `ADMIN_PASSWORD` (required when SEED_ADMIN=true)
- `SPRING_FLYWAY_ENABLED` (optional, set `false` to disable migrations)
- `SPRING_JPA_HIBERNATE_DDL_AUTO` (optional, default `validate`, set `none` to skip validation)
- `SPRING_JPA_DATABASE_PLATFORM` (optional, MariaDB: `org.hibernate.dialect.MariaDBDialect`)
- `SPRING_JPA_PROPERTIES_HIBERNATE_BOOT_ALLOW_JDBC_METADATA_ACCESS` (optional, set `false` if metadata errors)

Example values (replace with your own credentials):
```
DB_USER=appuser
DB_PASSWORD=app1234
JWT_SECRET=devsecretchangeitdevsecretchangeit
```

### Admin seed (optional)
Set these variables before startup to create a default admin user on boot:
```
SEED_ADMIN=true
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=admin1234
```
The password is stored hashed.

### MariaDB note
If you are on MariaDB and see errors like `Unknown column 'RESERVED'` or dialect metadata failures, set:
```
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MariaDBDialect
SPRING_JPA_PROPERTIES_HIBERNATE_BOOT_ALLOW_JDBC_METADATA_ACCESS=false
```

### Swagger
- `http://localhost:8080/swagger-ui.html`

## Frontend

### Prereqs
- Node 20+

### Run
```
cd frontend
npm install
npm start
```

### API base URL override
Set `window.__API_BASE_URL__` in `frontend/src/index.html` before `main.ts` runs.

### Config comments note
JSON files that must stay strict (like `frontend/package.json`) cannot include inline comments without breaking tools, so they are left without inline comments.
I file JSON che devono restare strict (come `frontend/package.json`) non possono includere commenti inline senza rompere gli strumenti, quindi sono lasciati senza commenti inline.

## Docker
```
docker compose up --build
```
