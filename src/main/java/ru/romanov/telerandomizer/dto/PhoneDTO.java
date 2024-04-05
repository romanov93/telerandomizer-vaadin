package ru.romanov.telerandomizer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneDTO {

    private long phoneNumber;
    private String regionName;
    private String cityName;
    private String searchDate;
    private int numberType;
    private String status;
    private String comment;

}