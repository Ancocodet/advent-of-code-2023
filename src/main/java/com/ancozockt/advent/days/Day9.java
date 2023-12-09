package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AInputData(day = 9, year = 2023)
public class Day9 implements IAdventDay {
    @Override
    public String part1(IInputHelper inputHelper) {
        AtomicLong sum = new AtomicLong();

        inputHelper.getInputAsStream().forEach(line -> {
            Long[] numbers = Arrays.stream(line.split(" ")).map(Long::parseLong).toArray(Long[]::new);
            List<Long[]> differences = parseDiff(numbers);

            long finalValue = 0;
            for(int i = differences.size(); i > 0; i--) {
                Long[] diffs = differences.get(i - 1);
                finalValue += diffs[diffs.length - 1];
            }
            finalValue += numbers[numbers.length - 1];

            sum.getAndAdd(finalValue);
        });

        return sum.get() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        AtomicLong sum = new AtomicLong();

        inputHelper.getInputAsStream().forEach(line -> {
            Long[] numbers = Arrays.stream(line.split(" ")).map(Long::parseLong).toArray(Long[]::new);
            List<Long[]> differences = parseDiff(numbers);

            long finalValue = 0;
            for(int i = differences.size() - 1; i > 0; i--) {
                Long[] diffs = differences.get(i - 1);
                finalValue = (diffs[0] - finalValue);
            }
            finalValue = (numbers[0] - finalValue);

            sum.getAndAdd(finalValue);
        });

        return sum.get() + "";
    }

    private ArrayList<Long[]> parseDiff(Long[] numbers) {
        ArrayList<Long[]> diffs = new ArrayList<>();
        Long[] start = numbers;
        while(!isAllZero(start)) {
            Long[] diff = getDiffs(start);
            diffs.add(diff);
            start = diff;
        }
        return diffs;

    }

    private Long[] getDiffs(Long[] numbers) {
        Long[] diffs = new Long[numbers.length - 1];
        for(int i = 1; i < numbers.length; i++) {
            long diff = numbers[i] - numbers[i - 1];
            diffs[i - 1] = diff;
        }
        return diffs;
    }

    private boolean isAllZero(Long[] diffs) {
        for (Long diff : diffs) {
            if(diff != 0) return false;
        }
        return true;
    }
}
