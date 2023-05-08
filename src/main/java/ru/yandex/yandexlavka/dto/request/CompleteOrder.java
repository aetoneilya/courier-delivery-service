package ru.yandex.yandexlavka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Validated
public record CompleteOrder(
        @NotNull
        @JsonProperty("courier_id")
        Long courierId,

        @NotNull
        @JsonProperty("order_id")
        Long orderId,

        @NotNull
        @JsonProperty("complete_time")
        OffsetDateTime completeTime) {
}
