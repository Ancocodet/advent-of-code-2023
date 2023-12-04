package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AInputData(day = 4, year = 2023)
public class Day4 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        AtomicLong sum = new AtomicLong();

        inputHelper.getInputAsStream().forEach(line -> {
            String input = line.split(": ")[1];
            String[] cardInfo = input.split(" \\| ");

            List<Integer> winNumbers = new ArrayList<>();
            Arrays.stream(cardInfo[0].split(" "))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(value -> Integer.parseInt(value.replace(" ", "")))
                    .forEach(winNumbers::add);

            List<Integer> numbers = new ArrayList<>();
            Arrays.stream(cardInfo[1].split(" "))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(value -> Integer.parseInt(value.replace(" ", "")))
                    .forEach(numbers::add);

            int points = 0;
            for(int number : numbers) {
                if(winNumbers.contains(number)) {
                    if(points == 0) {
                        points = 1;
                    } else {
                        points *= 2;
                    }
                }
            }

            sum.addAndGet(points);
        });

        return sum.get() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        AtomicLong sum = new AtomicLong();
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        Integer[] multipliers = new Integer[lines.length];
        Arrays.fill(multipliers, 0);

        for(int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String input = line.split(": ")[1];
            String[] cardInfo = input.split(" \\| ");

            List<Integer> winNumbers = new ArrayList<>();
            Arrays.stream(cardInfo[0].split(" "))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(value -> Integer.parseInt(value.replace(" ", "")))
                    .forEach(winNumbers::add);

            List<Integer> numbers = new ArrayList<>();
            Arrays.stream(cardInfo[1].split(" "))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(value -> Integer.parseInt(value.replace(" ", "")))
                    .forEach(numbers::add);

            int cards = 0;
            for(int number : numbers) {
                if(winNumbers.contains(number)) {
                    cards++;
                }
            }

            if(cards > 0) {
                for (int c = 0; c < cards; c++) {
                    multipliers[i + (c + 1)] += 1 + multipliers[i];
                }
            }

            sum.addAndGet(1 + multipliers[i]);
        }

        return sum.get() + "";
    }

}
