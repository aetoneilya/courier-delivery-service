package ru.yandex.yandexlavka.dto.mapper;

import org.junit.jupiter.api.Test;
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.dto.response.CouriersGroupOrders;
import ru.yandex.yandexlavka.dto.response.GroupOrders;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponse;
import ru.yandex.yandexlavka.model.*;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DtoMapperTest {

    private final DtoMapper dtoMapper = new DtoMapper();

    @Test
    void toCourierDtoList() {
        //given
        Courier courier1 = new Courier(null,
                Set.of(1),
                List.of(new CourierWorkHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0))),
                Courier.CourierType.FOOT);
        Courier courier2 = new Courier(null,
                Set.of(2),
                List.of(new CourierWorkHours(null, LocalTime.of(0, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0)),
                        new CourierWorkHours(null, LocalTime.of(21, 23), LocalTime.of(23, 59))),
                Courier.CourierType.BIKE);
        Courier courier3 = new Courier(null,
                Set.of(3),
                List.of(new CourierWorkHours(null, LocalTime.of(0, 0), LocalTime.of(23, 59))),
                Courier.CourierType.AUTO);

        //when
        List<CourierDto> courierDtos = dtoMapper.toCourierDtoList(List.of(courier1, courier2, courier3));

        // then
        CourierDto dto1 =
                new CourierDto(
                        null,
                        CourierTypeEnum.FOOT,
                        List.of(1),
                        List.of("11:00-12:00", "16:00-21:00")
                );
        CourierDto dto2 =
                new CourierDto(
                        null,
                        CourierTypeEnum.BIKE,
                        List.of(2),
                        List.of("00:00-12:00", "16:00-21:00", "21:23-23:59")
                );
        CourierDto dto3 =
                new CourierDto(
                        null,
                        CourierTypeEnum.AUTO,
                        List.of(3),
                        List.of("00:00-23:59")
                );

        assertThat(courierDtos).containsExactly(dto1, dto2, dto3);
    }

    @Test
    void toCourierDto() {
        Courier courier = new Courier(null,
                Set.of(1),
                List.of(new CourierWorkHours(null, LocalTime.of(0, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0)),
                        new CourierWorkHours(null, LocalTime.of(21, 23), LocalTime.of(23, 59))),
                Courier.CourierType.BIKE);

        CourierDto courierDto = dtoMapper.toCourierDto(courier);

        CourierDto expected =
                new CourierDto(
                        null,
                        CourierTypeEnum.BIKE,
                        List.of(1),
                        List.of("00:00-12:00", "16:00-21:00", "21:23-23:59")
                );

        assertThat(courierDto).isEqualTo(expected);
    }

    @Test
    void toCourierDto2() {
        Courier courier = new Courier(null,
                Set.of(1),
                List.of(new CourierWorkHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0))),
                Courier.CourierType.FOOT);

        CourierDto courierDto = dtoMapper.toCourierDto(courier);

        CourierDto expected =
                new CourierDto(
                        null,
                        CourierTypeEnum.FOOT,
                        List.of(1),
                        List.of("11:00-12:00", "16:00-21:00")
                );

        assertThat(courierDto).isEqualTo(expected);
    }

    @Test
    void toOrderDtoList() {
        Order order1 = new Order(
                null,
                List.of(new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                        new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                23F,
                100,
                1F,
                1,
                null,
                null
        );
        Order order2 = new Order(
                24L,
                List.of(new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                        new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                23F,
                100,
                1F,
                1,
                null,
                OffsetDateTime.MAX
        );

        List<OrderDto> orderDtos = dtoMapper.toOrderDtoList(List.of(order1, order2));

        OrderDto expected1 = new OrderDto(
                null,
                23F,
                1,
                List.of("11:00-12:00", "16:00-21:00"),
                100,
                null
        );
        OrderDto expected2 = new OrderDto(
                24L,
                23F,
                1,
                List.of("11:00-12:00", "16:00-21:00"),
                100,
                OffsetDateTime.MAX
        );

        assertThat(orderDtos).containsExactly(expected1, expected2);
    }

    @Test
    void toOrderDto() {
        Order order = new Order(
                null,
                List.of(new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                        new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                23F,
                100,
                1F,
                1,
                null,
                null
        );

        OrderDto orderDto = dtoMapper.toOrderDto(order);

        OrderDto expected = new OrderDto(
                null,
                23F,
                1,
                List.of("11:00-12:00", "16:00-21:00"),
                100,
                null
        );

        assertThat(orderDto).isEqualTo(expected);
    }

    @Test
    void toOrderDto2() {
        Order order = new Order(
                24L,
                List.of(new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                        new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                23F,
                100,
                1F,
                1,
                null,
                OffsetDateTime.MAX
        );

        OrderDto orderDto = dtoMapper.toOrderDto(order);

        OrderDto expected = new OrderDto(
                24L,
                23F,
                1,
                List.of("11:00-12:00", "16:00-21:00"),
                100,
                OffsetDateTime.MAX
        );

        assertThat(orderDto).isEqualTo(expected);
    }

    @Test
    void toOrderAssignResponse() {
        Courier courier1 = new Courier(1L,
                Set.of(1),
                List.of(new CourierWorkHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0))),
                Courier.CourierType.FOOT);
        Courier courier2 = new Courier(2L,
                Set.of(2),
                List.of(new CourierWorkHours(null, LocalTime.of(0, 0), LocalTime.of(12, 0)),
                        new CourierWorkHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0)),
                        new CourierWorkHours(null, LocalTime.of(21, 23), LocalTime.of(23, 59))),
                Courier.CourierType.BIKE);

        Assignment assignment1 = new Assignment(
                1L,
                courier1,
                List.of(new Order(24L, List.of(
                                new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                                new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                                23F, 100, 1F, 1, null, null),
                        new Order(21L, List.of(
                                new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                                new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                                23F, 100, 1F, 1, null, null)
                )
        );
        Assignment assignment2 = new Assignment(
                2L,
                courier2,
                List.of(new Order(25L, List.of(
                                new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                                new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                                23F, 100, 1F, 1, null, null),
                        new Order(26L, List.of(
                                new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                                new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                                23F, 100, 0.8F, 1, null, null)
                )
        );
        Assignment assignment3 = new Assignment(
                3L,
                courier2,
                List.of(new Order(27L, List.of(
                                new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                                new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                                23F, 100, 1F, 1, null, null),
                        new Order(28L, List.of(
                                new OrderDeliveryHours(null, LocalTime.of(11, 0), LocalTime.of(12, 0), null),
                                new OrderDeliveryHours(null, LocalTime.of(16, 0), LocalTime.of(21, 0), null)),
                                23F, 100, 0.8F, 1, null, null)
                )
        );

        OrderAssignResponse response = dtoMapper.toOrderAssignResponse(List.of(assignment1, assignment2, assignment3));


        OrderDto orderDto1 = new OrderDto(24L, 23F, 1,
                List.of("11:00-12:00", "16:00-21:00"), 100, null);
        OrderDto orderDto2 = new OrderDto(21L, 23F, 1,
                List.of("11:00-12:00", "16:00-21:00"), 100, null);
        OrderDto orderDto3 = new OrderDto(25L, 23F, 1,
                List.of("11:00-12:00", "16:00-21:00"), 100, null);
        OrderDto orderDto4 = new OrderDto(26L, 23F, 1,
                List.of("11:00-12:00", "16:00-21:00"), 100, null);
        OrderDto orderDto5 = new OrderDto(27L, 23F, 1,
                List.of("11:00-12:00", "16:00-21:00"), 100, null);
        OrderDto orderDto6 = new OrderDto(28L, 23F, 1,
                List.of("11:00-12:00", "16:00-21:00"), 100, null);


        OrderAssignResponse expected = new OrderAssignResponse(null,
                List.of(new CouriersGroupOrders(1L, List.of(new GroupOrders(1L, List.of(orderDto1, orderDto2)))),
                        new CouriersGroupOrders(2L, List.of(new GroupOrders(2L, List.of(orderDto3, orderDto4)),
                                new GroupOrders(3L, List.of(orderDto5, orderDto6))))));
        assertThat(response).isEqualTo(expected);
    }
}