
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AInputData(day = 15, year = 2023)
public class Day15 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        String input = streamToString(inputHelper);

        AtomicLong atomicLong = new AtomicLong(0);
        Arrays.stream(input.split(",")).forEach(check -> {
            long current = 0;
            for (char ch : check.toCharArray()) {
                current += (int) ch;

                current *= 17;
                current = current % 256;
            }
            atomicLong.getAndAdd(current);
        });

        return atomicLong.get() + "";
    }
    
    @Override
    public String part2(IInputHelper inputHelper) {
        String input = streamToString(inputHelper);
        List<String> list = Arrays.stream(input.split(",")).toList();

        List<List<Operation>> boxes = calculateHashMap(list.stream().map(Operation::fromString).toList());
        int sum = calculateSum(boxes);

        return sum + "";
    }

    private String streamToString(IInputHelper inputHelper){
        StringBuilder builder = new StringBuilder();
        inputHelper.getInputAsStream().forEach(builder::append);
        return builder.toString();
    }

    private int calculateSum(List<List<Operation>> boxes) {
        int sum = 0;
        for (int i = 0; i < boxes.size(); ++i) {
            var box = boxes.get(i);
            for (int j = 0; j < box.size(); ++j) {
                var o = box.get(j);
                sum += (i + 1) * (j + 1) * o.focal();
            }
        }
        return sum;
    }

    private List<List<Operation>> calculateHashMap(List<Operation> ops) {
        List<List<Operation>> boxes = new ArrayList<>(256);
        for (int i = 0; i < 256; ++i) {
            boxes.add(new ArrayList<>());
        }
        ops.forEach(o -> doMap(o, indexOf(boxes.get(o.hash()), o), boxes.get(o.hash())));
        return boxes;
    }

    private static void doMap(Operation o, int i, List<Operation> box) {
        if (i >= 0) {
            if (o.add()) {
                box.set(i, o);
            } else {
                box.remove(i);
            }
        } else if (o.add()) {
            box.add(o);
        }
    }

    private int indexOf(List<Operation> box, Operation o) {
        for (int i = 0; i < box.size(); ++i) {
            if (o.label().equals(box.get(i).label())) {
                return i;
            }
        }
        return -1;
    }

    private static int hash(String str) {
        return str.chars().reduce(0, (a, b) -> ((a + b) * 17) % 256);
    }

    private record Operation(String label, int focal, int hash, boolean add) {
        static Operation fromString(String desc) {
            if (desc.endsWith("-")) {
                return new Operation(desc.substring(0, desc.length() - 1), Integer.MIN_VALUE, Day15.hash(desc.substring(0, desc.length()-1)), false);
            }
            var parts = desc.split("=");
            return new Operation(parts[0], Integer.parseInt(parts[1]), Day15.hash(parts[0]), true);
        }
    }
}
