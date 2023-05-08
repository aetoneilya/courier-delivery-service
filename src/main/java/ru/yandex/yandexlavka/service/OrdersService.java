package ru.yandex.yandexlavka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.yandexlavka.dto.CreateOrderDto;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.dto.mapper.DtoMapper;
import ru.yandex.yandexlavka.dto.request.CompleteOrder;
import ru.yandex.yandexlavka.model.Courier;
import ru.yandex.yandexlavka.model.Order;
import ru.yandex.yandexlavka.model.mapper.OrderMapper;
import ru.yandex.yandexlavka.repository.CourierRepository;
import ru.yandex.yandexlavka.repository.OrderRepository;
import ru.yandex.yandexlavka.repository.requests.OffsetBasedPageRequest;
import ru.yandex.yandexlavka.service.exceptions.BadRequestException;
import ru.yandex.yandexlavka.service.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderMapper orderMapper;
    private final DtoMapper dtoMapper;

    @Transactional
    public List<OrderDto> createCouriers(List<CreateOrderDto> orders) {
        List<Order> orderList = orderRepository.saveAll(orderMapper.toListOrder(orders));
        return dtoMapper.toOrderDtoList(orderList);
    }

    @Transactional(readOnly = true)
    public OrderDto getOrdersById(Long id) {
        return dtoMapper.toOrderDto(
                orderRepository.findById(id)
                        .orElseThrow(NotFoundException::new));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(Integer limit, Integer offset) {
        return dtoMapper.toOrderDtoList(
                orderRepository.findAll(new OffsetBasedPageRequest(offset, limit, Sort.by("id"))).getContent());
    }

    @Transactional
    public List<OrderDto> completeOrders(List<CompleteOrder> completeOrders) {
        for (CompleteOrder completeOrder : completeOrders) {
            Order order = orderRepository.findById(completeOrder.orderId())
                    .orElseThrow(() -> new NotFoundException("Not found order with id = " + completeOrder.orderId()));
//            if (order.getCourier() != null && order.getCourier().getId().equals(completeOrder.courierId())) {
            if (order.getCourier() == null || order.getCourier().getId().equals(completeOrder.courierId())) {
                Courier courier = courierRepository.findById(completeOrder.courierId())
                        .orElseThrow(() -> new NotFoundException("Not found courier with id = " + completeOrder.courierId()));
                order.setCourier(courier);
                order.setCompletionTime(completeOrder.completeTime());
                orderRepository.save(order);
            } else {
                throw new BadRequestException();
            }
        }
        orderRepository.flush();
        return dtoMapper.toOrderDtoList(
                orderRepository.findAllById(completeOrders.stream().map(CompleteOrder::orderId).toList()));
    }
}
