package ru.yandex.yandexlavka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Validated
public record OrderAssignResponse(
        @JsonProperty("date")
        LocalDate date,

        @JsonProperty("couriers")
        @Valid
        List<CouriersGroupOrders> couriers
) {
}
