# Inditex Price Service

## Descripción
Servicio REST con Spring Boot que consulta precios de productos para distintas marcas usando una base de datos H2 en memoria.

## Tecnologías
- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database
- Lombok
- Springdoc OpenAPI (Swagger)
- JUnit 5 + RestAssured para tests

## Ejecución
```bash
mvn clean install
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080/prices`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

### Ejemplo de petición:
```bash
curl "http://localhost:8080/prices?productId=35455&brandId=1&applicationDate=2020-06-14T16:00:00"
```

### Perfil de desarrollo
Para usar el perfil `dev`:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Esto habilita la consola H2 en `http://localhost:8080/h2-console`.

## Tests
Ejecutar todos los tests:
```bash
mvn test
```

Las pruebas de integración cubren 5 casos de negocio según los datos proporcionados, además de un caso de "precio no encontrado".

## Notas
- Arquitectura hexagonal aplicada.
- Manejo de errores: si no se encuentra precio, retorna HTTP 404.
- H2 Console: `http://localhost:8080/h2-console` (user: sa, sin password).
