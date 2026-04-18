# API REST Catalog

API REST para gestión de artículos del catálogo. Permite consultar y actualizar parcialmente artículos siguiendo buenas prácticas de diseño, clean code y principios SOLID.

## Produccion

**URL Base:** https://api-rest-catalogs-32611211780.us-central1.run.app

**Swagger UI:** https://api-rest-catalogs-32611211780.us-central1.run.app/swagger-ui.html

---

## Tecnologias

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Java | 21 | LTS con virtual threads y records |
| Spring Boot | 3.4.5 | Framework principal |
| Spring Data JPA | - | Persistencia de datos |
| H2 Database | - | Base de datos en memoria |
| Bean Validation | - | Validaciones declarativas |
| SpringDoc OpenAPI | 2.8.6 | Documentación Swagger |
| JUnit 5 + Mockito | - | Testing unitario |
| Docker | - | Containerización |
| Google Cloud Run | - | Plataforma serverless |

---

## Arquitectura

```
src/main/java/com/gapsi/catalog/
├── controller/      # Capa de presentación (REST endpoints)
├── service/         # Lógica de orquestación
├── domain/          # Entidades de dominio con reglas de negocio
├── repository/      # Acceso a datos (JPA)
├── dto/             # Objetos de transferencia (records)
├── mapper/          # Conversión entre entidades y DTOs
├── exception/       # Manejo global de errores
└── config/          # Configuraciones (Jackson, etc.)
```

### Principios aplicados

- **Separación de responsabilidades:** Controller no tiene lógica, Service orquesta, Domain decide
- **Validación en capas:** DTOs validan formato, Domain valida reglas de negocio
- **Inmutabilidad parcial:** Campos no actualizables protegidos a nivel de entidad
- **DTOs como records:** Inmutables y concisos
- **Mapper manual:** Sin dependencias adicionales (no MapStruct)

---

## Ejecutar localmente

### Requisitos

- Java 21
- Maven 3.9+

### Con Maven

```bash
mvn spring-boot:run
```

### Con Docker

```bash
# Construir imagen
docker build -t api-rest-catalogs .

# Ejecutar contenedor
docker run -p 8080:8080 api-rest-catalogs
```

### Con Podman

```bash
# Construir imagen
podman build -t api-rest-catalogs .

# Ejecutar contenedor
podman run -p 8080:8080 api-rest-catalogs
```

### URLs locales

| Recurso | URL |
|---------|-----|
| API | http://localhost:8080/api/v1/articles |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |
| H2 Console | http://localhost:8080/h2-console |

**Credenciales H2:**
- JDBC URL: `jdbc:h2:mem:catalogdb`
- User: `sa`
- Password: *(vacío)*

---

## Endpoints

### GET /api/v1/articles/{id}

Obtiene un artículo por su ID.

**Request:**
```bash
curl -X GET https://api-rest-catalogs-32611211780.us-central1.run.app/api/v1/articles/ART0000001
```

**Response 200:**
```json
{
  "id": "ART0000001",
  "name": "Laptop Gaming",
  "description": "Laptop de alta gama para gaming con GPU dedicada",
  "price": 1299.99,
  "model": "XG-2024"
}
```

**Response 404:**
```json
{
  "timestamp": "2026-04-18T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Article not found with id: ART9999999",
  "path": "/api/v1/articles/ART9999999"
}
```

### PATCH /api/v1/articles/{id}

Actualiza parcialmente un artículo. Solo permite modificar `description` y `model`.

**Request:**
```bash
curl -X PATCH https://api-rest-catalogs-32611211780.us-central1.run.app/api/v1/articles/ART0000001 \
  -H "Content-Type: application/json" \
  -d '{"description": "Nueva descripción", "model": "XG-2025"}'
```

**Response 200:**
```json
{
  "id": "ART0000001",
  "name": "Laptop Gaming",
  "description": "Nueva descripción",
  "price": 1299.99,
  "model": "XG-2025"
}
```

