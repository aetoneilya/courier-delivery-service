package ru.yandex.yandexlavka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yandex.yandexlavka.controller.CouriersApiController;
import ru.yandex.yandexlavka.controller.OrdersApiController;

@SpringBootTest
@Testcontainers
class YandexLavkaApplicationTests {
    @Container
    public static PostgreSQLContainer<?> DB_CONTAINER = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
    }


    @Autowired
    private CouriersApiController couriersApiController;
    @Autowired
    private OrdersApiController ordersApiController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(couriersApiController);
        Assertions.assertNotNull(ordersApiController);
    }

}
