# DTOs y contratos

Los DTOs separan las entidades internas de los datos que entran y salen por la API.

```text
Frontend/Postman
   |
   | DTO Request
   v
Backend
   |
   | DTO Response
   v
Frontend/Postman
```

## Auth

`POST /api/v1/auth/token` no usa DTO JSON. Usa Basic Auth con:

```text
email
password
```

Respuesta:

```text
JWT en texto plano
```

`POST /api/v1/auth/change-password` usa:

```json
{
  "email": "katia@test.com",
  "currentPassword": "123456",
  "newPassword": "Katia1234"
}
```

## Incidencias

| DTO | Tipo | Uso |
| --- | --- | --- |
| `CreateIncidentDTORequest` | Request | Crear incidencia |
| `UpdateIncidentDTORequest` | Request | Editar datos basicos |
| `AssignIncidentDTORequest` | Request | Asignar departamento, prioridad y trabajador |
| `UpdateIncidentStatusDTORequest` | Request | Cambiar estado |
| `IncidentDTOResponse` | Response | Respuesta completa |

## Usuarios

| DTO | Tipo | Uso |
| --- | --- | --- |
| `CreateUserDTORequest` | Request | Crear usuario interno |
| `UpdateUserDTORequest` | Request | Previsto para editar usuario |
| `UserDTOResponse` | Response | Datos publicos del usuario |
| `ChangePasswordDTORequest` | Request | Cambiar password inicial |

## Roles

| DTO | Tipo | Uso |
| --- | --- | --- |
| `CreateUserRoleDTORequest` | Request | Crear rol |
| `UserRoleDTOResponse` | Response | Respuesta de rol |

## Notas importantes

- La password nunca se devuelve en respuestas.
- `createdByUserId` ya no se envia al crear incidencias.
- El creador de la incidencia sale del token del usuario logueado.
- Los enums se devuelven en ingles desde backend y se traducen en frontend.

El detalle completo esta en `docs/dto.md`.
