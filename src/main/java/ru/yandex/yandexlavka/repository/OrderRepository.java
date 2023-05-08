package ru.yandex.yandexlavka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
