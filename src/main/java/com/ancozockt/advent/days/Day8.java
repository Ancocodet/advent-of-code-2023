package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@AInputData(day = 8, year = 2023)
public class Day8 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        String lookUps = lines[0].replace("R", "1").replace("L", "0");
        HashMap<String, String[]> calculations = new HashMap<>();
        for(int i = 2; i < lines.length; i++) {
            String line = lines[i];

            String start = line.split(" = ")[0];
            String[] end = line.split(" = ")[1].replace("(", "").replace(")", "").split(", ");

            calculations.put(start, end);
        }

        return pathLength(lookUps, calculations, "AAA") + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        String lookUps = lines[0].replace("R", "1").replace("L", "0");
        HashMap<String, String[]> calculations = new HashMap<>();
        for(int i = 2; i < lines.length; i++) {
            String line = lines[i];

            String start = line.split(" = ")[0];
            String[] end = line.split(" = ")[1].replace("(", "").replace(")", "").split(", ");

            calculations.put(start, end);
        }

        List<String> starts = calculations.keySet().stream().filter(s -> s.endsWith("A")).toList();
        List<Long> steps = starts.stream().map(value -> pathLength(lookUps, calculations, value)).toList();

        return steps.stream().reduce(1L, this::leastCommonMultiplier) + "";
    }

    private long leastCommonMultiplier(long a, long b) {
        return (a * b) / greatestCommonDenominator(a, b);
    }

    private long greatestCommonDenominator(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return (greatestCommonDenominator(b, a % b));
        }
    }

    private long pathLength(String instructions, HashMap<String, String[]> calculations, String start) {
        long steps = 0;
        int index = 0;
        String current = start;
        while (!current.endsWith("Z")) {
            steps++;
            current = calculations.get(current)[Character.getNumericValue(instructions.charAt(index))];
            index = (index + 1) % instructions.length();
        }

        return steps;
    }
}
