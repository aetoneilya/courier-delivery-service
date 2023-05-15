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
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;
import ru.yandex.yandexlavka.dto.CreateCourierDto;
import ru.yandex.yandexlavka.dto.request.CreateCourierRequest;
import ru.yandex.yandexlavka.dto.response.CreateCouriersResponse;
import ru.yandex.yandexlavka.dto.response.GetCouriersResponse;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CouriersApiControllerIntTest {
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
    @Transactional
    @Rollback
    void createCourier() throws Exception {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(List.of(
                new CreateCourierDto(CourierTypeEnum.FOOT, List.of(1, 2, 3), List.of("11:00-12:00", "16:00-21:00")),
                new CreateCourierDto(CourierTypeEnum.AUTO, List.of(1, 4, 3), List.of("11:10-14:20", "15:00-20:30")),
                new CreateCourierDto(CourierTypeEnum.BIKE, List.of(2, 5), List.of("11:24-12:30", "16:30-21:06"))
        ));

        CreateCouriersResponse createCouriersResponse = new CreateCouriersResponse(List.of(
                new CourierDto(null, CourierTypeEnum.FOOT, List.of(1, 2, 3), List.of("11:00-12:00", "16:00-21:00")),
                new CourierDto(null, CourierTypeEnum.AUTO, List.of(1, 4, 3), List.of("11:10-14:20", "15:00-20:30")),
                new CourierDto(null, CourierTypeEnum.BIKE, List.of(2, 5), List.of("11:24-12:30", "16:30-21:06"))
        ));

        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createCourierRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(createCouriersResponse)));
    }

    @Test
    @Transactional
    @Rollback
    void createCourierBadRequest() throws Exception {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(List.of(
                new CreateCourierDto(CourierTypeEnum.FOOT, List.of(-1, 2, 3), List.of("11:00-12:00", "16:00-21:00"))
        ));

        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createCourierRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback
    void createCourierBadRequest2() throws Exception {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(List.of(
                new CreateCourierDto(CourierTypeEnum.FOOT, List.of(1, 2, 3), List.of("99:00-12:00", "16:00-21:00")),
                new CreateCourierDto(CourierTypeEnum.AUTO, List.of(1, 4, 3), List.of("11:10-14:20", "15:00-20:30")),
                new CreateCourierDto(CourierTypeEnum.BIKE, List.of(2, 5), List.of("11:24-12:30", "16:30-21:06"))
        ));

        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createCourierRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCourierByIdNotFound() throws Exception {
        mockMvc.perform(get("/couriers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourierByIdBadRequest() throws Exception {
        mockMvc.perform(get("/couriers/bad")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback
    void getCourierById() throws Exception {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(List.of(
                new CreateCourierDto(CourierTypeEnum.FOOT, List.of(1, 2, 3), List.of("11:00-12:00", "16:00-21:00")),
                new CreateCourierDto(CourierTypeEnum.AUTO, List.of(1, 4, 3), List.of("11:10-14:20", "15:00-20:30")),
                new CreateCourierDto(CourierTypeEnum.BIKE, List.of(2, 5), List.of("11:24-12:30", "16:30-21:06"))
        ));
        MvcResult mvcResult = mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createCourierRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "couriers[0].courier_id");

        CourierDto expected =
                new CourierDto(null, CourierTypeEnum.FOOT, List.of(1, 2, 3), List.of("11:00-12:00", "16:00-21:00"));

        mockMvc.perform(get("/couriers/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expected)));
    }

    @Test
    @Transactional
    @Rollback
    void getCouriers() throws Exception {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(List.of(
                new CreateCourierDto(CourierTypeEnum.FOOT, List.of(1, 2, 3), List.of("11:00-12:00", "16:00-21:00")),
                new CreateCourierDto(CourierTypeEnum.AUTO, List.of(1, 4, 3), List.of("11:10-14:20", "15:00-20:30")),
                new CreateCourierDto(CourierTypeEnum.BIKE, List.of(2, 5), List.of("11:24-12:30", "16:30-21:06"))
        ));
        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createCourierRequest)))
                .andExpect(status().isOk())
                .andReturn();

        GetCouriersResponse getCouriersResponse = new GetCouriersResponse(List.of(
                new CourierDto(null, CourierTypeEnum.AUTO, List.of(1, 4, 3), List.of("11:10-14:20", "15:00-20:30")),
                new CourierDto(null, CourierTypeEnum.BIKE, List.of(2, 5), List.of("11:24-12:30", "16:30-21:06"))
        ), 5, 1);

        mockMvc.perform(get("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("limit", "5")
                        .queryParam("offset", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(getCouriersResponse)));
    }

    @Test
    @Transactional
    @Rollback
    void getCouriersDefaultParams() throws Exception {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest(List.of(
                new CreateCourierDto(CourierTypeEnum.FOOT, List.of(1, 2, 3), List.of("11:00-12:00", "16:00-21:00")),
                new CreateCourierDto(CourierTypeEnum.AUTO, List.of(1, 4, 3), List.of("11:10-14:20", "15:00-20:30")),
                new CreateCourierDto(CourierTypeEnum.BIKE, List.of(2, 5), List.of("11:24-12:30", "16:30-21:06"))
        ));
        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createCourierRequest)))
                .andExpect(status().isOk())
                .andReturn();

        GetCouriersResponse getCouriersResponse = new GetCouriersResponse(List.of(
                new CourierDto(null, CourierTypeEnum.FOOT,
                        List.of(1, 2, 3), List.of("11:00-12:00", "16:00-21:00"))), 1, 0);

        mockMvc.perform(get("/couriers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(getCouriersResponse)));
    }
}