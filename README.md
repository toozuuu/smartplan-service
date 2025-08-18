# SmartPlan — Backend (smartplan-service)

Production‑ready REST API for SmartPlan, built with Java + Spring Boot and Maven. Provides authentication, member/admin features, diet‑plan logic, meals/catalog, cart & orders, and reporting.

> **Repo:** `toozuuu/smartplan-service`

---

## ✨ Features

* **Auth:** email/password login, JWT issuance & refresh, roles/permissions.
* **Members:** profile CRUD, goals & metrics.
* **Diet Plans:** create/update plans (goals & timeframes), compute daily target calories/macros.
* **Meals & Catalog:** CRUD meals, filters, search.
* **Cart & Orders:** add/remove items, checkout, order status tracking.
* **Reports:** downloadable summaries (CSV/PDF) for admins.
* **Admin:** manage users, meals, and system settings.
* **Chat (optional):** WebSocket endpoint for member–staff messaging.

---

## 🧰 Tech Stack

* **Language:** Java (17+)
* **Framework:** Spring Boot
* **Build:** Maven (wrapper included: `mvnw`)
* **Database:** MySQL 8 (or PostgreSQL with URL change)
* **Security:** Spring Security + JWT
* **Docs:** springdoc‑openapi (Swagger UI)
* **Migrations (optional):** Flyway
* **Observability:** Spring Boot Actuator
* **Containerization:** Docker / Docker Compose

---

## 📁 Project Structure (typical)

```
smartplan-service/
├─ .mvn/                    # Maven wrapper
├─ src/
│  ├─ main/
│  │  ├─ java/com/smartplan/
│  │  │  ├─ SmartPlanApplication.java
│  │  │  ├─ config/          # CORS, OpenAPI, ObjectMapper, etc.
│  │  │  ├─ security/        # JWT filters, UserDetails, PasswordEncoder
│  │  │  ├─ domain/          # JPA entities
│  │  │  ├─ repository/      # Spring Data JPA repos
│  │  │  ├─ service/         # business logic
│  │  │  ├─ web/             # controllers, DTOs, mappers
│  │  │  ├─ chat/            # (optional) WebSocket handlers
│  │  │  └─ util/
│  │  └─ resources/
│  │     ├─ application.yml
│  │     └─ db/migration/    # V1__init.sql, ... (Flyway)
│  └─ test/java/...          # unit/integration tests
├─ mvnw, mvnw.cmd
└─ pom.xml
```

---

## ⚙️ Configuration

Prefer **environment variables**; `application.yml` reads from them with sensible defaults.

**Environment variables**

```
# Server
SERVER_PORT=8080

# Database (MySQL example)
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/smartplan?useSSL=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=smartplan
SPRING_DATASOURCE_PASSWORD=smartplan
SPRING_JPA_HIBERNATE_DDL_AUTO=update   # validate|update|none
SPRING_JPA_SHOW_SQL=false

# Security / JWT
APP_JWT_ISSUER=https://smartplan.local
APP_JWT_SECRET=change_me_please
APP_JWT_ACCESS_TTL_MIN=30
APP_JWT_REFRESH_TTL_DAYS=7

# CORS — align with frontend origin(s)
APP_CORS_ALLOWED_ORIGINS=http://localhost:4200,https://app.yourdomain.com

# Files & reports
APP_STORAGE_DIR=/var/app/storage
APP_REPORTS_DIR=/var/app/reports

# Logging
LOGGING_LEVEL_ROOT=INFO
```

**`src/main/resources/application.yml` (snippet)**

```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: ${SPRING_JPA_HIBERNATE_DDL_AUTO:validate}
    open-in-view: false

app:
  cors:
    allowed-origins: ${APP_CORS_ALLOWED_ORIGINS:http://localhost:4200}
  jwt:
    issuer: ${APP_JWT_ISSUER}
    secret: ${APP_JWT_SECRET}
    accessTtlMin: ${APP_JWT_ACCESS_TTL_MIN:30}
    refreshTtlDays: ${APP_JWT_REFRESH_TTL_DAYS:7}
  files:
    storage-dir: ${APP_STORAGE_DIR:/var/app/storage}
    reports-dir: ${APP_REPORTS_DIR:/var/app/reports}
```

---

## ▶️ Getting Started (Local Dev)

1. **Create DB** (MySQL example)

