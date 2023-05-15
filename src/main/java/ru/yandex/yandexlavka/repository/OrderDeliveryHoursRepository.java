package ru.yandex.yandexlavka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.model.OrderDeliveryHours;

public interface OrderDeliveryHoursRepository extends JpaRepository<OrderDeliveryHours, Long> {
}
