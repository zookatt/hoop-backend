# HOOP Backend

Backend de **HOOP (Hospitality Operations Optimization Platform)**, una aplicacion web para gestionar incidencias internas en alojamientos turisticos.

HOOP permite registrar, valorar, asignar y cerrar incidencias, facilitando la coordinacion entre los roles de **Administracion, Recepcion, Mantenimiento y Limpieza**.

## Tecnologias

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Maven
- MySQL 8
- Docker
- Docker Compose
- SpringDoc OpenAPI

## Objetivo del PMV

El producto minimo viable actual se centra en:

- login con usuario interno
- generacion de token JWT
- usuarios con roles
- creacion de incidencias
- consulta de incidencias
- asignacion de departamento, prioridad y trabajador
- cambio de estado de incidencias
- control de permisos por rol
- filtrado de incidencias por rol
- orden de incidencias nuevas primero

No existe registro publico. Los usuarios de la aplicacion son trabajadores internos y deben ser creados por un usuario con rol `ADMIN`.

## Roles

Los roles iniciales son:

- `ADMIN`
- `RECEPTION`
- `MAINTENANCE`
- `CLEANING`

Reglas principales:

- Todos los roles pueden iniciar sesion.
- Todos los roles pueden crear incidencias.
- `ADMIN` gestiona usuarios, roles e incidencias.
- `RECEPTION` coordina, asigna, valida y cierra incidencias.
- `MAINTENANCE` y `CLEANING` trabajan solo con incidencias de su departamento y no pueden cerrarlas definitivamente.

## Arquitectura

El backend esta organizado por funcionalidad:

```text
auth
characteristics
config
credentials
incident
role
user
```

Dentro de cada funcionalidad se separan responsabilidades como:

```text
controller
dto
entity
repository
service
```

El flujo principal es:

```text
Controller
    |
Service
    |
Repository
    |
MySQL
```

## Seguridad

La seguridad actual usa:

- Spring Security
- Basic Auth para pedir el token
- JWT para usar la API despues del login
- sesiones stateless
- CSRF desactivado para API REST
- password cifrada con BCrypt

Login actual:

```text
POST /api/v1/auth/token
```

Este endpoint no recibe JSON. Se prueba con **Basic Auth** usando email y password.

Despues, el frontend o Postman debe enviar el token en cada peticion protegida:

```text
Authorization: Bearer <token>
```

## Endpoints principales

La URL base local es:

```text
http://localhost:8080/api/v1
```

Endpoints implementados:

- `POST /auth/token`
- `GET /users`
- `POST /users`
- `GET /user-roles`
- `POST /user-roles`
- `GET /incidents`
- `GET /incidents/{id}`
- `POST /incidents`
- `PUT /incidents/{id}`
- `PUT /incidents/{id}/assignment`
- `PUT /incidents/{id}/status`

Ver detalle en [docs/endpoints.md](docs/endpoints.md).

## Documentacion tecnica

- [Configuracion y ejecucion](docs/setup_instructions.md)
- [Endpoints](docs/endpoints.md)
- [DTOs](docs/dto.md)
- [Modelo de base de datos](docs/database_model.md)

## Swagger

Con la aplicacion arrancada:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs
```

## Diagramas

### Diagrama de clases

![Diagrama de clases de HOOP](docs/diagrams/HOOP%20-%20Class%20Diagram.png)

### Diagrama de base de datos

![Diagrama de base de datos de HOOP](docs/diagrams/HOOP%20-%20Database%20Diagram.png)

## Estado del proyecto

Implementado hasta ahora:

- base de datos MySQL dockerizada
- entidades JPA principales
- repositories
- servicios
- DTOs
- controladores REST principales
- login con Basic Auth
- generacion de JWT
- proteccion de endpoints por rol
- reglas de negocio para estados de incidencias
- filtrado de consulta de incidencias segun rol
- listado de incidencias ordenado por fecha de creacion descendente

Pendiente o mejora futura:

- endpoints avanzados de usuarios
- historial de incidencias cerradas
- dashboard con estadisticas
- mas tests unitarios e integracion
- documentacion final de presentacion