```bash
mysql -u root -p -e "CREATE DATABASE smartplan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

2. **Configure env vars** (see above) or edit `application.yml`.

3. **Run the app**

```bash
./mvnw spring-boot:run
# or
./mvnw clean package
java -jar target/*.jar
```

4. **Health check**

* `GET http://localhost:8080/actuator/health`

---

## 📚 API Quick Map (examples)

> Final paths may differ by implementation; update this section to mirror your controllers.

**Auth**

* `POST /api/auth/register`
* `POST /api/auth/login`
* `POST /api/auth/refresh`

**Me / Members**

* `GET /api/me`
* `PUT /api/me`
* `GET /api/members` (admin)

**Diet Plans**

* `POST /api/diet-plans`
* `GET /api/diet-plans/{id}`
* `PUT /api/diet-plans/{id}`
* `GET /api/diet-plans/{id}/daily-goal`

**Meals & Cart**

* `GET /api/meals`
* `POST /api/meals` (admin)
* `PUT /api/meals/{id}` (admin)
* `DELETE /api/meals/{id}` (admin)
* `POST /api/cart/items` / `DELETE /api/cart/items/{id}`

**Orders**

* `POST /api/orders`
* `GET /api/orders/{id}`
* `GET /api/orders?status=...` (admin)

**Reports**

* `GET /api/admin/reports?type=...` (admin, file download)

**Docs**

* Swagger UI (if enabled): `/swagger-ui.html` or `/swagger-ui/index.html`
* OpenAPI JSON: `/v3/api-docs`

---

## 🔐 Security

* **JWT** tokens issued on login; short‑lived access + refresh tokens.
* **Password hashing:** BCrypt.
* **RBAC:** `ROLE_MEMBER`, `ROLE_ADMIN` (extend as needed).
* **CORS:** restrict to known origins; deny credentials by default.
* **Validation:** Bean Validation (Jakarta), central exception handler.

---

## 🧪 Testing

```bash
./mvnw test
```

* Use **JUnit 5** & **Mockito**.
* **Data tests:** `@DataJpaTest`.
* **Web tests:** `@WebMvcTest`.
* **Integration:** Testcontainers (MySQL/Postgres) is recommended.

**`src/test/resources/application-test.yml` (example)**

```yaml
spring:
  datasource:
    url: jdbc:tc:mysql:8:///smartplan
  jpa:
    hibernate:
      ddl-auto: update
```

---

## 🐳 Docker

**Multi‑stage Dockerfile** (example)

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -e -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre
ENV JAVA_OPTS="-Xms256m -Xmx512m"
WORKDIR /opt/app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

**Run**

```bash
docker build -t smartplan-service:latest .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/smartplan" \
  -e SPRING_DATASOURCE_USERNAME=smartplan \
  -e SPRING_DATASOURCE_PASSWORD=smartplan \
  -e APP_CORS_ALLOWED_ORIGINS="http://localhost:4200" \
  smartplan-service:latest
```

**Docker Compose** (app + MySQL)

```yaml
version: "3.9"
services:
  db:
    image: mysql:8
    environment:
      MYSQL_DATABASE: smartplan
      MYSQL_USER: smartplan
      MYSQL_PASSWORD: smartplan
      MYSQL_ROOT_PASSWORD: root
    ports: ["3306:3306"]
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 10
  api:
    build: .
    depends_on: [db]
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/smartplan
      SPRING_DATASOURCE_USERNAME: smartplan
      SPRING_DATASOURCE_PASSWORD: smartplan
      APP_CORS_ALLOWED_ORIGINS: http://localhost:4200
    ports: ["8080:8080"]
```

---

## 🛠️ CI (GitHub Actions)

**`.github/workflows/ci.yml` (example)**

```yaml
name: BE CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
          cache: maven
      - run: ./mvnw -q -e -DskipTests verify
      - run: ./mvnw -q -e test
```

---

## 🚀 Deployment Checklist

**Goal:** zero‑downtime, secure release with observability and a fast rollback.

### 0) Pre‑Deploy Readiness

* [ ] All tests green (unit + integration + e2e where applicable)
* [ ] `CHANGELOG.md` updated and semantic version bumped (e.g., `v1.0.0`)
* [ ] Image built from a clean CI run; artifact is immutable and tagged (commit SHA + semver)
* [ ] OpenAPI spec generated/validated; client apps informed of breaking changes
* [ ] DB migrations reviewed; backward‑compatible where possible

### 1) Configuration & Secrets

* [ ] All config from **env vars** or a **secret manager** (no secrets in repo)
* [ ] `APP_CORS_ALLOWED_ORIGINS` set for prod domains
* [ ] Strong `APP_JWT_SECRET`, rotated and stored in secret manager
* [ ] `SPRING_JPA_HIBERNATE_DDL_AUTO=validate` in prod (no auto create/update)
* [ ] Log level set appropriately (`INFO`); PII redaction enabled in logs

### 2) Database & Migrations

* [ ] Full **backup/snapshot** taken before deploy; restore tested recently
* [ ] Run Flyway/Liquibase migrations separately (or on app start) in controlled order
* [ ] Connection pool sized for instance count (HikariCP)
* [ ] Long‑running queries profiled; relevant indexes in place

### 3) Container/Image Hardening

* [ ] Distroless or JRE‑only runtime; run as **non‑root** user
* [ ] `HEALTHCHECK` (or Actuator `/actuator/health`) wired to orchestrator
* [ ] Resource limits (CPU/mem) and JVM flags (`-XX:+UseG1GC`, heap sizing) set
* [ ] Image labeled with `org.opencontainers.image.*` metadata

### 4) Networking & TLS

* [ ] Reverse proxy (Nginx/Traefik/API GW) with **HTTPS**, HSTS, HTTP→HTTPS redirect
* [ ] TLS certs (Let’s Encrypt/ACM) valid and auto‑renewed
* [ ] CORS restricted; no `*` in `Access-Control-Allow-Origin` for creds flows
* [ ] WAF/rate‑limit or API gateway policies for auth endpoints

### 5) Observability & Ops

* [ ] Spring Boot Actuator exposed internally; sensitive endpoints secured
* [ ] Metrics scraped (Prometheus/OpenTelemetry) with alerts on **availability, error rate, latency, saturation**
* [ ] Structured JSON logs shipped to ELK/CloudWatch/Stackdriver
* [ ] Distributed tracing enabled (OpenTelemetry) with trace IDs in logs

### 6) Release Strategy

* [ ] **Blue/Green** or **Rolling** deploy configured; single‑click rollback
* [ ] Smoke tests after shift: `GET /actuator/health`, login flow, `GET /api/meals`
* [ ] Feature flags for risky changes where possible

### 7) Security Posture

* [ ] Dependencies scanned (OWASP/Dependabot); CVEs triaged
* [ ] Security headers via proxy (CSP, X‑Content‑Type‑Options, X‑Frame‑Options)
* [ ] Audit log for admin actions
* [ ] Backup encryption + at‑rest & in‑transit encryption verified

### 8) Disaster Recovery

* [ ] Automated nightly backups with retention policy
* [ ] Quarterly restore drill passed
* [ ] Runbooks documented (incident, rollback, hotfix)

---

### Quick Starts by Target

**A) Single VM / Docker Compose (small prod or staging)**

```bash
# 1) Bring up DB + API
docker compose -f docker-compose.prod.yml up -d --build
# 2) Run migrations (if not on start)
docker compose exec api java -jar app.jar --spring.flyway.migrate
# 3) Tail logs
docker compose logs -f api
```

**B) Kubernetes (EKS/GKE/AKS)**

* [ ] Create `Secret` objects for DB creds & JWT
* [ ] `Deployment` with `readinessProbe` and `livenessProbe` hitting `/actuator/health`
* [ ] `HorizontalPodAutoscaler` based on CPU/RPS
* [ ] `Ingress` with TLS; sticky sessions **off** (JWT is stateless)

*Example probes (snippet):*

```yaml
readinessProbe:
  httpGet: { path: /actuator/health/readiness, port: 8080 }
  initialDelaySeconds: 20
  periodSeconds: 10
livenessProbe:
  httpGet: { path: /actuator/health/liveness, port: 8080 }
  initialDelaySeconds: 30
  periodSeconds: 20
```

---

### Rollback Plan

* [ ] Keep previous image tag available (e.g., `:v1.0.0` → `:v0.9.3`)
* [ ] DB migrations are **backward‑compatible**, or provide down scripts & backups
* [ ] “One command” rollback documented for your platform

> Tip: Treat infra and app as code. Commit the exact Kubernetes manifests or Compose files used for each release.

## 🧩 Contributing

1. Create a feature branch from `main`.
2. Write tests; keep coverage for critical flows.
3. Follow Conventional Commits (optional) and open a PR.

---

## 📄 License

TBD (MIT/Apache‑2.0/Proprietary).
