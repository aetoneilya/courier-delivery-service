package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record CreateCourierDto(
        @NotNull
        @JsonProperty("courier_type")
        CourierTypeEnum courierType,

        @NotNull
        @JsonProperty("regions")
        @Valid List<@Positive Integer> regions,

        @JsonProperty("working_hours")
        @Valid
        List<String> workingHours) {
}
