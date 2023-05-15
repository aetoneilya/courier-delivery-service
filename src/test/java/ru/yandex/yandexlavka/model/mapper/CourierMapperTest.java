package ru.yandex.yandexlavka.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;
import ru.yandex.yandexlavka.dto.CreateCourierDto;
import ru.yandex.yandexlavka.model.Courier;
import ru.yandex.yandexlavka.model.CourierWorkHours;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CourierMapperTest {
    private final CourierMapper courierMapper = new CourierMapper();

    @Test
    void mapToListCourier() {
        CreateCourierDto dto1 =
                new CreateCourierDto(
                        CourierTypeEnum.FOOT,
                        List.of(1, 2),
                        List.of("11:00-12:00", "16:00-21:00")
                );
        CreateCourierDto dto2 =
                new CreateCourierDto(
                        CourierTypeEnum.BIKE,
                        List.of(1),
                        List.of("00:00-12:00", "16:00-21:00", "21:23-23:59")
                );
        CreateCourierDto dto3 =
                new CreateCourierDto(
                        CourierTypeEnum.AUTO,
                        List.of(1, 2, 3, 4, 5),
                        List.of("00:00-23:59")
                );

        List<Courier> couriers = courierMapper.toListCourier(List.of(dto1, dto2, dto3));

        Courier expected1 = new Courier(null,
                Set.of(1, 2),
                List.of(new CourierWorkHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0))),
                Courier.CourierType.FOOT);
        Courier expected2 = new Courier(null,
                Set.of(1),
                List.of(new CourierWorkHours(null, LocalTime.of(0, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0)),
                        new CourierWorkHours(null, LocalTime.of(21, 23), LocalTime.of(23, 59))),
                Courier.CourierType.BIKE);
        Courier expected3 = new Courier(null,
                Set.of(1, 2, 3, 4, 5),
                List.of(new CourierWorkHours(null, LocalTime.of(0, 0), LocalTime.of(23, 59))),
                Courier.CourierType.AUTO);

        assertThat(couriers).containsExactly(expected1, expected2, expected3);
    }

    @Test
    void mapCorrectToCourier() {
        CreateCourierDto createCourierDto =
                new CreateCourierDto(
                        CourierTypeEnum.FOOT,
                        List.of(1, 2),
                        List.of("11:00-12:00", "16:00-21:00")
                );

        Courier courier = courierMapper.toCourier(createCourierDto);

        Courier expected = new Courier(null,
                Set.of(1, 2),
                List.of(new CourierWorkHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0))),
                Courier.CourierType.FOOT);

        assertThat(courier).isEqualTo(expected);
    }
    @Test
    void mapCorrectToCourier2() {
        CreateCourierDto createCourierDto =
                new CreateCourierDto(
                        CourierTypeEnum.BIKE,
                        List.of(1),
                        List.of("00:00-12:00", "16:00-21:00", "21:23-23:59")
                );

        Courier courier = courierMapper.toCourier(createCourierDto);

        Courier expected = new Courier(null,
                Set.of(1),
                List.of(new CourierWorkHours(null, LocalTime.of(0, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0)),
                        new CourierWorkHours(null, LocalTime.of(21, 23), LocalTime.of(23, 59))),
                Courier.CourierType.BIKE);

        assertThat(courier).isEqualTo(expected);
    }

    @Test
    void expectMapThrow() {
        CreateCourierDto createCourierDto =
                new CreateCourierDto(
                        CourierTypeEnum.BIKE,
                        List.of(1),
                        List.of("00:00-12:99", "16:00-21:00", "21:23-23:59")
                );

        assertThatThrownBy(() -> courierMapper.toCourier(createCourierDto)).isInstanceOf(DateTimeParseException.class);
    }
}