package ru.yandex.yandexlavka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;

import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "yandex-lavka", ignoreUnknownFields = false)
public record ApplicationConfig (
        Map<CourierTypeEnum, Integer> couriersEarning,
        Map<CourierTypeEnum, Integer> couriersRating
) {
}
