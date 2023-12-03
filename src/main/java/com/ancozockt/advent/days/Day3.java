package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AInputData(day = 3, year = 2023)
public class Day3 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        List<String> lines = inputHelper.getInputAsStream().toList();
        Set<Cord> cords = new HashSet<>();

        for(int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for(int x = 0; x < line.length(); x++){
                Cord cord = new Cord(x, y);
                char c = line.charAt(x);
                if(!Character.isDigit(c) && c != '.'){
                    cord.neighbors().stream()
                            .filter(cord1 -> cord1.isValid(lines) && Character.isDigit(lines.get(cord1.y).charAt(cord1.x)))
                            .map(cord1 -> findStart(lines, cord1)).forEach(cords::add);
                }
            }
        }

        return cords.stream().mapToInt(cord -> readNumber(lines, cord)).sum() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        List<String> lines = inputHelper.getInputAsStream().toList();
        AtomicInteger sum = new AtomicInteger();

        for(int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                Cord cord = new Cord(x, y);
                char chr = line.charAt(x);
                if(chr == '*') {
                    Set<Cord> cords = cord.neighbors().stream()
                            .filter(c -> c.isValid(lines) && Character.isDigit(lines.get(c.y).charAt(c.x)))
                            .map(c -> findStart(lines, c)).collect(Collectors.toSet());
                    if(cords.size() == 2){
                        sum.addAndGet(cords.stream().mapToInt(c -> readNumber(lines, c)).reduce(1, (a, b) -> a * b));
                    }
                }
            }
        }

        return sum.get() + "";
    }

    private Cord findStart(List<String> lines, Cord cord) {
        String line = lines.get(cord.y);
        int x = cord.x;
        while(x > 0 && Character.isDigit(line.charAt(x - 1))){
            x--;
        }
        return new Cord(x, cord.y);
    }

    private int readNumber(List<String> lines, Cord cord){
        String line = lines.get(cord.y);
        int x = cord.x;
        while(x < line.length() && Character.isDigit(line.charAt(x))){
            x++;
        }
        return Integer.parseInt(line.substring(cord.x, x));
    }

    public record Cord(int x, int y) {

        List<Cord> neighbors() {
            return List.of(
                    new Cord(x + 1, y),
                    new Cord(x - 1, y),
                    new Cord(x, y + 1),
                    new Cord(x, y - 1),
                    new Cord(x + 1, y + 1),
                    new Cord(x - 1, y - 1),
                    new Cord(x + 1, y - 1),
                    new Cord(x - 1, y + 1)
            );
        }

        boolean isValid(List<String> lines) {
            return x >= 0 && y >= 0 && y < lines.size() && x < lines.get(y).length();
        }

    }
}
