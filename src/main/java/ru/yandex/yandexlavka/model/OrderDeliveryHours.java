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
@Table(name = "orders_delivery_hours")
public class OrderDeliveryHours {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "delivery_start", nullable = false)
    private LocalTime start;

    @Column(name = "delivery_end", nullable = false)
    private LocalTime end;

    @ManyToOne
    private Order order;

    public String toFormatString() {
        return "%02d:%02d-%02d:%02d".formatted(start.getHour(), start.getMinute(), end.getHour(), end.getMinute());
    }
}
