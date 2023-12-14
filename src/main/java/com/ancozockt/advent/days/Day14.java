
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.List;

@AInputData(day = 14, year = 2023)
public class Day14 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        Platform platform = parseInput(inputHelper);

        platform.shiftNorth();

        return platform.calculateWeightNorth() + "";
    }
    
    @Override
    public String part2(IInputHelper inputHelper) {
        Platform platform = parseInput(inputHelper);

        for(int c = 0; c < 100_000_000; c++) {
        }

        return platform.calculateWeightNorth() + "";
    }

    private Platform parseInput(IInputHelper inputHelper) {
        List<List<FieldType>> fields = new ArrayList<>();

        inputHelper.getInputAsStream().forEach(line -> {
            List<FieldType> row = new ArrayList<>();
            for(char c : line.toCharArray()) {
                if(c == 'O') {
                    row.add(FieldType.ROUNDED);
                } else if(c == '#') {
                    row.add(FieldType.CUBE);
                } else {
                    row.add(FieldType.EMPTY);
                }
            }
            fields.add(row);
        });

        return new Platform(fields);
    }

    public enum FieldType {
        CUBE,
        ROUNDED,
        EMPTY;
    }

    public record Platform(List<List<FieldType>> fields) {

        public void cycle() {
            shiftNorth();
            shiftWest();
            shiftSouth();
            shiftEast();
        }

        public void shiftNorth() {
            for(int y = 1; y < fields.size(); y++) {
                for(int x = 0; x < fields.get(y).size(); x++){
                    int fall = 1;
                    while (y - fall >= 0 && (fields.get(y - (fall - 1)).get(x).equals(FieldType.ROUNDED)
                            && fields.get(y - fall).get(x).equals(FieldType.EMPTY))) {
                        fields.get(y - fall).set(x, FieldType.ROUNDED);
                        fields.get(y - (fall - 1)).set(x, FieldType.EMPTY);
                        fall++;
                    }
                }
            }
        }

        public void shiftWest() {
            for (List<FieldType> field : fields) {
                for (int x = 0; x < field.size(); x++) {
                    int fall = 1;
                    while (x - fall >= 0 && (field.get(x - (fall - 1)).equals(FieldType.ROUNDED)
                            && field.get(x - fall).equals(FieldType.EMPTY))) {
                        field.set(x - fall, FieldType.ROUNDED);
                        field.set(x - (fall - 1), FieldType.EMPTY);
                        fall++;
                    }
                }
            }
        }

        public void shiftSouth() {
            for(int y = fields.size() - 2; y >= 0; y--) {
                for(int x = 0; x < fields.get(y).size(); x++){
                    int fall = 1;
                    while (y + fall < fields.size() && (fields.get(y + (fall - 1)).get(x).equals(FieldType.ROUNDED)
                            && fields.get(y + fall).get(x).equals(FieldType.EMPTY))) {
                        fields.get(y + fall).set(x, FieldType.ROUNDED);
                        fields.get(y + (fall - 1)).set(x, FieldType.EMPTY);
                        fall++;
                    }
                }
            }
        }

        public void shiftEast() {
            for (List<FieldType> field : fields) {
                for (int x = field.size() - 2; x >= 0; x--) {
                    int fall = 1;
                    while (x + fall < field.size() && (field.get(x + (fall - 1)).equals(FieldType.ROUNDED)
                            && field.get(x + fall).equals(FieldType.EMPTY))) {
                        field.set(x + fall, FieldType.ROUNDED);
                        field.set(x + (fall - 1), FieldType.EMPTY);
                        fall++;
                    }
                }
            }
        }

        public long calculateWeightNorth() {
            int rows = fields().size();

            long weight = 0;
            for(int y = 0; y < rows; y ++) {
                long cubes = fields().get(y).stream().filter(fieldType -> fieldType.equals(FieldType.ROUNDED)).count();
                weight += (rows - y) * cubes;
            }

            return weight;
        }

        public void print() {
            fields.forEach(rows -> {
                StringBuilder row = new StringBuilder();
                rows.forEach(fieldType -> {
                    if(fieldType.equals(FieldType.ROUNDED)) {
                        row.append("O");
                    } else if(fieldType.equals(FieldType.CUBE)) {
                        row.append("#");
                    } else {
                        row.append(".");
                    }
                });
                System.out.println(row.toString());
            });
        }

    }
}
