package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AInputData(day = 5, year = 2023)
public class Day5 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        List<Long> needed = new ArrayList<>();
        Arrays.stream(lines[0].split(": ")[1].split(" ")).mapToLong(Long::parseLong).forEach(needed::add);

        ArrayList<Mapping> mappings = parseMappings(lines);

        long lowestLoc = Long.MAX_VALUE;
        for (long seed : needed) {
            long next = seed;
            for (Mapping mapping : mappings) {
                next = mapping.findConversion(next);
            }
            if(next < lowestLoc) lowestLoc = next;
        }

        return lowestLoc + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        List<Long> needed = new ArrayList<>();
        Arrays.stream(lines[0].split(": ")[1].split(" ")).mapToLong(Long::parseLong).forEach(needed::add);

        ArrayList<Mapping> mappings = parseMappings(lines);

        long lowestLoc = Long.MAX_VALUE;
        for (int i = 0; i < needed.size(); i+=2) {
            for(long seed = needed.get(i); seed < needed.get(i) + (needed.get(i + 1) -1); seed++) {
                long next = seed;
                for (Mapping mapping : mappings) {
                    next = mapping.findConversion(next);
                }
                if (next < lowestLoc) lowestLoc = next;
            }
        }

        return lowestLoc + "";
    }

    public ArrayList<Mapping> parseMappings(String[] lines) {
        ArrayList<Mapping> mappings = new ArrayList<>();
        mappings.add(new Mapping(new ArrayList<>()));
        for(int i = 3; i < lines.length; i++) {
            if(lines[i].isEmpty()) {
                i++;
                mappings.add(new Mapping(new ArrayList<>()));
            } else {
                long destination = Long.parseLong(lines[i].split("\s+")[0]);
                long source = Long.parseLong(lines[i].split("\s+")[1]);
                long range = Long.parseLong(lines[i].split("\s+")[2]);
                mappings.get(mappings.size() - 1).addConversion(new Conversion(destination, source, range));
            }
        }
        return mappings;
    }

    public record Conversion(long destination, long source, long range) {

        public boolean partOfLine(long target) {
            return target >= source && target <= source + (range - 1);
        }

        public long convert(long target) {
            long diff = target - source;
            return destination + diff;
        }

    }

    public record Mapping(List<Conversion> conversions) {

        public long findConversion(long target) {
            for(Conversion conv : conversions) {
                if(!conv.partOfLine(target)) continue;
                return conv.convert(target);
            }
            return target;
        }

        public void addConversion(Conversion conversion) {
            conversions.add(conversion);
        }

    }
}
