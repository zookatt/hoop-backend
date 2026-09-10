# HOOP Backend - Configuración y ejecución

Esta guía explica cómo iniciar la base de datos MySQL mediante Docker y ejecutar el backend de HOOP.

## Requisitos

Es necesario tener instalado:

- Java 21
- Docker Desktop
- Git

El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven globalmente.

## 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
```

Entrar en el proyecto:

```bash
cd hoop-backend
```

## 2. Iniciar Docker

Docker Desktop debe estar ejecutándose antes de iniciar la base de datos.

Para comprobar que Docker funciona:

```bash
docker ps
```

## 3. Iniciar MySQL

Desde la raíz de `hoop-backend`:

```bash
docker compose up -d
```

Docker Compose iniciará el servicio MySQL definido en `compose.yaml`.

Comprobar los contenedores:

```bash
docker ps
```

Debería aparecer:

```text
hoop-mysql
```

## 4. Ejecutar Spring Boot

Con MySQL funcionando, iniciar el backend.

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw spring-boot:run
```

La aplicación se inicia por defecto en:

```text
http://localhost:8080
```

## 5. Acceder a MySQL

Para acceder directamente a MySQL dentro del contenedor:

```bash
docker exec -it hoop-mysql mysql -u hoop_user -p
```

Seleccionar la base de datos:

```sql
USE hoop;
```

Consultar las tablas:

```sql
SHOW TABLES;
```

## 6. Detener MySQL

Para detener los servicios:

```bash
docker compose down
```

Los datos almacenados en el volumen de MySQL se conservan.

Para iniciar nuevamente el servicio:

```bash
docker compose up -d
```