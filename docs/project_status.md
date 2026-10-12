# Estado del PMV - Backend HOOP

Este documento resume las decisiones tecnicas tomadas y el estado real del backend en el PMV.

## Decisiones implementadas

- La aplicacion es interna: no existe registro publico.
- Solo `ADMIN` puede crear usuarios internos.
- Los usuarios creados por `ADMIN` nacen con password temporal y `mustChangePassword = true`.
- El login usa `POST /api/v1/auth/token` con Basic Auth.
- Despues del login se usa JWT con `Authorization: Bearer <token>`.
- La API es stateless: el logout real se hace en frontend eliminando el token.
- Todos los roles pueden crear incidencias.
- El usuario creador de una incidencia se obtiene desde el token, no desde la URL.
- `GET /incidents` filtra por rol desde backend.
- `GET /incidents/{id}` bloquea el acceso si el rol no puede ver la incidencia.
- `ADMIN` y `RECEPTION` pueden ver todas las incidencias.
- `MAINTENANCE` solo puede ver incidencias del departamento `MAINTENANCE`.
- `CLEANING` solo puede ver incidencias del departamento `CLEANING`.
- `ADMIN` y `RECEPTION` pueden editar, asignar y cerrar incidencias.
- `MAINTENANCE` y `CLEANING` solo pueden cambiar estado a `IN_PROGRESS` o `RESOLVED` en incidencias de su departamento.
- Una incidencia `CLOSED` no se puede editar, reasignar ni reabrir.
- El listado de incidencias se ordena por `createdAt DESC`.
- `GET /users` lo pueden usar `ADMIN` y `RECEPTION`; Recepcion lo necesita para asignar trabajadores.
- Crear usuarios sigue siendo solo de `ADMIN`.

## Funcionalidad implementada

### Autenticacion

- Token JWT.
- Password cifrada con BCrypt.
- Cambio obligatorio de password inicial.
- Roles en el token como scopes.

### Usuarios

- Listar usuarios.
- Consultar usuario por id.
- Crear usuario interno.
- Crear y listar roles.

### Incidencias

- Crear incidencia.
- Listar incidencias filtradas por rol.
- Consultar detalle.
- Editar datos basicos.
- Asignar departamento, prioridad y trabajador.
- Cambiar estado.
- Cerrar incidencias.
- Bloquear modificaciones sobre incidencias cerradas.
- Responder con datos del usuario creador y usuario asignado:
  - id
  - nombre
  - rol

## No implementado o mejora futura

- Editar datos de usuario.
- Cambiar rol de usuario desde endpoint especifico.
- Desactivar usuario desde endpoint especifico.
- Recuperacion de password.
- Invalidacion real de JWT en servidor.
- Historial avanzado de incidencias cerradas.
- Endpoint separado `/incidents/closed`.
- Dashboard con estadisticas.
- Validar en backend que el trabajador asignado pertenece al departamento elegido.
- Tests unitarios e integracion completos para seguridad, usuarios e incidencias.

## Endpoints principales

Ver detalle completo en:

```text
docs/endpoints.md
```

## DTOs

Ver detalle completo en:

```text
docs/dto.md
```
