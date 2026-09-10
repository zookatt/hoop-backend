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

Actualmente el proyecto cuenta con las entidades JPA y los repositories de Spring Data JPA.

## Documentación

La documentación técnica del backend se encuentra en la carpeta `docs`:

- [Configuración y ejecución](docs/SETUP.md)
- [Modelo de base de datos](docs/DATABASE.md)

## Estado del proyecto

HOOP se encuentra actualmente en desarrollo.

Actualmente se ha implementado:

- configuración de MySQL mediante Docker Compose
- conexión entre Spring Boot y MySQL
- entidades JPA
- relaciones entre entidades
- enums de incidencias
- repositories con Spring Data JPA

Los siguientes pasos incluyen la implementación de servicios, DTOs, controladores REST, seguridad y tests.