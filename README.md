# HOOP Backend

Backend de **HOOP (Hospitality Operations Optimization Platform)**, una aplicación web para la gestión de incidencias en alojamientos turísticos.

HOOP permite gestionar incidencias y facilitar la coordinación entre los departamentos de Recepción, Mantenimiento y Limpieza.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- MySQL
- Docker / Docker Compose

## Dependencias

### Spring Web

Permite desarrollar la API REST del proyecto y gestionar las peticiones HTTP entre el frontend y el backend.

### Spring Data JPA

Permite trabajar con la base de datos mediante entidades y repositorios Java. Utiliza JPA e Hibernate para realizar la persistencia de datos.

### Spring Security

Se utilizará para implementar la autenticación y controlar el acceso a las funcionalidades de la aplicación según el rol del usuario.

### MySQL Driver

Permite establecer la conexión entre Spring Boot y la base de datos MySQL.

### Validation

Permite validar los datos recibidos por la API antes de procesarlos, por ejemplo campos obligatorios o formatos de email.

### Spring Boot DevTools

Facilita el desarrollo proporcionando herramientas como el reinicio automático de la aplicación cuando se realizan cambios.

### Docker Compose Support

Permite integrar Spring Boot con los servicios definidos mediante Docker Compose durante el desarrollo.

### Lombok

Reduce código repetitivo en las clases Java mediante anotaciones para generar automáticamente elementos como getters, setters y constructores.

## Base de datos

El proyecto utiliza **MySQL 8** como sistema gestor de base de datos.

MySQL se ejecuta en un contenedor Docker mediante Docker Compose, facilitando que el entorno de desarrollo pueda reproducirse en diferentes equipos.

Las credenciales y configuración sensible se gestionan mediante variables de entorno y no se almacenan directamente en el repositorio.

## Estado del proyecto

Proyecto actualmente en desarrollo.