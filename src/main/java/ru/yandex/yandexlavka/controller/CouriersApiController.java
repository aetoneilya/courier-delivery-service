package ru.yandex.yandexlavka.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.yandexlavka.controller.api.CouriersApi;
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.dto.request.CreateCourierRequest;
import ru.yandex.yandexlavka.dto.response.*;
import ru.yandex.yandexlavka.service.CouriersService;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CouriersApiController implements CouriersApi {
    private final CouriersService couriersService;


    @Override
    @RateLimiter(name = "couriersAssignments-limiter")
    public ResponseEntity<OrderAssignResponse> couriersAssignments(LocalDate date, Integer courierId) {
        return null;
    }

    @Override
    @RateLimiter(name = "createCourier-limiter")
    public ResponseEntity<CreateCouriersResponse> createCourier(CreateCourierRequest body) {
        log.info("Create courier request size = " + body.couriers().size());
        CreateCouriersResponse response = new CreateCouriersResponse(couriersService.createCouriers(body.couriers()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "getCourierById-limiter")
    public ResponseEntity<CourierDto> getCourierById(Long courierId) {
        log.info("Get courier request by id = " + courierId);
        return new ResponseEntity<>(couriersService.getCourierById(courierId), HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "getCourierMetaInfo-limiter")
    public ResponseEntity<GetCourierMetaInfoResponse> getCourierMetaInfo(Long courierId, LocalDate startDate, LocalDate endDate) {
        return new ResponseEntity<>(couriersService.getCourierMetaInfo(courierId, startDate, endDate), HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "getCouriers-limiter")
    public ResponseEntity<GetCouriersResponse> getCouriers(Integer limit, Integer offset) {
        log.info("Get couriers request limit = " + limit + " offset = " + offset);
        if (limit == null) limit = 1;
        if (offset == null) offset = 0;
        GetCouriersResponse response = new GetCouriersResponse(couriersService.getCouriers(limit, offset), limit, offset);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
