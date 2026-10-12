# Endpoints del backend

Usar este documento para revisar los endpoints disponibles, probarlos en Postman y entender las reglas de acceso por rol.

La URL base en local es:

```text
http://localhost:8080/api/v1
```

## Autenticacion

El login actual se hace con **Basic Auth**.

Endpoint:

```text
POST http://localhost:8080/api/v1/auth/token
```

En Postman:

```text
Authorization -> Basic Auth
Username: email del usuario
Password: password del usuario
```

No enviar JSON en este endpoint.

Si el login es correcto, la respuesta es un token JWT en texto plano:

```text
eyJhbGciOiJIUzUxMiJ9...
```

Si el usuario tiene una password temporal y debe cambiarla antes de entrar, el endpoint no devuelve token. En ese caso responde:

```text
409 Conflict
```

Mensaje esperado:

```text
Password change required before login
```

Para cambiar la password inicial:

```text
POST http://localhost:8080/api/v1/auth/change-password
```

Body:

```json
{
  "email": "cleaning1@hoop.test",
  "currentPassword": "Temporal1234",
  "newPassword": "Cleaning1234"
}
```

Despues de cambiar la password, el usuario ya puede volver a pedir token con Basic Auth.

Para usar el resto de endpoints protegidos:

```text
Authorization -> Bearer Token
Token: <token>
```

O como header:

```text
Authorization: Bearer <token>
```

## Endpoints implementados

| Parte | Metodo | Endpoint | Permisos | Que hace | Status esperado |
| --- | --- | --- | --- | --- | --- |
| Auth | POST | `/auth/token` | Usuario autenticado con Basic Auth | Genera token JWT | `200 OK` |
| Auth | POST | `/auth/change-password` | Publico con email y password actual | Cambia la password inicial y desactiva `mustChangePassword` | `200 OK` |
| Incidencias | GET | `/incidents` | Usuario autenticado | Consulta incidencias segun rol. `ADMIN` y `RECEPTION` ven todas; `MAINTENANCE` ve solo mantenimiento; `CLEANING` ve solo limpieza | `200 OK` |
| Incidencias | GET | `/incidents/{id}` | Usuario autenticado con permiso sobre la incidencia | Consulta una incidencia por id | `200 OK` |
| Incidencias | POST | `/incidents` | `ADMIN`, `RECEPTION`, `MAINTENANCE`, `CLEANING` | Crea una incidencia con el usuario logueado | `201 Created` |
| Incidencias | PUT | `/incidents/{id}` | `ADMIN`, `RECEPTION` | Modifica titulo, descripcion o habitacion | `200 OK` |
| Incidencias | PUT | `/incidents/{id}/assignment` | `ADMIN`, `RECEPTION` | Asigna departamento, prioridad y trabajador | `200 OK` |
| Incidencias | PUT | `/incidents/{id}/status` | `ADMIN`, `RECEPTION`, `MAINTENANCE`, `CLEANING` | Cambia el estado de una incidencia | `200 OK` |
| Usuarios | GET | `/users` | `ADMIN`, `RECEPTION` | Consulta los usuarios creados. Recepcion lo usa para asignar trabajadores a incidencias | `200 OK` |
| Usuarios | GET | `/users/{id}` | `ADMIN`, `RECEPTION` | Consulta un usuario concreto | `200 OK` |
| Usuarios | POST | `/users` | `ADMIN` | Crea un usuario interno | `201 Created` |
| Roles | GET | `/user-roles` | `ADMIN` | Consulta roles creados | `200 OK` |
| Roles | POST | `/user-roles` | `ADMIN` | Crea un rol | `201 Created` |

Importante: al crear una incidencia ya no se envia `createdByUserId` en la URL. El backend obtiene el usuario creador desde el token del usuario logueado.

El listado de incidencias se devuelve ordenado por fecha de creacion descendente:

```text
createdAt DESC
```

Esto hace que las incidencias mas nuevas aparezcan primero.

## Endpoints pendientes

