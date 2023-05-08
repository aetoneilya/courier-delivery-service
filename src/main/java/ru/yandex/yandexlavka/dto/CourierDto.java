package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record CourierDto(
        @JsonProperty("courier_id")
        Long courierId,

        @JsonProperty("courier_type")
        @NotNull
        CourierTypeEnum courierType,

        @JsonProperty("regions")
        @Valid
        List<Integer> regions,

        @JsonProperty("working_hours")
        @Valid
        List<String> workingHours
) {
}
