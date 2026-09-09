# HOOP Backend

Backend de **HOOP (Hospitality Operations Optimization Platform)**, una aplicación web para la gestión de incidencias en alojamientos turísticos.

HOOP permite registrar, valorar, asignar y gestionar incidencias, facilitando la coordinación entre los departamentos de **Recepción, Mantenimiento y Limpieza**.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- MySQL 8
- Docker
- Docker Compose

## Dependencias

### Spring Web

Permite desarrollar la API REST del proyecto y gestionar las peticiones HTTP entre el frontend y el backend.

### Spring Data JPA

Permite trabajar con la base de datos mediante entidades y repositorios Java.

JPA, junto con Hibernate, se utiliza para mapear las clases Java con las tablas de MySQL y gestionar la persistencia de los datos.

### Spring Security

Se utilizará para implementar la autenticación y controlar el acceso a las funcionalidades de la aplicación según el rol del usuario.

Los roles definidos para HOOP son:

- `ADMIN`
- `RECEPTION`
- `MAINTENANCE`
- `CLEANING`

### MySQL Driver

Permite establecer la conexión entre Spring Boot y la base de datos MySQL.

### Validation

Permite validar los datos recibidos por la API antes de procesarlos, por ejemplo campos obligatorios o formatos de email.

### Spring Boot DevTools

Facilita el desarrollo proporcionando herramientas como el reinicio automático de la aplicación cuando se realizan cambios en el código.

### Docker Compose Support

Permite integrar Spring Boot con los servicios definidos mediante Docker Compose durante el desarrollo.

### Lombok

Permite reducir código repetitivo mediante anotaciones que pueden generar automáticamente elementos como getters, setters y constructores.

Su uso será opcional dependiendo de las necesidades de cada clase.

---

## Base de datos

El proyecto utiliza **MySQL 8** como sistema gestor de base de datos.

La base de datos se ejecuta dentro de un contenedor Docker mediante Docker Compose, facilitando la configuración y reproducción del entorno de desarrollo en diferentes equipos.

La base de datos utilizada por la aplicación se llama:

```text
hoop
```

### Variables de entorno

Las credenciales y otros datos sensibles no se almacenan directamente en los archivos versionados del proyecto.

La configuración utiliza variables de entorno.

Por ejemplo, Spring Boot puede obtener la configuración de conexión mediante:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Docker Compose utiliza igualmente variables de entorno para configurar MySQL:

```yaml
environment:
  MYSQL_DATABASE: ${MYSQL_DATABASE}
  MYSQL_USER: ${MYSQL_USER}
  MYSQL_PASSWORD: ${MYSQL_PASSWORD}
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
```

El archivo `.env` contiene los valores utilizados en el entorno local y está excluido del repositorio mediante `.gitignore`.

Se puede utilizar `.env.example` como referencia para indicar qué variables necesita el proyecto sin publicar las credenciales reales.

---

## Modelo de datos

El modelo de datos se está implementando mediante entidades JPA.

Las entidades principales son:

- `UserRole`
- `User`
- `UserCredentials`
- `UserCharacteristics`
- `Incident`

Las entidades se encuentran en el paquete:

```text
entity
```

Los enums utilizados por el modelo se encuentran en:

```text
enums
```

---

## Usuarios y roles

### UserRole

Representa los diferentes roles disponibles dentro de HOOP.

Los roles previstos son:

```text
ADMIN
RECEPTION
MAINTENANCE
CLEANING
```

La relación entre roles y usuarios es:

```text
UserRole 1 ---- N User
```

Un rol puede pertenecer a varios usuarios, mientras que cada usuario tiene un rol.

### User

Representa al usuario principal dentro del sistema.

Contiene la información necesaria para identificar al usuario dentro del modelo, su estado y su rol.

Los usuarios pueden ser desactivados sin eliminarlos físicamente de la base de datos mediante el atributo `active`.

Esto permite conservar las relaciones y el histórico asociado al usuario.

### UserCredentials

Contiene la información relacionada con la autenticación del usuario.

Incluye:

- email
- password
- relación con `User`

La relación es:

```text
User 1 ---- 1 UserCredentials
```

El email debe ser único.

Las contraseñas no se almacenarán en texto plano. La implementación de seguridad se encargará de almacenarlas mediante un hash seguro.

### UserCharacteristics

Contiene información descriptiva del trabajador, como su nombre.

La relación es:

```text
User 1 ---- 1 UserCharacteristics
```

La separación entre usuario, credenciales y características permite mantener responsabilidades diferenciadas dentro del modelo.

---

## Incidencias

La entidad `Incident` representa una incidencia detectada dentro del alojamiento turístico.

Una incidencia puede contener:

- título
- descripción
- número de habitación
- departamento
- prioridad
- estado
- usuario creador
- usuario asignado
- fecha de creación
- fecha de actualización

