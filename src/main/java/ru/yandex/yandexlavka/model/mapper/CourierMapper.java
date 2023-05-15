package ru.yandex.yandexlavka.model.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CreateCourierDto;
import ru.yandex.yandexlavka.model.Courier;
import ru.yandex.yandexlavka.model.CourierWorkHours;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourierMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public List<Courier> toListCourier(List<CreateCourierDto> couriers) {
        return couriers.stream().map(this::toCourier).collect(Collectors.toList());
    }

    public Courier toCourier(CreateCourierDto courierDto) {
        Courier courier = new Courier();
        courier.setType(Courier.CourierType.valueOf(courierDto.courierType().toString()));
        courier.setRegions( new HashSet<>(courierDto.regions()));

        List<CourierWorkHours> workHours = new ArrayList<>(courierDto.workingHours().size());
        for (String workHour : courierDto.workingHours()) {
            CourierWorkHours courierWorkHours = new CourierWorkHours();
            courierWorkHours.setStart(LocalTime.parse(workHour.split("-")[0], formatter));
            courierWorkHours.setEnd(LocalTime.parse(workHour.split("-")[1], formatter));
            workHours.add(courierWorkHours);
        }
        courier.setWorkHours(workHours);
        return courier;
    }
}
