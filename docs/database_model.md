# HOOP Backend - Base de datos

HOOP usa **MySQL 8** como base de datos y **JPA/Hibernate** para gestionar la persistencia.

La base de datos principal se llama:

```text
hoop
```

## Entidades

El modelo actual esta compuesto por:

- `UserRole`
- `User`
- `UserCredentials`
- `UserCharacteristics`
- `Incident`

Tablas principales:

```text
user_role
users
user_credentials
user_characteristics
incident
```

## USER_ROLE

Almacena los roles disponibles.

```text
USER_ROLE
----------------
PK id       INT
   name     VARCHAR
```

Roles previstos:

```text
ADMIN
RECEPTION
MAINTENANCE
CLEANING
```

Relacion:

```text
UserRole 1 ---- N User
```

## USERS

Representa a los trabajadores del sistema.

```text
USERS
----------------
PK id       INT
FK role_id  INT
   active   BOOLEAN
```

`active` permite desactivar usuarios sin eliminarlos fisicamente.

## USER_CREDENTIALS

Contiene los datos usados para autenticacion.

```text
USER_CREDENTIALS
-----------------------
PK id        INT
FK user_id   INT
   email     VARCHAR
   password  VARCHAR
```

El email identifica al usuario en el login.

La password se guarda cifrada con BCrypt.

Relacion:

```text
User 1 ---- 1 UserCredentials
```

## USER_CHARACTERISTICS

Contiene datos descriptivos del trabajador.

```text
USER_CHARACTERISTICS
-----------------------
PK id       INT
FK user_id  INT
   name     VARCHAR
```

Relacion:

```text
User 1 ---- 1 UserCharacteristics
```

## INCIDENT

Representa una incidencia registrada en el alojamiento.

```text
INCIDENT
-------------------------
PK id             INT
FK created_by     INT
FK assigned_to    INT NULL
   title          VARCHAR
   description    TEXT
   room_number    VARCHAR
   department     VARCHAR NULL
   priority       VARCHAR NULL
   status         VARCHAR
   created_at     TIMESTAMP
   updated_at     TIMESTAMP
```

Una incidencia se crea con:

```text
title
description
roomNumber
createdBy
```

`createdBy` sale del usuario autenticado.

Estado inicial:

```text
OPEN
```

Inicialmente pueden estar vacios:

```text
department = null
priority = null
assignedTo = null
```

Estos datos se completan durante la valoracion y asignacion.

## Enums

### Department

```text
MAINTENANCE
CLEANING
```

### Priority

```text
LOW
MEDIUM
HIGH
```

### IncidentStatus

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

Flujo principal:

```text
OPEN
  |
  v
IN_PROGRESS
  |
  v
RESOLVED
  |
  v
CLOSED
```

Los enums se almacenan con:

```java
@Enumerated(EnumType.STRING)
```

Esto permite guardar valores legibles en MySQL.

## Relaciones

```text
UserRole 1 ---- N User

User 1 ---- 1 UserCredentials

User 1 ---- 1 UserCharacteristics

User 1 ---- N Incident
        creates

User 1 ---- N Incident
        assigned
```

`createdBy` es obligatorio.

`assignedTo` es opcional porque una incidencia puede existir antes de asignarse a un trabajador.

## Repositories

Repositories actuales:

```text
UserRoleRepository
UserRepository
UserCredentialsRepository
UserCharacteristicsRepository
IncidentRepository
```

Extienden `JpaRepository` y proporcionan operaciones como:

```text
save()
findById()
findAll()
deleteById()
existsById()
```

`IncidentRepository` tambien define:

```text
findAllByOrderByCreatedAtDesc()
```

Este metodo se usa para devolver las incidencias mas nuevas primero en `GET /api/v1/incidents`.

## Visibilidad por rol

La base de datos guarda todas las incidencias, pero el backend filtra lo que cada rol puede consultar:

```text
ADMIN / RECEPTION -> todas las incidencias
MAINTENANCE       -> solo department = MAINTENANCE
CLEANING          -> solo department = CLEANING
```

Las incidencias nuevas pueden tener `department = null`. En ese estado son visibles para `ADMIN` y `RECEPTION`, que se encargan de valorarlas y asignarlas.
