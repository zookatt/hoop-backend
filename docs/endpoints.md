# Endpoints del backend

Usar este documento para revisar los endpoints que ya estan hechos, ver cuales faltan y probarlos en Postman.

La URL base en local es:

```text
http://localhost:8080/api/v1
```

Arrancar la aplicacion con perfil `local` para poder probar endpoints con `POST` y `PUT`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

En Postman, seleccionar siempre esta opcion cuando haya body:

```text
Body -> raw -> JSON
```

Comprobar tambien que el header sea:

```text
Content-Type: application/json
```

## Endpoints hechos

| Parte | Metodo | Endpoint | Que hacer | Status esperado |
| --- | --- | --- | --- | --- |
| Incidencias | GET | `/incidents` | Revisar todas las incidencias | `200 OK` |
| Incidencias | GET | `/incidents/{id}` | Revisar una incidencia por id | `200 OK` |
| Incidencias | POST | `/incidents?createdByUserId={userId}` | Crear una incidencia | `201 Created` |
| Incidencias | PUT | `/incidents/{id}` | Modificar titulo, descripcion o habitacion | `200 OK` |
| Incidencias | PUT | `/incidents/{id}/assignment` | Introducir departamento, prioridad y trabajador | `200 OK` |
| Incidencias | PUT | `/incidents/{id}/status` | Cambiar el estado de la incidencia | `200 OK` |
| Usuarios | GET | `/users` | Revisar los usuarios creados | `200 OK` |
| Usuarios | POST | `/users` | Crear un usuario para probar incidencias | `201 Created` |
| Roles | GET | `/user-roles` | Revisar los roles creados | `200 OK` |
| Roles | POST | `/user-roles` | Crear un rol | `201 Created` |

Al crear una incidencia, introducir `createdByUserId` en la URL. Esto es temporal. Mas adelante, cuando este hecha la autenticacion, el usuario deberia salir del usuario logueado.

## Endpoints pendientes

| Parte | Metodo | Endpoint | Que falta hacer |
| --- | --- | --- | --- |
| Auth | POST | `/auth/login` | Crear login |
| Auth | POST | `/auth/logout` | Crear logout |
| Usuarios | GET | `/users/{id}` | Consultar un usuario concreto |
| Usuarios | PUT | `/users/{id}` | Editar nombre o email |
| Usuarios | PUT | `/users/{id}/role` | Cambiar el rol de un usuario |
| Usuarios | PUT | `/users/{id}/deactivate` | Desactivar un usuario |
| Incidencias | GET | `/incidents/closed` | Consultar historial de incidencias cerradas |
| Incidencias | PUT | `/incidents/{id}/department` | Cambiar solo el departamento |
| Incidencias | PUT | `/incidents/{id}/priority` | Cambiar solo la prioridad |
| Incidencias | PUT | `/incidents/{id}/room-number` | Cambiar solo la habitacion |

## Probar en Postman

### 1. Crear un rol

Crear primero un rol, porque para crear un usuario hay que introducir un `roleId`.

```text
POST http://localhost:8080/api/v1/user-roles
```

Introducir este body:

```json
{
  "name": "RECEPTION"
}
```

Comprobar que devuelve `201 Created` y una respuesta parecida a esta:

```json
{
  "id": 1,
  "name": "RECEPTION"
}
```

Guardar el `id` del rol.

### 2. Crear un usuario

Crear un usuario usando el `id` del rol anterior.

```text
POST http://localhost:8080/api/v1/users
```

Introducir este body:

```json
{
  "name": "Katia",
  "email": "katia@test.com",
  "password": "123456",
  "roleId": 1
}
```

Comprobar que devuelve `201 Created` y una respuesta parecida a esta:

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

Guardar el `id` del usuario.

### 3. Crear una incidencia

Crear una incidencia usando el `id` del usuario en `createdByUserId`.

```text
POST http://localhost:8080/api/v1/incidents?createdByUserId=1
```

Introducir este body:

```json
{
  "title": "Aire acondicionado roto",
  "description": "El aire no enfria en la habitacion",
  "roomNumber": "203"
}
```

Comprobar que devuelve `201 Created` y que la incidencia se crea con estado `OPEN`:

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
  "assignedToUserId": null,
  "createdAt": "...",
  "updatedAt": "..."
}
```

### 4. Revisar incidencias

Revisar todas las incidencias:

```text
GET http://localhost:8080/api/v1/incidents
```

Comprobar que devuelve `200 OK`.

Revisar una incidencia concreta:

```text
GET http://localhost:8080/api/v1/incidents/1
```

Comprobar que devuelve `200 OK`.

### 5. Asignar departamento, prioridad y trabajador

Introducir departamento antes de asignar trabajador. Este endpoint permite enviar departamento, prioridad y trabajador en la misma peticion.

```text
PUT http://localhost:8080/api/v1/incidents/1/assignment
```

Introducir este body:

```json
{
  "department": "MAINTENANCE",
  "priority": "HIGH",
  "assignedToUserId": 1
}
```

Comprobar que devuelve `200 OK`.

### 6. Cambiar estado de la incidencia

Seguir este orden:

```text
OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED
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

Comprobar que devuelve `200 OK`.

Resolver incidencia:

```text
PUT http://localhost:8080/api/v1/incidents/1/status
```

```json
{
  "status": "RESOLVED"
}
```

Comprobar que devuelve `200 OK`.

Cerrar incidencia:

```text
PUT http://localhost:8080/api/v1/incidents/1/status
```

```json
{
  "status": "CLOSED"
}
```

Comprobar que devuelve `200 OK`.

## Errores comunes

| Error | Que revisar | Como solucionarlo |
| --- | --- | --- |
| `403 Forbidden` | Revisar si la app esta arrancada con perfil `local` | Arrancar con `-Dspring-boot.run.profiles=local` |
| `415 Unsupported Media Type` | Revisar si Postman esta mandando JSON | Seleccionar `Body -> raw -> JSON` |
| `404 User not found with id X` | Revisar si existe el usuario | Crear primero un usuario |
| `404 Role not found with id X` | Revisar si existe el rol | Crear primero un rol |
| `409 Conflict` | Revisar si se esta rompiendo una regla del servicio | Seguir el flujo correcto de estados o revisar la asignacion |
