package ru.yandex.yandexlavka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.yandex.yandexlavka.dto.OrderDto;

import java.util.List;

@Validated
public record GroupOrders(
        @JsonProperty("group_order_id")
        Long groupOrderId,

        @JsonProperty("orders")
        @Valid
        List<OrderDto> orders
) {
}