| Parte | Metodo | Endpoint | Que falta hacer |
| --- | --- | --- | --- |
| Usuarios | PUT | `/users/{id}` | Editar nombre o email |
| Usuarios | PUT | `/users/{id}/role` | Cambiar el rol de un usuario |
| Usuarios | PUT | `/users/{id}/deactivate` | Desactivar un usuario |
| Incidencias | GET | `/incidents/closed` | Consultar historial de incidencias cerradas |

Se dejan como mejora futura:

- historial complejo
- dashboard con estadisticas
- recuperacion de password
- logout real con invalidacion de token

Con JWT stateless, el logout basico del frontend puede hacerse eliminando el token guardado en el navegador.

## Reglas de negocio de incidencias

Una incidencia nueva nace siempre en:

```text
OPEN
```

Flujo normal:

```text
OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED
```

Reglas actuales:

- Para pasar de `OPEN` a `IN_PROGRESS`, la incidencia debe estar asignada a un usuario.
- Solo una incidencia `IN_PROGRESS` puede pasar a `RESOLVED`.
- Solo una incidencia `RESOLVED` puede pasar a `CLOSED`.
- Una incidencia `OPEN` sin asignar puede cerrarse directamente.
- `ADMIN` y `RECEPTION` pueden ver todas las incidencias.
- `ADMIN` y `RECEPTION` pueden editar datos basicos, asignar y cerrar incidencias.
- Una incidencia `CLOSED` no se puede editar, reasignar ni reabrir.
- Si se intenta modificar una incidencia ya cerrada, el backend responde `409 Conflict`.
- `MAINTENANCE` solo puede ver incidencias del departamento `MAINTENANCE`.
- `CLEANING` solo puede ver incidencias del departamento `CLEANING`.
- `MAINTENANCE` y `CLEANING` no pueden editar datos basicos ni asignar incidencias.
- `MAINTENANCE` y `CLEANING` solo pueden cambiar estado a `IN_PROGRESS` o `RESOLVED` en incidencias de su departamento.
- `MAINTENANCE` y `CLEANING` no pueden cerrar incidencias.
- Solo `ADMIN` y `RECEPTION` pueden cerrar incidencias.

## Probar en Postman

### 1. Obtener token

```text
POST http://localhost:8080/api/v1/auth/token
```

En `Authorization -> Basic Auth`:

```text
Username: admin@hoop.test
Password: Admin1234
```

Guardar el token devuelto.

### 2. Crear un rol

Requiere token de `ADMIN`.

```text
POST http://localhost:8080/api/v1/user-roles
```

Body:

```json
{
  "name": "RECEPTION"
}
```

Respuesta esperada:

```json
{
  "id": 1,
  "name": "RECEPTION"
}
```

### 3. Crear un usuario

Requiere token de `ADMIN`.

```text
POST http://localhost:8080/api/v1/users
```

Body:

```json
{
  "name": "Katia",
  "email": "katia@test.com",
  "password": "123456",
  "roleId": 1
}
```

Respuesta esperada:

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

Los usuarios creados por `ADMIN` nacen con `mustChangePassword: true`. Esto significa que la password enviada al crear el usuario es temporal.

### 4. Cambiar password inicial

El usuario creado no puede obtener token hasta cambiar su password inicial.

Si intenta pedir token:

```text
POST http://localhost:8080/api/v1/auth/token
```

Con Basic Auth:

```text
Username: katia@test.com
Password: 123456
```

Respuesta esperada:

```text
409 Conflict
```

Para cambiar la password:

```text
POST http://localhost:8080/api/v1/auth/change-password
```

Body:

```json
{
  "email": "katia@test.com",
  "currentPassword": "123456",
  "newPassword": "Katia1234"
}
```

Respuesta esperada:

```text
200 OK
```

Despues de esto, el usuario puede pedir token con:

```text
Username: katia@test.com
Password: Katia1234
```

### 5. Crear una incidencia

Requiere token de cualquier rol.

```text
POST http://localhost:8080/api/v1/incidents
```

