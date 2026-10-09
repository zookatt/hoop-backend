# HOOP Backend - Configuracion y ejecucion

Esta guia explica como iniciar MySQL con Docker y ejecutar el backend de HOOP.

## Requisitos

- Java 21
- Docker Desktop
- Git

El proyecto incluye Maven Wrapper, por lo que no es obligatorio instalar Maven globalmente.

## 1. Entrar en el proyecto

```powershell
cd C:\Users\zotov\Development\FactoriaF5\fullstack-personal\hoop-backend
```

## 2. Variables de entorno necesarias

La aplicacion necesita estas variables:

```text
DB_URL=jdbc:mysql://localhost:3306/hoop
DB_USERNAME=hoop_user
DB_PASSWORD=hoop_password
JWT_KEY=<clave-larga-para-firmar-jwt>
```

Docker Compose necesita estas variables:

```text
MYSQL_DATABASE=hoop
MYSQL_USER=hoop_user
MYSQL_PASSWORD=hoop_password
MYSQL_ROOT_PASSWORD=root_password
```

Si arrancas desde VS Code, puedes ponerlas en `.vscode/launch.json` dentro de `env`.

Ejemplo:

```json
{
  "DB_URL": "jdbc:mysql://localhost:3306/hoop",
  "DB_USERNAME": "hoop_user",
  "DB_PASSWORD": "hoop_password",
  "MYSQL_DATABASE": "hoop",
  "MYSQL_USER": "hoop_user",
  "MYSQL_PASSWORD": "hoop_password",
  "MYSQL_ROOT_PASSWORD": "root_password",
  "JWT_KEY": "<clave-larga-para-firmar-jwt>"
}
```

No subir claves reales a repositorios publicos.

## 3. Iniciar Docker

Docker Desktop debe estar ejecutandose antes de iniciar la base de datos.

Comprobar que Docker funciona:

```powershell
docker ps
```

## 4. Iniciar MySQL

Desde la raiz de `hoop-backend`:

```powershell
docker compose up -d
```

Comprobar los contenedores:

```powershell
docker ps
```

Deberia aparecer:

```text
hoop-mysql
```

## 5. Ejecutar Spring Boot

Con MySQL funcionando:

```powershell
.\mvnw.cmd spring-boot:run
```

Si se quiere indicar perfil local por comando:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

La aplicacion se inicia por defecto en:

```text
http://localhost:8080
```

## 6. Ejecutar desde VS Code

Usar la configuracion de launch de `HoopBackendApplication`.

Recomendado:

```json
{
  "type": "java",
  "name": "HoopBackendApplication",
  "request": "launch",
  "mainClass": "zotov.hoop_backend.HoopBackendApplication",
  "projectName": "hoop-backend",
  "cwd": "C:\\Users\\zotov\\Development\\FactoriaF5\\fullstack-personal\\hoop-backend",
  "vmArgs": "-Dspring.profiles.active=local",
  "env": {
    "DB_URL": "jdbc:mysql://localhost:3306/hoop",
    "DB_USERNAME": "hoop_user",
    "DB_PASSWORD": "hoop_password",
    "MYSQL_DATABASE": "hoop",
    "MYSQL_USER": "hoop_user",
    "MYSQL_PASSWORD": "hoop_password",
    "MYSQL_ROOT_PASSWORD": "root_password",
    "JWT_KEY": "<clave-larga-para-firmar-jwt>"
  }
}
```

## 7. Probar Swagger

Con la aplicacion arrancada:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs
```

## 8. Acceder a MySQL

Entrar al contenedor:

```powershell
docker exec -it hoop-mysql mysql -u hoop_user -p
```

Seleccionar la base de datos:

```sql
USE hoop;
```

Consultar tablas:

```sql
SHOW TABLES;
```

Consultar usuarios:

```sql
SELECT u.id, uc.email, u.active, ur.name AS role
FROM users u
JOIN user_credentials uc ON uc.user_id = u.id
JOIN user_role ur ON ur.id = u.role_id;
```

## 9. Detener MySQL

```powershell
docker compose down
```

Los datos almacenados en el volumen de MySQL se conservan.

Para borrar tambien los datos del volumen:

```powershell
docker compose down -v
```
