package ru.yandex.yandexlavka.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yandex.yandexlavka.dto.CreateOrderDto;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.dto.request.CreateOrderRequest;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class OrdersApiControllerIntTest {
    @Container
    public static PostgreSQLContainer<?> DB_CONTAINER = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
    }

    public static String asJsonString(final Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void completeOrder() {
        // TODO
    }

    @Test
    @Transactional
    @Rollback
    void createOrder() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100)
        ));

        List<OrderDto> orderDtoList = List.of(
                new OrderDto(null, 23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100, null),
                new OrderDto(null, 23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100, null),
                new OrderDto(null, 23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100, null)
        );

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(orderDtoList)));
    }

    @Test
    @Transactional
    @Rollback
    void getOrder() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100)
        ));

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createOrderRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "[0].order_id");

        OrderDto expected = new OrderDto(null, 23F, 1,
                List.of("11:00-12:00", "16:00-21:00"), 100, null);

        mockMvc.perform(get("/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expected)));
    }

    @Test
    @Transactional
    @Rollback
    void getOrders() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100)
        ));


        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createOrderRequest)))
                .andExpect(status().isOk());



        List<OrderDto> orderDtoList = List.of(
                new OrderDto(null, 23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100, null),
                new OrderDto(null, 23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100, null)
        );

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("limit", "5")
                        .queryParam("offset", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(orderDtoList)));
    }
    @Test
    @Transactional
    @Rollback
    void getOrdersDefaultParams() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100),
                new CreateOrderDto(23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100)
        ));


        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createOrderRequest)))
                .andExpect(status().isOk());



        List<OrderDto> orderDtoList = List.of(
                new OrderDto(null, 23F, 1, List.of("11:00-12:00", "16:00-21:00"), 100, null)
        );

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(orderDtoList)));
    }

    @Test
    void ordersAssign() {
        //TODO
    }
}