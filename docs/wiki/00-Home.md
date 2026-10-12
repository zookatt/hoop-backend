# HOOP Backend - Wiki

HOOP es una aplicacion interna para gestionar incidencias en alojamientos turisticos. El backend permite crear usuarios internos, autenticar trabajadores, registrar incidencias, asignarlas por departamento y controlar su estado segun el rol del usuario.

## Paginas recomendadas

1. [PMV y roles](01-PMV-y-roles.md)
2. [Instalacion y ejecucion](02-Instalacion-y-ejecucion.md)
3. [Seguridad y autenticacion](03-Seguridad-y-autenticacion.md)
4. [API y endpoints](04-API-y-endpoints.md)
5. [Reglas de incidencias](05-Reglas-de-incidencias.md)
6. [Base de datos y diagramas](06-Base-de-datos-y-diagramas.md)
7. [DTOs y contratos](07-DTOs-y-contratos.md)
8. [Testing y cobertura](08-Testing-y-cobertura.md)
9. [Mejoras pendientes](09-Mejoras-pendientes.md)

## Stack tecnico

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL 8
- Docker Compose
- Maven
- SpringDoc OpenAPI
- JUnit, Mockito, MockMvc, H2 y JaCoCo

## Estado actual

El PMV backend esta funcional para:

- login interno con Basic Auth y JWT
- creacion de usuarios por `ADMIN`
- cambio obligatorio de password inicial
- gestion principal de incidencias
- permisos por rol
- filtrado de incidencias por rol y departamento
- tests unitarios e integracion con cobertura superior al 70%
