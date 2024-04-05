package ru.romanov.telerandomizer.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneNumberUtils {

    public static long buildFullNumber(int code, int extensionNumber) {
        return  80000000000L + (code * 10000000L) + extensionNumber;
    }

    public static int getFirstDigitsOfNumber(int number, int howMany) {
        int x = 10000000;
        for (int i = 0 ; i < howMany ; i++)
            x /= 10;
        return (number - (number % x)) / x;
    }

    public static int getNumberWithoutRegionCode(long fullNumber) {
        return (int) (fullNumber % 10000000);
    }
}