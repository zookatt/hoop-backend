# DTOs - HOOP Backend

Usar este documento para revisar que datos entran y salen de la API.

Los DTOs sirven para no exponer directamente las entidades de la base de datos. La idea es:

```text
Frontend
   |
   | DTO Request
   v
Backend
   |
   | DTO Response
   v
Frontend
```

Un DTO `Request` recoge los datos que llegan desde Postman o desde el frontend.

Un DTO `Response` recoge los datos que devuelve el backend.

## Incident DTOs

| DTO | Tipo | Datos |
| --- | --- | --- |
| `CreateIncidentDTORequest` | Request | title, description, roomNumber |
| `UpdateIncidentDTORequest` | Request | title, description, roomNumber |
| `AssignIncidentDTORequest` | Request | department, priority, assignedToUserId |
| `UpdateIncidentStatusDTORequest` | Request | status |
| `IncidentDTOResponse` | Response | datos completos de la incidencia |

### CreateIncidentDTORequest

Introducir estos datos para crear una incidencia:

```text
title
description
roomNumber
```

Ejemplo:

```json
{
  "title": "Aire acondicionado roto",
  "description": "El aire no enfria en la habitacion",
  "roomNumber": "203"
}
```

El backend establece:

```text
status = OPEN
createdAt
updatedAt
```

Ahora mismo, en esta rama, el usuario creador se indica temporalmente con `createdByUserId` en la URL:

```text
POST /api/v1/incidents?createdByUserId=1
```

Mas adelante, cuando la autenticacion este terminada, `createdBy` deberia salir del usuario logueado.

### UpdateIncidentDTORequest

Introducir los datos que se quieran modificar:

```text
title
description
roomNumber
```

Ejemplo:

```json
{
  "title": "Aire acondicionado no funciona",
  "description": "El cliente dice que sale aire caliente",
  "roomNumber": "203"
}
```

### AssignIncidentDTORequest

Introducir los datos para valorar y asignar una incidencia:

```text
department
priority
assignedToUserId
```

Ejemplo:

```json
{
  "department": "MAINTENANCE",
  "priority": "HIGH",
  "assignedToUserId": 1
}
```

### UpdateIncidentStatusDTORequest

Introducir el nuevo estado:

```text
status
```

Estados disponibles:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

Ejemplo:

```json
{
  "status": "IN_PROGRESS"
}
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

## User DTOs

| DTO | Tipo | Datos |
| --- | --- | --- |
| `CreateUserDTORequest` | Request | name, email, password, roleId |
| `UpdateUserDTORequest` | Request | name, email |
| `UserDTOResponse` | Response | id, name, email, active, roleId, roleName |

### CreateUserDTORequest

Introducir estos datos para crear un usuario:

```text
name
email
password
roleId
```

Ejemplo:

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

La password se guarda codificada y no se devuelve en la respuesta.

### UpdateUserDTORequest

Introducir los datos que se quieran modificar:

```text
name
email
```

El rol y el estado `active` se gestionaran con operaciones especificas.

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

La password nunca se devuelve en un DTO Response.

## User Role DTOs

| DTO | Tipo | Datos |
| --- | --- | --- |
| `CreateUserRoleDTORequest` | Request | name |
| `UserRoleDTOResponse` | Response | id, name |

### CreateUserRoleDTORequest

Introducir el nombre del rol:

```text
name
```

Ejemplo:

```json
{
  "name": "RECEPTION"
}
```

Este DTO se usa en:

```text
POST /api/v1/user-roles
```

### UserRoleDTOResponse

Datos que devuelve el backend:

```text
id
name
```

Ejemplo:

```json
{
  "id": 1,
  "name": "RECEPTION"
}
```

## Authentication DTOs

| DTO | Tipo | Datos |
| --- | --- | --- |
| `LoginDTORequest` | Request | email, password |
| `LoginDTOResponse` | Response | userId, name, email, role |

### LoginDTORequest

Introducir los datos para iniciar sesion:

```text
email
password
```

### LoginDTOResponse

Datos que deberia devolver el backend despues de un login correcto:

```text
userId
name
email
role
```

La estrategia final de autenticacion todavia no esta cerrada.

No se ha creado un DTO para logout porque todavia no es necesario. Su implementacion dependera de la estrategia que se use con Spring Security.

## Resumen

```text
REQUEST                              RESPONSE

CreateIncidentDTORequest        ->   IncidentDTOResponse
UpdateIncidentDTORequest        ->   IncidentDTOResponse
AssignIncidentDTORequest        ->   IncidentDTOResponse
UpdateIncidentStatusDTORequest  ->   IncidentDTOResponse

CreateUserDTORequest            ->   UserDTOResponse
UpdateUserDTORequest            ->   UserDTOResponse

CreateUserRoleDTORequest        ->   UserRoleDTOResponse

LoginDTORequest                 ->   LoginDTOResponse
```

