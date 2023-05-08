package ru.yandex.yandexlavka.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    List<OrderDeliveryHours> deliveryHours;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(name = "region_id", nullable = false)
    private Integer region;

    @ManyToOne
    private Courier courier;

    @Column(name = "completion_time")
    private OffsetDateTime completionTime;
}
