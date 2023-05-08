package ru.yandex.yandexlavka.controller.handler;


import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.yandexlavka.dto.response.BadRequestResponse;
import ru.yandex.yandexlavka.dto.response.NotFoundResponse;
import ru.yandex.yandexlavka.service.exceptions.BadRequestException;
import ru.yandex.yandexlavka.service.exceptions.NotFoundException;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<NotFoundResponse> catchNotFound(NotFoundException ex) {
        log.warn("Not found exception : " + ex.getMessage());
        return new ResponseEntity<>(new NotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<BadRequestResponse> catchBadRequest(BadRequestException ex) {
        log.warn("Bad request : " + ex.getMessage());
        return new ResponseEntity<>(new BadRequestResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<BadRequestResponse> catchDateTimeParseException(DateTimeParseException ex) {
        log.warn("Bad request : " + ex.getMessage());
        return new ResponseEntity<>(new BadRequestResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<Object> handleRequestNotPermitted(RequestNotPermitted ex, HttpServletRequest request) {
        log.warn("Request to path '{}' is blocked due to rate-limiting. {}",
                request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
    }
}
