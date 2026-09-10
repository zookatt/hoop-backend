# HOOP Backend - Base de datos

HOOP utiliza **MySQL 8** como sistema gestor de base de datos y **JPA/Hibernate** para gestionar la persistencia.

La base de datos principal se llama:

```text
hoop
```

## Entidades

El modelo está compuesto actualmente por cinco entidades:

- `UserRole`
- `User`
- `UserCredentials`
- `UserCharacteristics`
- `Incident`

Hibernate genera las siguientes tablas:

```text
user_role
users
user_credentials
user_characteristics
incident
```

## USER_ROLE

Almacena los roles disponibles en la aplicación.

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

## USERS

Representa a los usuarios del sistema.

```text
USERS
----------------
PK id       INT
FK role_id  INT
   active   BOOLEAN
```

`active` permite desactivar usuarios sin eliminarlos físicamente de la base de datos.

Relación:

```text
UserRole 1 ---- N User
```

## USER_CREDENTIALS

Contiene la información utilizada para la autenticación.

```text
USER_CREDENTIALS
-----------------------
PK id        INT
FK user_id   INT
   email     VARCHAR
   password  VARCHAR
```

El email es único y cada usuario dispone de un único registro de credenciales.

Relación:

```text
User 1 ---- 1 UserCredentials
```

Las contraseñas no se almacenarán en texto plano. La capa de seguridad se encargará de almacenar un hash seguro.

## USER_CHARACTERISTICS

Contiene información descriptiva del trabajador.

```text
USER_CHARACTERISTICS
-----------------------
PK id       INT
FK user_id  INT
   name     VARCHAR
```

Relación:

```text
User 1 ---- 1 UserCharacteristics
```

## INCIDENT

Representa una incidencia registrada dentro del alojamiento.

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

Una incidencia se crea inicialmente con los datos básicos:

```text
title
description
roomNumber
createdBy
```

Su estado inicial es:

```text
OPEN
```

Inicialmente pueden no estar definidos:

```text
department = null
priority = null
assignedTo = null
```

Estos datos se completan posteriormente durante la valoración y asignación de la incidencia.

## Department

El departamento se representa mediante el enum `Department`:

```text
MAINTENANCE
CLEANING
```

## Priority

La prioridad se representa mediante el enum `Priority`:

```text
LOW
MEDIUM
HIGH
```

## IncidentStatus

El estado se representa mediante el enum `IncidentStatus`:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

Flujo principal:

```text
OPEN
  ↓
IN_PROGRESS
  ↓
RESOLVED
  ↓
CLOSED
```

Los enums se almacenan mediante:

```java
@Enumerated(EnumType.STRING)
```

Esto permite almacenar valores legibles en MySQL en lugar de números.

## Relaciones

Las principales relaciones del modelo son:

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

`assignedTo` es opcional porque una incidencia puede existir antes de ser asignada a un trabajador.

## Repositories

Cada entidad dispone de un repository basado en Spring Data JPA:

```text
UserRoleRepository
UserRepository
UserCredentialsRepository
UserCharacteristicsRepository
IncidentRepository
```

Los repositories extienden `JpaRepository` y proporcionan operaciones básicas de persistencia como:

```text
save()
findById()
findAll()
deleteById()
existsById()
```

Las consultas específicas se añadirán según las necesidades de las funcionalidades del proyecto.