package ru.yandex.yandexlavka.dto.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.dto.response.CouriersGroupOrders;
import ru.yandex.yandexlavka.dto.response.GroupOrders;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponse;
import ru.yandex.yandexlavka.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                new ArrayList<>(courier.getRegions()),
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

    public OrderAssignResponse toOrderAssignResponse(List<Assignment> assignments) {

        Map<Long, List<GroupOrders>> couriersToGroup = new HashMap<>();
        for (Assignment assignment : assignments) {
            if (!couriersToGroup.containsKey(assignment.getCourier().getId())) {
                couriersToGroup.put(assignment.getCourier().getId(), new ArrayList<>());
            }
            couriersToGroup.get(assignment.getCourier().getId()).add(new GroupOrders(assignment.getId(), toOrderDtoList(assignment.getOrders())));
        }

        List<CouriersGroupOrders> couriersGroupOrdersList = new ArrayList<>();
        for (Map.Entry<Long, List<GroupOrders>> entry : couriersToGroup.entrySet()) {
            couriersGroupOrdersList.add(new CouriersGroupOrders(entry.getKey(), entry.getValue()));
        }

        return new OrderAssignResponse(null, couriersGroupOrdersList);
    }


}
