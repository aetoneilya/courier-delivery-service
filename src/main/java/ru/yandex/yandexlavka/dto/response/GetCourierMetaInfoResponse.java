package ru.yandex.yandexlavka.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;

import java.util.List;

@Validated
public record GetCourierMetaInfoResponse(
        @JsonProperty("courier_id")
        Long courierId,

        @JsonProperty("courier_type")
        CourierTypeEnum courierType,

        @JsonProperty("regions")
        @Valid
        List<Integer> regions,

        @JsonProperty("working_hours")
        @Valid
        List<String> workingHours,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("rating")
        Integer rating,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("earnings")
        Integer earnings) {
}
