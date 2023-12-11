
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.List;

@AInputData(day = 11, year = 2023)
public class Day11 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        Universe universe = new Universe(inputHelper);
        List<Cord> galaxies = universe.getGalaxies();

        long sumDist = 0L;
        for(int i=0; i<galaxies.size()-1; ++i) {
            for(int j=i+1; j<galaxies.size(); ++j) {
                Cord a = galaxies.get(i);
                Cord b = galaxies.get(j);
                sumDist += universe.pathFrom(a, b, 2);
            }
        }

        return sumDist + "";
    }
    
    @Override
    public String part2(IInputHelper inputHelper) {
        Universe universe = new Universe(inputHelper);
        List<Cord> galaxies = universe.getGalaxies();

        long sumDist = 0L;
        for(int i=0; i<galaxies.size()-1; ++i) {
            for(int j=i+1; j<galaxies.size(); ++j) {
                Cord a = galaxies.get(i);
                Cord b = galaxies.get(j);
                sumDist += universe.pathFrom(a, b, 1_000_000);
            }
        }

        return sumDist + "";
    }

    private record Cord(int x, int y) {}

    private static class Universe {

        List<List<Character>> expandable = new ArrayList<>();

        List<Integer> countInRow = new ArrayList<>();
        List<Integer> countInCol = new ArrayList<>();

        public Universe(IInputHelper inputHelper) {
            inputHelper.getInputAsStream().map(String::toCharArray)
                            .map(chars -> {
                                List<Character> lchs = new ArrayList<>();
                                for(var ch : chars) {
                                    lchs.add(ch);
                                }
                                return lchs;
                            }).forEach(expandable::add);
            for(int y=0; y<expandable.size(); ++y) {
                countInRow.add(countInRow(y));
            }
            for(int x=0; x < expandable.get(0).size(); ++x) {
                countInCol.add(countInColum(x));
            }
        }

        List<Cord> getGalaxies() {
            List<Cord> galaxies = new ArrayList<>();
            for(int y=0; y<expandable.size(); ++y) {
                for(int x=0; x<expandable.get(y).size(); ++x) {
                    if(expandable.get(y).get(x) == '#') {
                        galaxies.add(new Cord(x,y));
                    }
                }
            }
            return galaxies;
        }

        long pathFrom(Cord a, Cord b, int mul) {
            return walkRow(a.x, b.x, mul) + walkCol(a.y,b.y,mul);
        }

        private long walkRow(int a, int b, int mul) {
            return walkOn(countInCol, a, b, mul);
        }

        private long walkCol(int a, int b, int mul) {
            return walkOn(countInRow, a, b, mul);
        }

        private long walkOn(List<Integer> count, int a, int b, int mul) {
            int f = Math.min(a, b);
            int s = Math.max(a, b);
            long sum = 0L;
            for(int i=f; i<s; ++i) {
                if(count.get(i) == 0) {
                    sum += mul;
                } else {
                    sum += 1;
                }
            }
            return sum;
        }

        private int countInColum(int x) {
            return expandable.stream().map(l->l.get(x)).mapToInt(c->'#'==c?1:0).sum();
        }

        private int countInRow(int y) {
            return expandable.get(y).stream().mapToInt(c->'#'==c?1:0).sum();
        }

    }

}
