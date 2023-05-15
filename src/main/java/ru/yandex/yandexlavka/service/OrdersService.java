package ru.yandex.yandexlavka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.yandexlavka.configuration.ApplicationConfig;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;
import ru.yandex.yandexlavka.dto.CreateOrderDto;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.dto.mapper.DtoMapper;
import ru.yandex.yandexlavka.dto.request.CompleteOrder;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponse;
import ru.yandex.yandexlavka.model.*;
import ru.yandex.yandexlavka.model.mapper.OrderMapper;
import ru.yandex.yandexlavka.repository.AssignmentsRepository;
import ru.yandex.yandexlavka.repository.CourierRepository;
import ru.yandex.yandexlavka.repository.OrderDeliveryHoursRepository;
import ru.yandex.yandexlavka.repository.OrderRepository;
import ru.yandex.yandexlavka.repository.requests.OffsetBasedPageRequest;
import ru.yandex.yandexlavka.service.exceptions.BadRequestException;
import ru.yandex.yandexlavka.service.exceptions.NotFoundException;

import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final AssignmentsRepository assignmentsRepository;
    private final OrderDeliveryHoursRepository orderDeliveryHoursRepository;
    private final OrderMapper orderMapper;
    private final DtoMapper dtoMapper;
    private final ApplicationConfig config;

    @Transactional
    public List<OrderDto> createOrders(List<CreateOrderDto> orders) {
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
        List<Order> completedOrders = new LinkedList<>();
        for (CompleteOrder completeOrder : completeOrders) {
            Order order = orderRepository.findById(completeOrder.orderId())
                    .orElseThrow(() -> new BadRequestException("Not found order with id = " + completeOrder.orderId()));
            if (!(order.getAssignment() == null) && order.getAssignment().getCourier().getId().equals(completeOrder.courierId())) {
                order.setCompletionTime(completeOrder.completeTime());
                completedOrders.add(order);
            } else {
                throw new BadRequestException("Order wasn't assigned or was assigned to other courier");
            }
        }

        return dtoMapper.toOrderDtoList(orderRepository.saveAll(completedOrders));
    }

    @Transactional
    public List<OrderAssignResponse> ordersAssign() {
        List<OrderDeliveryHours> deliveryHours = orderDeliveryHoursRepository.findAll();

        List<Assignment> assignments = new LinkedList<>();
        for (Courier courier : courierRepository.findAll()) {
            assignments.addAll(assignOrdersToCourier(courier, deliveryHours));
        }

        return Collections.singletonList(dtoMapper.toOrderAssignResponse(assignmentsRepository.saveAll(assignments)));
    }

    private List<Assignment> assignOrdersToCourier(Courier courier, List<OrderDeliveryHours> deliveryHours) {
        CourierTypeEnum courierType = CourierTypeEnum.valueOf(courier.getType().toString());
        int maxWeight = config.maxWeight().get(courierType);
        int maxOrderAmount = config.maxOrderAmount().get(courierType);
        int regionsOnDelivery = config.regionsOnDelivery().get(courierType);
        int timeOnFirstOrder = config.timeOnFirstOrder().get(courierType);
        int timeOnNextOrder = config.timeOnNextOrder().get(courierType);
        float costCoefNextOrder = config.costCoefNextOrder();

        List<Assignment> assignments = new ArrayList<>();

        for (CourierWorkHours courierWorkHours : courier.getWorkHours()) {
            LocalTime deliveryTime = courierWorkHours.getStart();
            deliveryTime = deliveryTime.plusMinutes(timeOnFirstOrder);

            Assignment assignment = new Assignment();
            Set<Integer> chosenRegions = new HashSet<>();
            int currentWeight = 0;

            // первый заказ в группе надо искать как самый первый подходящий в множестве
            // остальные заказы пытаться сгруппировать с этим
            while (!deliveryTime.isAfter(courierWorkHours.getEnd())) {
                Optional<Order> appropriateOrder;

                if (assignment.getOrders().isEmpty()) {
                    //choose next appropriate order
                    Optional<OrderDeliveryHours> appropriateDeliveryTime = findAppropriateDeliveryTime(deliveryHours, deliveryTime, courierWorkHours.getEnd(),
                            maxWeight - currentWeight,
                            chosenRegions.size() >= regionsOnDelivery ? chosenRegions : courier.getRegions());
                    if (appropriateDeliveryTime.isEmpty()) {
                        break;
                    }
                    appropriateOrder = appropriateDeliveryTime.map(OrderDeliveryHours::getOrder);
                    if (appropriateDeliveryTime.get().getStart().isAfter(deliveryTime)) {
                        deliveryTime = appropriateDeliveryTime.get().getStart();
                    }
                } else if (assignment.getOrders().size() < maxOrderAmount) {
                    // find order by delivery time
                    appropriateOrder = findAppropriateOrder(deliveryHours, deliveryTime,
                            maxWeight - currentWeight,
                            chosenRegions.size() >= regionsOnDelivery ? chosenRegions : courier.getRegions());
                } else {
                    appropriateOrder = Optional.empty();
                }

                if (appropriateOrder.isPresent()) {
                    Order order = appropriateOrder.get();
                    order.setAssignment(assignment);
                    order.setCostCoefficient(assignment.getOrders().isEmpty() ? 1 : costCoefNextOrder);
                    deliveryTime = deliveryTime.plusMinutes(assignment.getOrders().isEmpty() ? timeOnFirstOrder : timeOnNextOrder);
                    chosenRegions.add(order.getRegion());
                    assignment.getOrders().add(order);
                    currentWeight += order.getWeight();
                } else {
                    chosenRegions.clear();
                    if (!assignment.getOrders().isEmpty()) {
                        assignment.setCourier(courier);
                        assignments.add(assignment);
                    }
                    currentWeight = 0;
                    assignment = new Assignment();
                }
            }
        }

        return assignments;
    }

    private Optional<Order> findAppropriateOrder(List<OrderDeliveryHours> deliveryHours, LocalTime time, int weight, Set<Integer> regions) {
        for (OrderDeliveryHours deliveryTime : deliveryHours) {
            Order order = deliveryTime.getOrder();
            if (!deliveryTime.getStart().isAfter(time) && !deliveryTime.getEnd().isBefore(time)
                    && order.getWeight() <= weight && regions.contains(order.getRegion()) && order.getAssignment() == null) {
                return Optional.of(order);
            }
        }

        return Optional.empty();
    }

    //find orders with delivery hours in diapason of delivery time
    private Optional<OrderDeliveryHours> findAppropriateDeliveryTime(List<OrderDeliveryHours> deliveryHours, LocalTime from, LocalTime to, int weight, Set<Integer> regions) {
//        !(A1 > B2 || A2 < B1 )
        for (OrderDeliveryHours deliveryTime : deliveryHours) {
            Order order = deliveryTime.getOrder();
            if (!(deliveryTime.getStart().isAfter(to) || deliveryTime.getEnd().isBefore(from))
                    && order.getWeight() <= weight && regions.contains(order.getRegion()) && order.getAssignment() == null) {
                return Optional.of(deliveryTime);
            }
        }

        return Optional.empty();
    }
}
