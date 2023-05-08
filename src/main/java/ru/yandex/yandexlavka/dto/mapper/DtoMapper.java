package ru.yandex.yandexlavka.dto.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;
import ru.yandex.yandexlavka.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {
    public List<CourierDto> toCourierDtoList(List<Courier> courierList) {
        return courierList.stream()
                .map(this::toCourierDto)
                .collect(Collectors.toList());
    }

    public CourierDto toCourierDto(Courier courier) {
        return new CourierDto(courier.getId(),
                CourierTypeEnum.valueOf(courier.getType().toString()),
                courier.getRegionList().stream().map(CourierRegion::getRegionId).collect(Collectors.toList()),
                courier.getWorkHours().stream().map(CourierWorkHours::toFormatString).collect(Collectors.toList()));
    }

    public List<OrderDto> toOrderDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDto toOrderDto(Order order) {
        return new OrderDto(order.getId(),
                order.getWeight(),
                order.getRegion(),
                order.getDeliveryHours().stream().map(OrderDeliveryHours::toFormatString).collect(Collectors.toList()),
                order.getCost(),
                order.getCompletionTime());
    }
}
