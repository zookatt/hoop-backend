# Seguridad y autenticacion

## Flujo de autenticacion

El backend usa:

- Spring Security
- Basic Auth para pedir token
- JWT para usar la API
- BCrypt para cifrar passwords
- sesiones stateless
- CSRF desactivado para API REST

## Login

Endpoint:

```text
POST /api/v1/auth/token
```

Este endpoint no recibe JSON. Se usa Basic Auth:

```text
Username: email del usuario
Password: password del usuario
```

Respuesta correcta:

```text
200 OK
```

Body:

```text
eyJhbGciOiJIUzUxMiJ9...
```

## Uso del token

Despues del login, las peticiones protegidas usan:

```text
Authorization: Bearer <token>
```

## Cambio obligatorio de password inicial

Los usuarios creados por `ADMIN` nacen con:

```text
mustChangePassword = true
```

Si intentan pedir token antes de cambiar la password, el backend responde:

```text
409 Conflict
```

Endpoint para cambiar password:

```text
POST /api/v1/auth/change-password
```

Body:

```json
{
  "email": "worker@hoop.test",
  "currentPassword": "Temporal1234",
  "newPassword": "Worker1234"
}
```

Si el cambio es correcto:

```text
200 OK
```

Despues el usuario ya puede pedir token con su nueva password.

## Logout

El backend usa JWT stateless. Por eso, en el PMV el logout se hace en frontend eliminando el token guardado.

La invalidacion real de tokens en servidor queda como mejora futura.
