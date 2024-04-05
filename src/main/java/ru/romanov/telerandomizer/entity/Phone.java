package ru.romanov.telerandomizer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "phone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone{

    public static final int NUMBER_TYPE_HOME = 1;
    public static final int NUMBER_TYPE_MOBILE = 2;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "phone_sequence", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_sequence")
    private long id;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "city_name")
    private String cityName;

    @ManyToOne
    @JoinColumn(name = "range_id", referencedColumnName = "id")
    private PhoneRange range;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "number")
    private long number;

    @Column(name = "status")
    private String status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "number_type")
    private int numberType;

    public int getNumberWithoutCode() {
        return (int) (number % 10000000);
    }

}