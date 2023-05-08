package ru.yandex.yandexlavka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import ru.yandex.yandexlavka.dto.CreateCourierDto;

import java.util.List;

public record CreateCourierRequest(
        @JsonProperty("couriers")
        @Valid
        List<CreateCourierDto> couriers) {
}
