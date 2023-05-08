package ru.yandex.yandexlavka.model.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CreateOrderDto;
import ru.yandex.yandexlavka.model.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public List<Order> toListOrder(List<CreateOrderDto> orders) {
        return orders.stream().map(this::toOrder).collect(Collectors.toList());
    }

    public Order toOrder(CreateOrderDto orderDto) {
        Order order = new Order();

        order.setCost(orderDto.cost());
        order.setWeight(orderDto.weight());
        order.setRegion(orderDto.regions());

        List<OrderDeliveryHours> deliveryHours = new ArrayList<>(orderDto.deliveryHours().size());
        for (String hours : orderDto.deliveryHours()) {
            OrderDeliveryHours orderDeliveryHours = new OrderDeliveryHours();
            orderDeliveryHours.setStart(LocalTime.parse(hours.split("-")[0], formatter));
            orderDeliveryHours.setEnd(LocalTime.parse(hours.split("-")[1], formatter));
            deliveryHours.add(orderDeliveryHours);
        }
        order.setDeliveryHours(deliveryHours);

        return order;
    }
}
