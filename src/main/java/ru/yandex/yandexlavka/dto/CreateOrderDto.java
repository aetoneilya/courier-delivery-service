package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record CreateOrderDto(
        @NotNull
        @PositiveOrZero
        @JsonProperty("weight")
        Float weight,

        @NotNull
        @Positive
        @JsonProperty("regions")
        Integer regions,

        @JsonProperty("delivery_hours")
        @Valid
        List<String> deliveryHours,

        @PositiveOrZero
        @JsonProperty("cost")
        @NotNull
        Integer cost
) {
}
