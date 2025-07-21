# Inditex Price Service
## 🚀 Descripción General
Este proyecto implementa un servicio REST utilizando Spring Boot, diseñado para consultar precios de productos de diferentes marcas. La aplicación sigue una **arquitectura hexagonal** para asegurar una clara separación de responsabilidades, alta mantenibilidad y facilidad de prueba. Utiliza una base de datos H2 en memoria para la persistencia de datos, precargada con información de precios de ejemplo.

El servicio está optimizado para manejar solicitudes de precios para diversos productos y marcas, devolviendo el precio más aplicable basado en un rango de fechas y un sistema de prioridad.

## 🛠️ Tecnologías Utilizadas
- **Java 17:** Lenguaje de programación.

- **Spring Boot 3.2.1:** Framework para el desarrollo rápido de aplicaciones Java.

- **Spring Data JPA:** Abstracción para la capa de persistencia, facilitando la interacción con la base de datos.

- **H2 Database:** Base de datos relacional en memoria, ideal para desarrollo y pruebas.

- **Lombok:** Librería que reduce el código boilerplate (repetitivo) en las clases Java.

- **Springdoc OpenAPI (Swagger UI):** Para la generación automática y visualización interactiva de la documentación de la API.

- **JUnit 5:** Framework de pruebas unitarias y de integración.

- **RestAssured:** Librería para facilitar las pruebas de APIs REST.

- **Docker & Docker Compose:** Para la contenerización y orquestación del servicio.

- **GitHub Actions:** Para la integración continua (CI/CD).

## 🏗️ Arquitectura del Código
El proyecto sigue una **arquitectura hexagonal (Puertos y Adaptadores)**, dividiendo la aplicación en capas claras:

- **Dominio (`com.inditex.price.domain`):** Contiene la lógica de negocio central (`Price` model) y las interfaces (puertos) que definen cómo el dominio interactúa con el exterior (`PriceRepository`). Es completamente agnóstico a la tecnología.

- **Aplicación (`com.inditex.price.application`):** Aquí reside el caso de uso (`GetPriceUseCase`), que orquesta las operaciones del dominio. Utiliza los puertos definidos en el dominio.

- **Infraestructura (`com.inditex.price.infrastructure`):** Implementa los adaptadores que conectan el dominio y la aplicación con tecnologías externas:

- **Controladores (`controller`):** Adaptadores de entrada REST (`PriceController`).

- **Persistencia (`repository`):** Adaptadores de base de datos (`JpaPriceRepositoryAdapter` que implementa `PriceRepository` y usa `JpaPriceRepository`).

- **Configuración (`config`):** Configuraciones específicas de Spring y otras librerías (ej. Swagger).

Esta estructura garantiza que la lógica de negocio principal sea independiente de los detalles de implementación, facilitando cambios y pruebas.

## 🚨 Manejo de Errores
El servicio cuenta con un manejador global de excepciones (GlobalExceptionHandler) que proporciona respuestas HTTP adecuadas:

- `404 Not Found`: Cuando no se encuentra un precio que cumpla con los criterios de búsqueda.

- `500 Internal Server Error`: Para cualquier otra excepción inesperada en el servidor.

Esto asegura una experiencia de usuario consistente y clara en caso de errores.

## 🚀 Ejecución del Servicio
Para levantar el servicio, asegúrate de tener Java 17 y Maven instalados.

1. **Compilar y Ejecutar con Maven:**

    Navega a la raíz del proyecto (donde se encuentra `pom.xml`) en tu terminal y ejecuta:
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
Esto construirá la imagen Docker del servicio y lo iniciará en un contenedor. La API también estará disponible en http://localhost:8080/prices.

### Ejemplo de Petición API:
Puedes probar el endpoint GET /prices con curl o cualquier cliente REST:

```bash
curl "http://localhost:8080/prices?productId=35455&brandId=1&applicationDate=2020-06-14T16:00:00"
```
## 📄 Documentación de la API (Swagger UI)
La API está documentada automáticamente utilizando Springdoc OpenAPI. Puedes acceder a la interfaz interactiva de Swagger UI para explorar los endpoints y probar las peticiones directamente desde tu navegador:

- **Swagger UI:** http://localhost:8080/swagger-ui.html

## 🧪 Pruebas
El proyecto incluye un conjunto robusto de pruebas para garantizar la calidad y el comportamiento esperado del servicio.

1. **Ejecutar todos los tests:**

   Desde la raíz del proyecto, ejecuta:
```bash
mvn test
```
2. **Tipos de Pruebas:**

- **Tests Unitarios (`GetPriceUseCaseTest`):** Validan la lógica de negocio central del caso de uso de forma aislada, utilizando mocks para las dependencias del repositorio.

- **Tests de Integración / E2E (`PriceControllerIntegrationTest`):** Estos tests de alto nivel utilizan **RestAssured** para realizar llamadas HTTP reales al servicio desplegado localmente (en un puerto aleatorio). Cubren las **5 casuísticas de negocio** especificadas en el enunciado, verificando que el servicio devuelve los precios correctos para diferentes fechas, productos y marcas. También incluyen un test para el escenario de "precio no encontrado", asegurando que la API responde con el código de estado y el mensaje de error adecuados.

Las pruebas están diseñadas para validar que el servicio maneja con precisión las solicitudes para diferentes combinaciones de productos y marcas, y que devuelve los resultados esperados según la lógica de prioridad y rangos de fecha.

## Notas
- Arquitectura hexagonal aplicada.

- Manejo de errores: si no se encuentra precio, retorna HTTP 404.

- H2 Console: http://localhost:8080/h2-console (user: sa, sin password).