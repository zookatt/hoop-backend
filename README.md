# HOOP Backend

Backend de **HOOP (Hospitality Operations Optimization Platform)**, una aplicación web para la gestión de incidencias en alojamientos turísticos.

HOOP permite registrar, valorar, asignar y gestionar incidencias, facilitando la coordinación entre los departamentos de **Recepción, Mantenimiento y Limpieza**.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- MySQL 8
- Docker
- Docker Compose
- SpringDoc OpenAPI

## Dependencias

### Spring Web

Permite desarrollar la API REST del proyecto y gestionar las peticiones HTTP entre el frontend y el backend.

### Spring Data JPA

Permite trabajar con la base de datos mediante entidades y repositorios Java.

JPA, junto con Hibernate, se utiliza para mapear las clases Java con las tablas de MySQL y gestionar la persistencia de los datos.

### Spring Security

Se utilizará para implementar la autenticación y controlar el acceso a las funcionalidades de la aplicación según el rol del usuario.

Los roles definidos para HOOP son:

- `ADMIN`
- `RECEPTION`
- `MAINTENANCE`
- `CLEANING`

### MySQL Driver

Permite establecer la conexión entre Spring Boot y la base de datos MySQL.

### Validation

Permite validar los datos recibidos por la API antes de procesarlos, por ejemplo campos obligatorios o formatos de email.

### SpringDoc OpenAPI

Permite generar automáticamente la documentación de la API REST en formato OpenAPI y consultar los endpoints desde Swagger UI.

Cuando la aplicación esté arrancada, la documentación se podrá consultar en:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

### Spring Boot DevTools

Facilita el desarrollo proporcionando herramientas como el reinicio automático de la aplicación cuando se realizan cambios en el código.

### Docker Compose Support

Permite integrar Spring Boot con los servicios definidos mediante Docker Compose durante el desarrollo.

### Lombok

Permite reducir código repetitivo mediante anotaciones que pueden generar automáticamente elementos como getters, setters y constructores.

## Arquitectura

El backend sigue una arquitectura por capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

Actualmente el proyecto cuenta con las entidades JPA, los DTOs, los servicios y los repositories de Spring Data JPA.

## Diagramas

### Diagrama de clases

![Diagrama de clases de HOOP](docs/diagrams/HOOP%20-%20Class%20Diagram.png)

### Diagrama de base de datos

![Diagrama de base de datos de HOOP](docs/diagrams/HOOP%20-%20Database%20Diagram.png)

## Documentación

La documentación técnica del backend se encuentra en la carpeta `docs`:

- [Configuración y ejecución](docs/setup_instructions.md)
- [Modelo de base de datos](docs/database_model.md)
- [DTOs](docs/dto.md)

## Estado del proyecto

HOOP se encuentra actualmente en desarrollo.

Actualmente se ha implementado:

- configuración de MySQL mediante Docker Compose
- conexión entre Spring Boot y MySQL
- entidades JPA
- relaciones entre entidades
- enums de incidencias
- repositories con Spring Data JPA
- servicios
- DTOs de autenticación, usuarios e incidencias

Los siguientes pasos incluyen la implementación de controladores REST, seguridad y tests.
