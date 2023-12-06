package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AInputData(day = 6, year = 2023)
public class Day6 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        long[] times = Arrays.stream(lines[0].split(": ")[1].split(" ")).filter(st -> ! st.isEmpty()).mapToLong(st -> Long.parseLong(st.replace(" ", ""))).toArray();
        long[] records = Arrays.stream(lines[1].split(": ")[1].split(" ")).filter(st -> ! st.isEmpty()).mapToLong(st -> Long.parseLong(st.replace(" ", ""))).toArray();

        List<Integer> possibilities = new ArrayList<>();
        for(int i = 0; i < times.length; i++) {
            long time = times[i];
            int wins = 0;
            for(long press = 0; press < time; press++){
                if(press * (time - press) > records[i]) {
                    wins++;
                }
            }
            possibilities.add(wins);
        }

        return possibilities.stream().reduce(1, (a, b) -> a * b) + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        String timeString = lines[0].split(": ")[1].replace(" ", "");
        String recordString = lines[1].split(": ")[1].replace(" ", "");

        long time = Long.parseLong(timeString);
        long record = Long.parseLong(recordString);

        AtomicLong wins = new AtomicLong();
        for(long press = 0; press < time; press++) {
            if(press * (time - press) > record) {
                wins.getAndIncrement();
            }
        }

        return wins.get() + "";
    }

}
