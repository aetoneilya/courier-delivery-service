package ru.yandex.yandexlavka.service.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
    }
}
