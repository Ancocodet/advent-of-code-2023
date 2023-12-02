package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AInputData(day = 1, year = 2023)
public class Day1 implements IAdventDay {

    private final Map<String, Integer> replaceMap = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5,
            "six", 6, "seven", 7, "eight", 8, "nine", 9);

    @Override
    public String part1(IInputHelper inputHelper) {
        AtomicInteger sum = new AtomicInteger();

        inputHelper.getInputAsStream().forEach(line -> {
            List<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    numbers.add(Integer.parseInt(String.valueOf(c)));
                }
            }
            sum.addAndGet(numbers.get(0) * 10 + numbers.get(numbers.size() - 1));
        });

        return sum.toString();
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        AtomicInteger sum = new AtomicInteger();

        inputHelper.getInputAsStream().forEach(line -> {
            List<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    numbers.add(Integer.parseInt(String.valueOf(c)));
                } else {
                    String sub = line.substring(i);
                    replaceMap.keySet().stream().filter(sub::startsWith).findAny().ifPresent(s -> {
                        numbers.add(replaceMap.get(s));
                    });
                }
            }
            sum.addAndGet(numbers.get(0) * 10 + numbers.get(numbers.size() - 1));
        });

        return sum.toString();
    }

}
