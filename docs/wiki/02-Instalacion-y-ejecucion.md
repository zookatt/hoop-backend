# Instalacion y ejecucion

## Requisitos

- Java 21
- Docker Desktop
- Git
- Maven o Maven Wrapper del proyecto

## Variables necesarias

La aplicacion necesita:

```text
DB_URL=jdbc:mysql://localhost:3306/hoop
DB_USERNAME=hoop_user
DB_PASSWORD=hoop_password
JWT_KEY=<clave-larga-para-firmar-jwt>
```

Docker Compose necesita:

```text
MYSQL_DATABASE=hoop
MYSQL_USER=hoop_user
MYSQL_PASSWORD=hoop_password
MYSQL_ROOT_PASSWORD=root_password
```

No se deben subir claves reales a GitHub.

## Iniciar base de datos

Desde la raiz de `hoop-backend`:

```powershell
docker compose up -d
```

Comprobar:

```powershell
docker ps
```

Debe aparecer el contenedor de MySQL.

## Ejecutar backend

Con MySQL levantado:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternativa si se usa Maven instalado:

```powershell
mvn spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

## Swagger

Con la aplicacion arrancada:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs
```

## Ejecutar tests

```powershell
mvn test
```

El reporte de cobertura se genera en:

```text
target/site/jacoco/index.html
```