Body:

```json
{
  "title": "Aire acondicionado roto",
  "description": "El aire no enfria en la habitacion",
  "roomNumber": "203"
}
```

Respuesta esperada:

```json
{
  "id": 1,
  "title": "Aire acondicionado roto",
  "description": "El aire no enfria en la habitacion",
  "roomNumber": "203",
  "department": null,
  "priority": null,
  "status": "OPEN",
  "createdByUserId": 1,
  "createdByUserName": "Katia",
  "createdByUserRoleName": "RECEPTION",
  "assignedToUserId": null,
  "assignedToUserName": null,
  "assignedToUserRoleName": null,
  "createdAt": "...",
  "updatedAt": "..."
}
```

### 6. Consultar incidencias

Requiere token de cualquier usuario autenticado.

```text
GET http://localhost:8080/api/v1/incidents
```

```text
GET http://localhost:8080/api/v1/incidents/1
```

Segun rol:

```text
ADMIN / RECEPTION -> reciben todas las incidencias
MAINTENANCE       -> recibe solo incidencias con department = MAINTENANCE
CLEANING          -> recibe solo incidencias con department = CLEANING
```

Si `MAINTENANCE` o `CLEANING` intenta consultar por id una incidencia de otro departamento, la respuesta esperada es:

```text
403 Forbidden
```

### 7. Asignar departamento, prioridad y trabajador

Requiere token de `ADMIN` o `RECEPTION`.

```text
PUT http://localhost:8080/api/v1/incidents/1/assignment
```

Body:

```json
{
  "department": "MAINTENANCE",
  "priority": "HIGH",
  "assignedToUserId": 2
}
```

Decision de PMV:

- el backend recibe `assignedToUserId`
- el frontend muestra un desplegable de trabajadores
- si el departamento es `MAINTENANCE`, solo muestra usuarios con rol `MAINTENANCE`
- si el departamento es `CLEANING`, solo muestra usuarios con rol `CLEANING`

La validacion principal de permisos sigue estando en backend: solo `ADMIN` y `RECEPTION` pueden usar este endpoint.

### 8. Cambiar estado

Permisos:

```text
ADMIN / RECEPTION -> pueden cambiar estados segun reglas de negocio
MAINTENANCE       -> solo IN_PROGRESS o RESOLVED en incidencias MAINTENANCE
CLEANING          -> solo IN_PROGRESS o RESOLVED en incidencias CLEANING
```

Empezar trabajo:

```text
PUT http://localhost:8080/api/v1/incidents/1/status
```

```json
{
  "status": "IN_PROGRESS"
}
```

Resolver:

```text
PUT http://localhost:8080/api/v1/incidents/1/status
```

```json
{
  "status": "RESOLVED"
}
```

Cerrar:

```text
PUT http://localhost:8080/api/v1/incidents/1/status
```

```json
{
  "status": "CLOSED"
}
```

Antes de cerrar, el frontend muestra una confirmacion visual porque las incidencias cerradas no se pueden volver a editar ni reabrir.

## Errores comunes

| Error | Que revisar | Como solucionarlo |
| --- | --- | --- |
| `401 Unauthorized` | Token ausente, expirado o credenciales Basic Auth incorrectas | Pedir un token nuevo en `/auth/token` |
| `403 Forbidden` | El rol no tiene permiso para esa accion | Probar con un usuario con rol permitido |
| `415 Unsupported Media Type` | Postman no esta enviando JSON en endpoints con body | Usar `Body -> raw -> JSON` |
| `400 Bad Request` | Falta el campo `status` al cambiar estado | Enviar `{ "status": "..." }` |
| `404 User not found with id X` | No existe el usuario asignado | Crear o revisar el usuario |
| `404 Incident not found with id X` | No existe la incidencia | Revisar el id |
| `409 Conflict` | Se rompe una regla de negocio | Revisar flujo de estados y asignacion |
| `409 Password change required before login` | El usuario tiene password temporal | Usar `/auth/change-password` antes de pedir token |
