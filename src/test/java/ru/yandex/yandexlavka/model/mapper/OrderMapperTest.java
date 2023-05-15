package ru.yandex.yandexlavka.model.mapper;

import org.junit.jupiter.api.Test;
import ru.yandex.yandexlavka.dto.CreateOrderDto;
import ru.yandex.yandexlavka.model.Order;
import ru.yandex.yandexlavka.model.OrderDeliveryHours;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderMapperTest {
    private final OrderMapper orderMapper = new OrderMapper();

    @Test
    void toListOrder() {
        CreateOrderDto dto1 = new CreateOrderDto(
                23F,
                1,
                List.of("11:00-12:00", "16:00-21:00"),
                100
        );
        CreateOrderDto dto2 = new CreateOrderDto(
                100F,
                2,
                List.of("12:00-12:00", "15:00-21:00"),
                1000
        );

        List<Order> orders = orderMapper.toListOrder(List.of(dto1, dto2));

        Order expected1 = new Order(null,
                List.of(new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                        new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                23F,
                100,
                1F,
                1,
                null,
                null
        );
        Order expected2 = new Order(null,
                List.of(new OrderDeliveryHours(null, LocalTime.of(12, 0), LocalTime.of(12, 0), null),
                        new OrderDeliveryHours(null, LocalTime.of(15, 0), LocalTime.of(21, 0), null)),
                100F,
                1000,
                1F,
                2,
                null,
                null
        );

        assertThat(orders).containsExactly(expected1, expected2);
    }

    @Test
    void toOrder() {
        CreateOrderDto createOrderDto = new CreateOrderDto(
                23F,
                1,
                List.of("11:00-12:00", "16:00-21:00"),
                100
        );

        Order order = orderMapper.toOrder(createOrderDto);

        Order expected = new Order(null,
                List.of(new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                        new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                23F,
                100,
                1F,
                1,
                null,
                null
        );
        assertThat(order).isEqualTo(expected);
    }

    @Test
    void toOrder2() {
        CreateOrderDto createOrderDto = new CreateOrderDto(
                25F,
                2,
                List.of("11:00-32:00", "16:00-21:00"),
                100
        );

        assertThatThrownBy(() -> orderMapper.toOrder(createOrderDto)).isInstanceOf(DateTimeParseException.class);
    }
}