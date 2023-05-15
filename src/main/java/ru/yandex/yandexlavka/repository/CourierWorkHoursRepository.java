package ru.yandex.yandexlavka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.model.CourierWorkHours;

import java.util.List;

public interface CourierWorkHoursRepository extends JpaRepository<CourierWorkHours, Long> {
    List<CourierWorkHours> findAllByOrderByStartAsc();
}
