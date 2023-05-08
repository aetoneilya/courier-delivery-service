package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import ru.yandex.yandexlavka.dto.request.CompleteOrder;

import java.util.List;

public record CompleteOrderRequestDto(
        @JsonProperty("complete_info")
        @Valid
        List<CompleteOrder> completeInfo) {
}
