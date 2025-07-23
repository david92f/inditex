# Inditex Price Service
## Descripción General
Este microservicio RESTful, desarrollado con **Spring Boot**, permite la consulta eficiente de precios de productos por marca, producto y fecha. Su diseño bajo **Arquitectura Hexagonal (Ports & Adapters)** asegura modularidad, testabilidad y adaptabilidad. Utiliza **H2 Database en memoria** para la persistencia de datos de referencia, optimizando la determinación del precio aplicable mediante un sistema de prioridades.

## Tecnologías Utilizadas
- **Java 17:** Lenguaje de programación.

- **Spring Boot 3.2.1:** Framework para el desarrollo rápido de aplicaciones Java.

- **Spring Data JPA:** Abstracción para la capa de persistencia, facilitando la interacción con la base de datos.

- **H2 Database:** Base de datos relacional en memoria, ideal para desarrollo y pruebas.

- **Lombok:** Librería que reduce el código boilerplate (repetitivo) en las clases Java.

- **Springdoc OpenAPI (Swagger UI):** Para la generación automática y visualización interactiva de la documentación de la API.

- **JUnit 5:** Framework de pruebas unitarias y de integración.

- **RestAssured:** Librería para facilitar las pruebas de APIs REST.

- **Docker & Docker Compose:** Para la contenerización y orquestación del servicio.

## Arquitectura del Código
El proyecto sigue una **arquitectura hexagonal (Puertos y Adaptadores)**, dividiendo la aplicación en capas claras:

- **Dominio (`com.inditex.price.domain`):** Contiene la lógica de negocio central (`Price` model) y las interfaces (puertos) que definen cómo el dominio interactúa con el exterior (`PriceRepository`). Es completamente agnóstico a la tecnología.

- **Aplicación (`com.inditex.price.application`):** Aquí reside el caso de uso (`GetPriceUseCase`), que orquesta las operaciones del dominio. Utiliza los puertos definidos en el dominio.

- **Infraestructura (`com.inditex.price.infrastructure`):** Implementa los adaptadores que conectan el dominio y la aplicación con tecnologías externas (Controladores REST, Repositorios JPA, Configuración).

## Manejo de Errores
Implementa un `GlobalExceptionHandler` para un manejo consistente de excepciones, retornando:

- `404 Not Found`: Cuando no se encuentra un precio que cumpla con los criterios de búsqueda.

- `500 Internal Server Error`: Para cualquier otra excepción inesperada en el servidor.

Esto asegura una experiencia de usuario consistente y clara en caso de errores.

## Ejecución del Servicio
Para levantar el servicio, asegúrate de tener Java 17 y Maven instalados.

1. **Compilar y Ejecutar con Maven:**

   Desde la raíz del proyecto:
```bash
mvn clean install
mvn spring-boot:run
```
La API estará disponible en http://localhost:8080/prices.

La consola H2 estará disponible en http://localhost:8080/h2-console.

- **JDBC URL:** `jdbc:h2:mem:pricesdb`

- **User Name:** `sa`

- **Password:** (dejar en blanco)

2. **Ejecutar con Docker (recomendado para despliegue):**

    Asegúrate de tener Docker y Docker Compose instalados. Desde la raíz del proyecto, ejecuta:
```bash
docker-compose up --build
```
La API estará disponible en http://localhost:8080/prices.

### Ejemplo de Petición API:
Puedes probar el endpoint GET /prices con curl o cualquier cliente REST:

```bash
curl "http://localhost:8080/prices?productId=35455&brandId=1&applicationDate=2020-06-14T16:00:00"
```
## Documentación de la API (Swagger UI)
Acceda a la interfaz interactiva de Swagger UI para explorar y probar los endpoints:

- **Swagger UI:** http://localhost:8080/swagger-ui.html

## Estrategia de Testing
El proyecto cuenta con una estrategia de testing robusta:

1. **Ejecutar todos los tests:**

   Desde la raíz del proyecto, ejecuta:
```bash
mvn test
```
2. **Tipos de Pruebas:**

- **Tests Unitarios (`GetPriceUseCaseTest`):** Validación aislada de la lógica de negocio.

- **Tests de Integración / E2E (`PriceControllerIntegrationTest`):** Pruebas HTTP reales con **RestAssured** que cubren las 5 casuísticas de negocio y el escenario de "precio no encontrado".

La suite de pruebas asegura la precisión y robustez del servicio.