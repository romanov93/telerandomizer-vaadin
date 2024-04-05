package ru.romanov.telerandomizer.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileNameHelper {

    public static String formatFileName(String region, String city) {
        StringBuilder sb = new StringBuilder();
        sb.append("Телефоны");
        if (region != null)
            sb.append(", ").append(region);
        if (city != null && !city.equals("ВСЕ"))
            sb.append(", ").append(city);
        return sb.toString();
    }
}