### Creación de una incidencia

Cuando se registra inicialmente una incidencia se conocen los datos básicos:

```text
title
description
roomNumber
createdBy
```

La incidencia se crea automáticamente con:

```text
status = OPEN
```

En ese momento todavía puede no tener:

```text
department = null
priority = null
assignedTo = null
```

Esto permite registrar primero el problema y realizar posteriormente su valoración y asignación.

### Departamento

El departamento responsable se representa mediante el enum `Department`.

Valores disponibles:

```text
MAINTENANCE
CLEANING
```

El departamento puede estar inicialmente vacío.

Posteriormente, durante la valoración de la incidencia, Recepción o un usuario autorizado establece qué departamento debe encargarse del problema.

### Prioridad

La prioridad se representa mediante el enum `Priority`.

Valores disponibles:

```text
LOW
MEDIUM
HIGH
```

La prioridad puede estar inicialmente vacía y establecerse durante la valoración de la incidencia.

### Estados

El estado de una incidencia se representa mediante el enum `IncidentStatus`.

Estados disponibles:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

El flujo principal previsto es:

```text
OPEN
  ↓
IN_PROGRESS
  ↓
RESOLVED
  ↓
CLOSED
```

Una incidencia se crea en estado `OPEN`.

Posteriormente, un trabajador de Mantenimiento o Limpieza puede iniciar el trabajo, pasando la incidencia a `IN_PROGRESS`.

Cuando el trabajo termina, pasa a `RESOLVED`.

Finalmente, Recepción puede validar la resolución y cerrar la incidencia, pasando a `CLOSED`.

### Asignación

Una incidencia puede estar asignada a un usuario concreto mediante `assignedTo`.

Esta relación es opcional porque una incidencia puede existir antes de ser asignada.

Por tanto:

```text
assignedTo = null
```

es un estado válido durante las primeras fases de la incidencia.

---

## Relaciones principales

Las relaciones principales del modelo son:

```text
UserRole 1 ---- N User

User 1 ---- 1 UserCredentials

User 1 ---- 1 UserCharacteristics

User 1 ---- N Incident
        creates

User 1 ---- N Incident
        assigned
```

Una incidencia tiene un usuario creador obligatorio.

El usuario asignado es opcional hasta que la incidencia haya sido valorada y asignada.

---

## Persistencia con JPA

Las clases que representan tablas de la base de datos utilizan JPA mediante la anotación:

```java
@Entity
```

Hibernate se encarga de realizar el mapeo entre las entidades Java y las tablas de MySQL.

Durante el desarrollo se utiliza:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Esto permite que Hibernate actualice el esquema de la base de datos conforme se desarrolla el modelo.

### Enums

Los enums se almacenan utilizando:

```java
@Enumerated(EnumType.STRING)
```

De esta manera, la base de datos almacena valores legibles como:

```text
OPEN
HIGH
MAINTENANCE
```

en lugar de valores numéricos asociados a la posición del enum.

---

## Estructura actual del backend

La estructura prevista del código es:

```text
src/main/java/zotov/hoop_backend/
│
├── entity/
│   ├── UserRole.java
│   ├── User.java
│   ├── UserCredentials.java
│   ├── UserCharacteristics.java
│   └── Incident.java
│
├── enums/
│   ├── Department.java
│   ├── Priority.java
│   └── IncidentStatus.java
│
├── repository/
├── service/
├── controller/
├── dto/
├── security/
├── config/
│
└── HoopBackendApplication.java
```

La arquitectura se irá completando progresivamente conforme avance el desarrollo.

---

## Flujo general de una incidencia

El flujo funcional previsto es:

```text
Trabajador
    │
    ▼
Crea incidencia
    │
    ▼
OPEN
    │
    ▼
Recepción / Admin
valora la incidencia
    │
    ├── establece prioridad
    ├── establece departamento
    └── asigna trabajador
    │
    ▼
Trabajador asignado
inicia el trabajo
    │
    ▼
IN_PROGRESS
    │
    ▼
Trabajador resuelve
    │
    ▼
RESOLVED
    │
    ▼
Recepción valida
    │
    ▼
CLOSED
```

---

## Estado del proyecto

HOOP se encuentra actualmente en desarrollo.

En la fase actual se está trabajando en:

- configuración de MySQL mediante Docker Compose
- conexión entre Spring Boot y MySQL
- configuración de variables de entorno
- creación del modelo de entidades JPA
- definición de relaciones entre entidades
- definición de enums para incidencias

Los siguientes pasos del backend incluirán:

- repositorios JPA
- capa de servicios
- DTOs
- controladores REST
- autenticación y autorización con Spring Security
- validaciones
- gestión del flujo de incidencias
- tests unitarios
- tests de integración
