package ru.yandex.yandexlavka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.yandex.yandexlavka.dto.CourierDto;

import java.util.List;

@Validated
public record CreateCouriersResponse(
        @JsonProperty("couriers")
        @Valid
        List<CourierDto> couriers) {
}
