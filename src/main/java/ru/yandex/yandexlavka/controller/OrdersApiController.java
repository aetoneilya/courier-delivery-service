package ru.yandex.yandexlavka.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.yandexlavka.controller.api.OrdersApi;
import ru.yandex.yandexlavka.dto.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.dto.request.CreateOrderRequest;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponse;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.service.OrdersService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrdersApiController implements OrdersApi {
    private final OrdersService ordersService;

    @Override
    @RateLimiter(name = "completeOrder-limiter")
    public ResponseEntity<List<OrderDto>> completeOrder(CompleteOrderRequestDto body) {
        log.info("Complete order request size = " + body.completeInfo().size());
        return new ResponseEntity<>(ordersService.completeOrders(body.completeInfo()), HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "createOrder-limiter")
    public ResponseEntity<List<OrderDto>> createOrder(CreateOrderRequest body) {
        log.info("Create order request size = " + body.orders().size());
        return new ResponseEntity<>(ordersService.createCouriers(body.orders()), HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "getOrder-limiter")
    public ResponseEntity<OrderDto> getOrder(Long orderId) {
        log.info("Get order request by id = " + orderId);
        return new ResponseEntity<>(ordersService.getOrdersById(orderId), HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "getOrders-limiter")
    public ResponseEntity<List<OrderDto>> getOrders(Integer limit, Integer offset) {
        log.info("Get orders request limit = " + limit + " offset = " + offset);
        if (limit == null) limit = 1;
        if (offset == null) offset = 0;
        return new ResponseEntity<>(ordersService.getOrders(limit, offset), HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "ordersAssign-limiter")
    public ResponseEntity<List<OrderAssignResponse>> ordersAssign(LocalDate date) {
        return null;
    }
}
