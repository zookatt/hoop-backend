# PMV y roles

## Objetivo del PMV

El objetivo del backend de HOOP es permitir que un hotel o alojamiento gestione incidencias internas entre recepcion, mantenimiento, limpieza y administracion.

El PMV incluye:

- login de usuarios internos
- JWT para proteger la API
- creacion de usuarios por `ADMIN`
- cambio obligatorio de password inicial
- creacion y consulta de incidencias
- asignacion de departamento, prioridad y trabajador
- cambio de estado de incidencias
- filtrado de incidencias segun rol
- bloqueo de edicion sobre incidencias cerradas

No existe registro publico. Todos los usuarios son trabajadores internos.

## Roles

Roles iniciales:

- `ADMIN`
- `RECEPTION`
- `MAINTENANCE`
- `CLEANING`

## Permisos por rol

### ADMIN

Puede:

- crear usuarios
- consultar usuarios
- crear roles
- consultar roles
- crear incidencias
- consultar todas las incidencias
- editar incidencias
- asignar departamento, prioridad y trabajador
- cambiar estados
- cerrar incidencias

### RECEPTION

Puede:

- crear incidencias
- consultar todas las incidencias
- consultar usuarios para asignar trabajadores
- editar incidencias
- asignar departamento, prioridad y trabajador
- cambiar estados
- cerrar incidencias

### MAINTENANCE

Puede:

- crear incidencias
- consultar incidencias del departamento `MAINTENANCE`
- cambiar estado a `IN_PROGRESS` o `RESOLVED`

No puede cerrar incidencias definitivamente.

### CLEANING

Puede:

- crear incidencias
- consultar incidencias del departamento `CLEANING`
- cambiar estado a `IN_PROGRESS` o `RESOLVED`

No puede cerrar incidencias definitivamente.
