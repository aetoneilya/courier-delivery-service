package ru.yandex.yandexlavka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;

import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "yandex-lavka", ignoreUnknownFields = false)
public record ApplicationConfig (
        Map<CourierTypeEnum, Integer> earning,
        Map<CourierTypeEnum, Integer> rating,
        Map<CourierTypeEnum, Integer> maxWeight,
        Map<CourierTypeEnum, Integer> maxOrderAmount,
        Map<CourierTypeEnum, Integer> regionsOnDelivery,
        Map<CourierTypeEnum, Integer> timeOnFirstOrder,
        Map<CourierTypeEnum, Integer> timeOnNextOrder,
        Float costCoefNextOrder
) {
}
