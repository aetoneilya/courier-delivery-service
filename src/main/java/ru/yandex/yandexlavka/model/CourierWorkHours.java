package ru.yandex.yandexlavka.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "couriers_work_hours")
public class CourierWorkHours {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "work_start", nullable = false)
    private LocalTime start;

    @Column(name = "work_end", nullable = false)
    private LocalTime end;

    public String toFormatString() {
        return "%d:%d-%d:%d".formatted(start.getHour(), start.getMinute(), end.getHour(), end.getMinute());
    }
}
