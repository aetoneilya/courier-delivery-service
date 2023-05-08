package ru.yandex.yandexlavka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.yandexlavka.model.Courier;

import java.time.OffsetDateTime;
import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    @Query("select cost from Order where courier.id = :courierId " +
            "and completionTime >= :startDate and  completionTime < :endDate")
    List<Integer> getEarnedMoney(Long courierId, OffsetDateTime startDate, OffsetDateTime endDate);

}
