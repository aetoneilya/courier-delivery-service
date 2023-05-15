package ru.yandex.yandexlavka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.model.OrderDeliveryHours;

import java.util.List;

public interface OrderDeliveryHoursRepository extends JpaRepository<OrderDeliveryHours, Long> {
}
