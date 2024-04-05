package ru.romanov.telerandomizer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "range")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneRange {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "range_sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "range_sequence")
    private int id;

    @Column(name = "code")
    private int code;

    @Column(name = "first_phone")
    private int firstPhone;

    @Column(name = "last_phone")
    private int lastPhone;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @Column(name = "location_from_csv")
    private String locationFromCsv;

    @Column(name = "numbers_type")
    private int numbersType;

    @Column(name = "size")
    private int capacity;

}