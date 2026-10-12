# Reglas de incidencias

## Estado inicial

Toda incidencia nueva nace en:

```text
OPEN
```

Tambien nace sin asignacion:

```text
department = null
priority = null
assignedTo = null
```

## Flujo normal

```text
OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED
```

## Reglas de cambio de estado

- Para pasar de `OPEN` a `IN_PROGRESS`, la incidencia debe estar asignada.
- Solo una incidencia `IN_PROGRESS` puede pasar a `RESOLVED`.
- Solo una incidencia `RESOLVED` puede pasar a `CLOSED`.
- Una incidencia `OPEN` sin asignar puede cerrarse directamente.
- Una incidencia `CLOSED` no se puede editar, reasignar ni reabrir.

Si se intenta modificar una incidencia cerrada, el backend responde:

```text
409 Conflict
```

## Reglas por rol

```text
ADMIN / RECEPTION -> pueden ver todas las incidencias
MAINTENANCE       -> solo department = MAINTENANCE
CLEANING          -> solo department = CLEANING
```

`ADMIN` y `RECEPTION` pueden:

- editar datos basicos
- asignar departamento, prioridad y trabajador
- cerrar incidencias

`MAINTENANCE` y `CLEANING` pueden:

- consultar incidencias de su departamento
- cambiar estado a `IN_PROGRESS`
- cambiar estado a `RESOLVED`

No pueden cerrar incidencias.

## Orden del listado

El listado se devuelve ordenado por fecha de creacion descendente:

```text
createdAt DESC
```

Asi las incidencias mas recientes aparecen primero.