**Response 400 (campo no permitido):**
```json
{
  "timestamp": "2026-04-18T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Field not allowed for update: price",
  "path": "/api/v1/articles/ART0000001"
}
```

---

## Validaciones

### Campos del artículo

| Campo | Reglas | Actualizable |
|-------|--------|--------------|
| id | 10 caracteres alfanuméricos | No |
| name | Máximo 20 caracteres | No |
| description | 1-200 caracteres, no vacío | Si |
| price | Decimal positivo | No |
| model | 1-10 caracteres, no vacío | Si |

### Reglas del PATCH

- Al menos un campo debe estar presente (`description` o `model`)
- No se permiten campos adicionales (`id`, `name`, `price`)
- Los valores no pueden ser nulos ni vacíos si se envían
- Si los valores son iguales a los actuales, no se actualiza

---

## Decisiones tecnicas

### Por que PATCH en lugar de PUT?

Se usa `PATCH` porque solo se actualizan campos parciales del recurso (`description` y `model`). `PUT` implicaría reemplazar el recurso completo, lo cual no aplica cuando hay campos inmutables.

### Por que rechazar campos no permitidos?

Para evitar confusión del cliente. Si el cliente envía `price` esperando que se actualice, debe recibir un error explícito en lugar de que se ignore silenciosamente.

### Por que validar en dominio y en DTO?

- **DTO:** Valida formato (longitud, no vacío) antes de llegar al servicio
- **Domain:** Valida reglas de negocio (campos requeridos, consistencia)

Esto permite fallar rápido en la capa de entrada y mantener la lógica de negocio en el dominio.

### Por que H2 en memoria?

Para simplificar el desarrollo y deploy inicial. Cloud Run es stateless, por lo que los datos se reinician con cada deploy. Para producción se recomienda Cloud SQL.

---

## Datos de prueba

La aplicación carga automáticamente 3 artículos al iniciar:

| ID | Name | Description | Price | Model |
|----|------|-------------|-------|-------|
| ART0000001 | Laptop Gaming | Laptop de alta gama para gaming con GPU dedicada | 1299.99 | XG-2024 |
| ART0000002 | Monitor 4K | Monitor Ultra HD de 27 pulgadas | 499.99 | MN-4K27 |
| ART0000003 | Teclado Mec | Teclado mecánico RGB con switches Cherry MX | 149.99 | KB-MX01 |

---

## Tests

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

---

## Deploy en Google Cloud Run

### Prerequisitos

1. Cuenta de Google Cloud Platform
2. [Google Cloud SDK](https://cloud.google.com/sdk/docs/install) instalado

### Comandos de deploy

```bash
# Autenticarse
gcloud auth login
gcloud config set project TU_PROYECTO

# Habilitar APIs
gcloud services enable cloudbuild.googleapis.com run.googleapis.com artifactregistry.googleapis.com

# Crear repositorio de imágenes
gcloud artifacts repositories create catalog-repo \
  --repository-format=docker \
  --location=us-central1

# Construir y subir imagen
gcloud builds submit --tag us-central1-docker.pkg.dev/TU_PROYECTO/catalog-repo/api-catalog:v1

# Desplegar
gcloud run deploy api-catalog \
  --image us-central1-docker.pkg.dev/TU_PROYECTO/catalog-repo/api-catalog:v1 \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --memory 512Mi
```

### Comandos utiles

```bash
# Ver logs
gcloud run services logs read api-catalog --region us-central1

# Obtener URL
gcloud run services describe api-catalog --region us-central1 --format="value(status.url)"

# Eliminar servicio
gcloud run services delete api-catalog --region us-central1
```

---

## Estructura del proyecto

```
api-rest-catalogs/
├── src/
│   ├── main/
│   │   ├── java/com/gapsi/catalog/
│   │   │   ├── CatalogApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── domain/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       └── java/com/gapsi/catalog/
│           ├── domain/
│           └── service/
├── Dockerfile
├── openapi.yaml
├── pom.xml
└── README.md
```
