package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;
import java.util.List;

@Validated
public record OrderDto(
        @JsonProperty("order_id")
        Long orderId,

        @NotNull
        @PositiveOrZero
        @JsonProperty("weight")
        Float weight,

        @NotNull
        @JsonProperty("regions")
        Integer regions,

        @NotNull
        @Valid
        @JsonProperty("delivery_hours")
        List<String> deliveryHours,

        @NotNull
        @PositiveOrZero
        @JsonProperty("cost")
        Integer cost,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("completed_time")
        OffsetDateTime completedTime
) {
}
