package ru.yandex.yandexlavka.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "couriers")
public class Courier {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ElementCollection
    private Set<Integer> regions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "courier_id")
    private List<CourierWorkHours> workHours = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private CourierType type;

    public enum CourierType {FOOT, BIKE, AUTO}
}
