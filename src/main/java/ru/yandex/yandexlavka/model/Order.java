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
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderDeliveryHours> deliveryHours;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(name = "cost_coefficient", nullable = false, columnDefinition = "float default 1")
    private Float costCoefficient = 1f;

    @Column(name = "region_id", nullable = false)
    private Integer region;

    @ManyToOne
    @JoinColumn(name = "orders")
    private Assignment assignment;

    @Column(name = "completion_time")
    private OffsetDateTime completionTime;
}
