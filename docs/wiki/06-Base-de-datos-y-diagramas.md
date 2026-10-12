# Base de datos y diagramas

HOOP usa MySQL 8 y JPA/Hibernate.

Base de datos local:

```text
hoop
```

## Tablas principales

- `user_role`
- `users`
- `user_credentials`
- `user_characteristics`
- `incident`

## Relaciones principales

```text
UserRole 1 ---- N User

User 1 ---- 1 UserCredentials

User 1 ---- 1 UserCharacteristics

User 1 ---- N Incident
        creates

User 1 ---- N Incident
        assigned
```

## Incident

Campos principales:

```text
id
created_by
assigned_to
title
description
room_number
department
priority
status
created_at
updated_at
```

`created_by` es obligatorio.

`assigned_to` puede ser `null` porque una incidencia puede estar creada pero todavia no asignada.

## Diagramas

### Diagrama de clases

![Diagrama de clases](../diagrams/HOOP%20-%20Class%20Diagram.png)

### Diagrama de base de datos

![Diagrama de base de datos](../diagrams/HOOP%20-%20Database%20Diagram.png)

El detalle completo del modelo esta en `docs/database_model.md`.
