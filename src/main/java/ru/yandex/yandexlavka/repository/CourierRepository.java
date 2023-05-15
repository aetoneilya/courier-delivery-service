package ru.yandex.yandexlavka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.yandexlavka.model.Courier;

import java.time.OffsetDateTime;
import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    @Query("select  o.cost * o.costCoefficient from Order o join Assignment a where a.courier.id = :courierId " +
            "and o.completionTime >= :startDate and o.completionTime < :endDate")
    List<Integer> getEarnedMoney(Long courierId, OffsetDateTime startDate, OffsetDateTime endDate);

}
