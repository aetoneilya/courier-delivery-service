package ru.yandex.yandexlavka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import ru.yandex.yandexlavka.dto.CreateOrderDto;

import java.util.List;

public record CreateOrderRequest(
        @JsonProperty("orders")
        @Valid
        List<CreateOrderDto> orders) {
}
