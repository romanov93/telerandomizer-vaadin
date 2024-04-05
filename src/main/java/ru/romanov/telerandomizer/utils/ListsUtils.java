package ru.romanov.telerandomizer.utils;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class ListsUtils {

    public static List<String> findStringsDifference(List<String> oldList, List<String> newList) {
        Set<String> oldSet = new HashSet<>(oldList);
        return newList.stream().filter(row -> !oldSet.contains(row)).toList();
    }

    public static <T> void cutToSize(List<T> items, int requiredSize) {
        if (items.size() > requiredSize)
            items.subList(requiredSize, items.size()).clear();
    }
}