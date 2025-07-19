package com.inditex.price.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceControllerIntegrationTest {

    public static final String APPLICATION_DATE = "applicationDate";
    public static final String PRODUCT_ID = "productId";
    public static final String BRAND_ID = "brandId";
    public static final String PRICE_LIST = "priceList";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String CURR = "curr";
    public static final String T_23_59_59 = "2020-12-31T23:59:59";
    public static final String PRICES = "/prices";

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Prueba 1: Petición a las 10:00 del día 14 para el producto 35455 y la marca 1 (ZARA).
    @Test
    void testPriceAt10AmOn14th() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        given().param(PRODUCT_ID, 35455)
                .param(BRAND_ID, 1)
                .param(APPLICATION_DATE, formatDateTime(applicationDate))
                .when().get(PRICES)
                .then().statusCode(200)
                .body(PRODUCT_ID, equalTo(35455))
                .body(BRAND_ID, equalTo(1))
                .body(PRICE_LIST, equalTo(1))
                .body(START_DATE, equalTo("2020-06-14T00:00:00"))
                .body(END_DATE, equalTo(T_23_59_59))
                .body(PRODUCT_PRICE, equalTo(35.50f))
                .body(CURR, equalTo("EUR"));
    }

    // Prueba 2: Petición a las 16:00 del día 14 para el producto 35455 y la marca 1 (ZARA).
    @Test
    void testPriceAt16PmOn14th() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
        given().param(PRODUCT_ID, 35455)
                .param(BRAND_ID, 1)
                .param(APPLICATION_DATE, formatDateTime(applicationDate))
                .when().get(PRICES)
                .then().statusCode(200)
                .body(PRODUCT_ID, equalTo(35455))
                .body(BRAND_ID, equalTo(1))
                .body(PRICE_LIST, equalTo(2)) // Esta tarifa tiene prioridad 1, la anterior tiene 0
                .body(START_DATE, equalTo("2020-06-14T15:00:00"))
                .body(END_DATE, equalTo("2020-06-14T18:30:00"))
                .body(PRODUCT_PRICE, equalTo(25.45f))
                .body(CURR, equalTo("EUR"));
    }

    // Prueba 3: Petición a las 21:00 del día 14 para el producto 35455 y la marca 1 (ZARA).
    @Test
    void testPriceAt21PmOn14th() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0, 0);
        given().param(PRODUCT_ID, 35455)
                .param(BRAND_ID, 1)
                .param(APPLICATION_DATE, formatDateTime(applicationDate))
                .when().get(PRICES)
                .then().statusCode(200)
                .body(PRODUCT_ID, equalTo(35455))
                .body(BRAND_ID, equalTo(1))
                .body(PRICE_LIST, equalTo(1)) // La tarifa 2 ya no está vigente, vuelve la tarifa 1
                .body(START_DATE, equalTo("2020-06-14T00:00:00"))
                .body(END_DATE, equalTo(T_23_59_59))
                .body(PRODUCT_PRICE, equalTo(35.50f))
                .body(CURR, equalTo("EUR"));
    }

    // Prueba 4: Petición a las 10:00 del día 15 para el producto 35455 y la marca 1 (ZARA).
    @Test
    void testPriceAt10AmOn15th() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0, 0);
        given().param(PRODUCT_ID, 35455)
                .param(BRAND_ID, 1)
                .param(APPLICATION_DATE, formatDateTime(applicationDate))
                .when().get(PRICES)
                .then().statusCode(200)
                .body(PRODUCT_ID, equalTo(35455))
                .body(BRAND_ID, equalTo(1))
                .body(PRICE_LIST, equalTo(3)) // Entra la tarifa 3 (prioridad 1)
                .body(START_DATE, equalTo("2020-06-15T00:00:00"))
                .body(END_DATE, equalTo("2020-06-15T11:00:00"))
                .body(PRODUCT_PRICE, equalTo(30.50f))
                .body(CURR, equalTo("EUR"));
    }

    // Prueba 5: Petición a las 21:00 del día 16 para el producto 35455 y la marca 1 (ZARA).
    @Test
    void testPriceAt21PmOn16th() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0, 0);
        given().param(PRODUCT_ID, 35455)
                .param(BRAND_ID, 1)
                .param(APPLICATION_DATE, formatDateTime(applicationDate))
                .when().get(PRICES)
                .then().statusCode(200)
                .body(PRODUCT_ID, equalTo(35455))
                .body(BRAND_ID, equalTo(1))
                .body(PRICE_LIST, equalTo(4)) // Entra la tarifa 4 (prioridad 1)
                .body(START_DATE, equalTo("2020-06-15T16:00:00"))
                .body(END_DATE, equalTo(T_23_59_59))
                .body(PRODUCT_PRICE, equalTo(38.95f))
                .body(CURR, equalTo("EUR"));
    }

    // Test adicional: Precio no encontrado
    @Test
    void testPriceNotFound() {
        LocalDateTime applicationDate = LocalDateTime.of(2025, 1, 1, 0, 0, 0); // Fecha muy posterior a los datos
        given().param(PRODUCT_ID, 99999) // Producto inexistente
                .param(BRAND_ID, 1)
                .param(APPLICATION_DATE, formatDateTime(applicationDate))
                .when().get(PRICES)
                .then().statusCode(404)
                .body("error", equalTo("Price not found"))
                .body("details", containsString("No price found for given parameters"));
    }
}
