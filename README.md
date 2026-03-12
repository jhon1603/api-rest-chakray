# API REST - Gestión de Usuarios

Prueba técnica desarrollada en **Java 17 + Spring Boot 4** que
implementa una API REST para la gestión de usuarios.

## Tecnologías utilizadas

-   Java 17
-   Spring Boot 4
-   Maven
-   Lombok
-   Swagger / OpenAPI
-   AES256 Encryption

## Arquitectura del proyecto

src/main/java/com/example/apirestchakray

-   controller → Controladores REST
-   service → Lógica de negocio
-   repository → Acceso a datos (in-memory)
-   dto → Objetos de transferencia de datos
-   request → Modelos de entrada
-   response → Modelos de salida
-   exception → Manejo global de errores
-   config → Configuración de la aplicación
-   util → Utilidades (encriptación AES)
-   model → Modelos de dominio

## Cómo ejecutar el proyecto

1.  Clonar el repositorio

git clone `<repository-url>`{=html}

2.  Entrar al proyecto

cd api-rest-chakray

3.  Compilar

mvn clean install

4.  Ejecutar

mvn spring-boot:run

La aplicación se ejecutará en:

http://localhost:8080

## Documentación de la API

Swagger UI: http://localhost:8080/swagger-ui/index.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

## Endpoints

### Obtener usuarios

GET /users

### Crear usuario

POST /users

### Actualizar usuario

PATCH /users/{id}

### Eliminar usuario

DELETE /users/{id}

### Login

POST /login

## Filtros

El parámetro filter usa el formato:

atributo+operador+valor

Operadores: - co → contains - eq → equals - sw → starts with - ew → ends
with

Ejemplos:

GET /users?filter=name+co+user GET /users?filter=email+ew+mail.com GET
/users?filter=phone+sw+555 GET /users?filter=tax_id+eq+AARR990101XXX

## Manejo de errores

Ejemplo de respuesta:

{ "timestamp": "2026-03-12T10:00:00", "status": 400, "error": "Bad
Request", "message": "El parámetro 'filter' no debe ser vacío o nulo" }

## Usuarios iniciales

La aplicación inicia con **3 usuarios precargados en memoria**.

## Autor

Prueba técnica desarrollada para proceso de selección.
