# DTOs - HOOP Backend

Este documento resume que datos entran y salen de la API.

Los DTOs evitan exponer directamente las entidades de la base de datos:

```text
Frontend/Postman
   |
   | DTO Request
   v
Backend
   |
   | DTO Response
   v
Frontend/Postman
```

## Auth

El endpoint actual de autenticacion es:

```text
POST /api/v1/auth/token
```

No usa DTO JSON. Usa **Basic Auth** con:

```text
email
password
```

La respuesta es un JWT en texto plano:

```text
eyJhbGciOiJIUzUxMiJ9...
```

Ese token se usa despues con:

```text
Authorization: Bearer <token>
```

Los records `LoginDTORequest` y `LoginDTOResponse` existen en el proyecto, pero no forman parte del flujo activo actual.

## Incident DTOs

| DTO | Tipo | Datos |
| --- | --- | --- |
| `CreateIncidentDTORequest` | Request | `title`, `description`, `roomNumber` |
| `UpdateIncidentDTORequest` | Request | `title`, `description`, `roomNumber` |
| `AssignIncidentDTORequest` | Request | `department`, `priority`, `assignedToUserId` |
| `UpdateIncidentStatusDTORequest` | Request | `status` |
| `IncidentDTOResponse` | Response | datos completos de la incidencia |

### CreateIncidentDTORequest

Body para crear una incidencia:

```json
{
  "title": "Aire acondicionado roto",
  "description": "El aire no enfria en la habitacion",
  "roomNumber": "203"
}
```

El backend establece automaticamente:

```text
status = OPEN
createdBy = usuario logueado
createdAt
updatedAt
```

No se envia `createdByUserId` en la URL ni en el body.

### UpdateIncidentDTORequest

Body para modificar datos basicos:

```json
{
  "title": "Aire acondicionado no funciona",
  "description": "El cliente dice que sale aire caliente",
  "roomNumber": "203"
}
```

Se pueden enviar solo los campos que se quieran cambiar.

### AssignIncidentDTORequest

Body para valorar y asignar una incidencia:

```json
{
  "department": "MAINTENANCE",
  "priority": "HIGH",
  "assignedToUserId": 2
}
```

Valores posibles de `department`:

```text
MAINTENANCE
CLEANING
```

Valores posibles de `priority`:

```text
LOW
MEDIUM
HIGH
```

### UpdateIncidentStatusDTORequest

Body para cambiar estado:

```json
{
  "status": "IN_PROGRESS"
}
```

Estados posibles:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

### IncidentDTOResponse

Datos que devuelve el backend:

```text
id
title
description
roomNumber
department
priority
status
createdByUserId
assignedToUserId
createdAt
updatedAt
```

Ejemplo:

```json
{
  "id": 1,
  "title": "Aire acondicionado roto",
  "description": "El aire no enfria en la habitacion",
  "roomNumber": "203",
  "department": "MAINTENANCE",
  "priority": "HIGH",
  "status": "OPEN",
  "createdByUserId": 1,
  "assignedToUserId": 2,
  "createdAt": "2026-10-08T10:00:00",
  "updatedAt": "2026-10-08T10:00:00"
}
```

## User DTOs

| DTO | Tipo | Datos |
| --- | --- | --- |
| `CreateUserDTORequest` | Request | `name`, `email`, `password`, `roleId` |
| `UpdateUserDTORequest` | Request | `name`, `email` |
| `UserDTOResponse` | Response | `id`, `name`, `email`, `active`, `roleId`, `roleName` |

### CreateUserDTORequest

Body para crear usuario:

```json
{
  "name": "Katia",
  "email": "katia@test.com",
  "password": "123456",
  "roleId": 1
}
```

Con este request, el backend crea datos en:

```text
users
user_credentials
user_characteristics
```

La password se guarda cifrada y no se devuelve en la respuesta.

### UpdateUserDTORequest

Body previsto para modificar usuario:

```json
{
  "name": "Katia Ivanova",
  "email": "katia.ivanova@test.com"
}
```

El endpoint de update de usuario esta pendiente.

### UserDTOResponse

Datos que devuelve el backend:

```text
id
name
email
active
roleId
roleName
```

Ejemplo:

```json
{
  "id": 1,
  "name": "Katia",
  "email": "katia@test.com",
  "active": true,
  "roleId": 1,
  "roleName": "RECEPTION"
}
```

## User Role DTOs

| DTO | Tipo | Datos |
| --- | --- | --- |
| `CreateUserRoleDTORequest` | Request | `name` |
| `UserRoleDTOResponse` | Response | `id`, `name` |

### CreateUserRoleDTORequest

Body:

```json
{
  "name": "RECEPTION"
}
```

Roles previstos:

```text
ADMIN
RECEPTION
MAINTENANCE
CLEANING
```

### UserRoleDTOResponse

Ejemplo:

```json
{
  "id": 1,
  "name": "RECEPTION"
}
```

## Resumen

```text
AUTH
Basic Auth email/password      ->   JWT string

INCIDENTS
CreateIncidentDTORequest       ->   IncidentDTOResponse
UpdateIncidentDTORequest       ->   IncidentDTOResponse
AssignIncidentDTORequest       ->   IncidentDTOResponse
UpdateIncidentStatusDTORequest ->   IncidentDTOResponse

USERS
CreateUserDTORequest           ->   UserDTOResponse
UpdateUserDTORequest           ->   UserDTOResponse

ROLES
CreateUserRoleDTORequest       ->   UserRoleDTOResponse
```
