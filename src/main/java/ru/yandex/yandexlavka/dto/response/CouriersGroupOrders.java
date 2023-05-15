package ru.yandex.yandexlavka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record CouriersGroupOrders(
        @JsonProperty("courier_id")
        Long courierId,
        @JsonProperty("orders")
        @Valid
        List<GroupOrders> orders
) {
}
