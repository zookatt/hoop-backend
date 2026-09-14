# DTOs - HOOP Backend

Los DTOs (Data Transfer Objects) definen los datos que entran y salen de la API.

```text
Frontend
   │
   │ DTO Request
   ▼
Backend
   │
   │ DTO Response
   ▼
Frontend
```

Los DTOs evitan exponer directamente las entidades de la base de datos.

---

## Incident DTOs

| DTO                              | Tipo     | Datos                                  |
| -------------------------------- | -------- | -------------------------------------- |
| `CreateIncidentDTORequest`       | Request  | title, description, roomNumber         |
| `UpdateIncidentDTORequest`       | Request  | title, description, roomNumber         |
| `AssignIncidentDTORequest`       | Request  | department, priority, assignedToUserId |
| `UpdateIncidentStatusDTORequest` | Request  | status                                 |
| `IncidentDTOResponse`            | Response | datos completos de la incidencia       |

### CreateIncidentDTORequest

Datos enviados para crear una incidencia:

```text
title
description
roomNumber
```

El backend establece automáticamente:

```text
status = OPEN
createdBy = usuario autenticado
createdAt
updatedAt
```

La incidencia puede comenzar sin:

```text
department
priority
assignedTo
```

### UpdateIncidentDTORequest

Datos que se pueden modificar:

```text
title
description
roomNumber
```

### AssignIncidentDTORequest

Datos utilizados para valorar y asignar la incidencia:

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
  "assignedToUserId": 8
}
```

### UpdateIncidentStatusDTORequest

Envía el nuevo estado:

```text
status
```

Estados:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

### IncidentDTOResponse

Datos que recibe el frontend:

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

---

## User DTOs

| DTO                    | Tipo     | Datos                                     |
| ---------------------- | -------- | ----------------------------------------- |
| `CreateUserDTORequest` | Request  | name, email, password, roleId             |
| `UpdateUserDTORequest` | Request  | name, email                               |
| `UserDTOResponse`      | Response | id, name, email, active, roleId, roleName |

### CreateUserDTORequest

Datos enviados para crear un usuario:

```text
name
email
password
roleId
```

### UpdateUserDTORequest

Datos enviados para modificar un usuario:

```text
name
email
```

El rol y el estado `active` se gestionarán mediante operaciones específicas.

### UserDTOResponse

Datos que recibe el frontend:

```text
id
name
email
active
roleId
roleName
```

La contraseña nunca se devuelve en un DTO Response.

---

## Authentication DTOs

| DTO                | Tipo     | Datos                     |
| ------------------ | -------- | ------------------------- |
| `LoginDTORequest`  | Request  | email, password           |
| `LoginDTOResponse` | Response | userId, name, email, role |

### LoginDTORequest

Datos enviados al iniciar sesión:

```text
email
password
```

### LoginDTOResponse

Datos que recibe el frontend después de un login correcto:

```text
userId
name
email
role
```

No se ha creado un DTO para logout porque todavía no es necesario. Su implementación dependerá de la estrategia de autenticación utilizada con Spring Security.

---

## Resumen

```text
REQUEST                         RESPONSE

CreateIncidentDTORequest   ──→  IncidentDTOResponse
UpdateIncidentDTORequest   ──→  IncidentDTOResponse
AssignIncidentDTORequest   ──→  IncidentDTOResponse
UpdateIncidentStatusDTORequest
                           ──→  IncidentDTOResponse

CreateUserDTORequest       ──→  UserDTOResponse
UpdateUserDTORequest       ──→  UserDTOResponse

LoginDTORequest            ──→  LoginDTOResponse
```
