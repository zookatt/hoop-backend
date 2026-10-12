# API y endpoints

URL base local:

```text
http://localhost:8080/api/v1
```

## Endpoints implementados

| Parte | Metodo | Endpoint | Permisos | Estado esperado |
| --- | --- | --- | --- | --- |
| Auth | POST | `/auth/token` | Basic Auth valido | `200 OK` |
| Auth | POST | `/auth/change-password` | Publico con password actual | `200 OK` |
| Usuarios | GET | `/users` | `ADMIN`, `RECEPTION` | `200 OK` |
| Usuarios | GET | `/users/{id}` | `ADMIN`, `RECEPTION` | `200 OK` |
| Usuarios | POST | `/users` | `ADMIN` | `201 Created` |
| Roles | GET | `/user-roles` | `ADMIN` | `200 OK` |
| Roles | POST | `/user-roles` | `ADMIN` | `201 Created` |
| Incidencias | GET | `/incidents` | Usuario autenticado | `200 OK` |
| Incidencias | GET | `/incidents/{id}` | Usuario con permiso | `200 OK` |
| Incidencias | POST | `/incidents` | Todos los roles | `201 Created` |
| Incidencias | PUT | `/incidents/{id}` | `ADMIN`, `RECEPTION` | `200 OK` |
| Incidencias | PUT | `/incidents/{id}/assignment` | `ADMIN`, `RECEPTION` | `200 OK` |
| Incidencias | PUT | `/incidents/{id}/status` | Segun reglas de rol | `200 OK` |

## Crear usuario

```text
POST /api/v1/users
```

Requiere token de `ADMIN`.

Body:

```json
{
  "name": "Katia",
  "email": "katia@test.com",
  "password": "123456",
  "roleId": 1
}
```

Respuesta:

```json
{
  "id": 1,
  "name": "Katia",
  "email": "katia@test.com",
  "active": true,
  "roleId": 1,
  "roleName": "RECEPTION",
  "mustChangePassword": true
}
```

## Crear incidencia

```text
POST /api/v1/incidents
```

Requiere token de cualquier rol.

Body:

```json
{
  "title": "Aire acondicionado roto",
  "description": "El aire no enfria en la habitacion",
  "roomNumber": "203"
}
```

El backend obtiene el creador desde el usuario autenticado.

## Asignar incidencia

```text
PUT /api/v1/incidents/{id}/assignment
```

Requiere `ADMIN` o `RECEPTION`.

Body:

```json
{
  "department": "MAINTENANCE",
  "priority": "HIGH",
  "assignedToUserId": 2
}
```

## Cambiar estado

```text
PUT /api/v1/incidents/{id}/status
```

Body:

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

## Errores comunes

| Error | Significado |
| --- | --- |
| `401 Unauthorized` | Falta token o credenciales incorrectas |
| `403 Forbidden` | El rol no tiene permiso |
| `404 Not Found` | No existe el recurso |
| `409 Conflict` | Se rompe una regla de negocio |
| `415 Unsupported Media Type` | No se envio JSON correctamente |

El detalle completo esta en `docs/endpoints.md`.
