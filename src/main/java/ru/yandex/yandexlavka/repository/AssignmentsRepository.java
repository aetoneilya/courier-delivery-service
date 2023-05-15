package ru.yandex.yandexlavka.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.model.Assignment;

import java.util.List;

public interface AssignmentsRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByCourierId(Long courierId);
}
