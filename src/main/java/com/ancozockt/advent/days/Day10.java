
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;
import java.util.stream.IntStream;

@AInputData(day = 10, year = 2023)
public class Day10 implements IAdventDay {

    private final char[] PIPES     = {'|', '-', 'L', 'J', '7', 'F'};
    private final int[] DIRECTIONS = {-1, 0, 0, 1, 1, 0, 0, -1};

    @Override
    public String part1(IInputHelper inputHelper) {
        List<String> lines = inputHelper.getInputAsStream().toList();

        Cord start = findStart(lines);
        List<String> map = replace(lines, start);
        Set<Cord> pipe = findPipe(start, map);

        return (pipe.size() / 2) + "";
    }
    
    @Override
    public String part2(IInputHelper inputHelper) {
        List<String> lines = inputHelper.getInputAsStream().toList();

        Cord start = findStart(lines);
        List<String> map = replace(lines, start);
        Set<Cord> pipe = findPipe(start, map);

        return countInternal(pipe, map).size() + "";
    }

    private Set<Cord> findPipe(Cord start, List<String> lines) {
        LinkedHashSet<Cord> visited = new LinkedHashSet<>();
        Queue<Cord> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        while(!queue.isEmpty()) {
            Cord top = queue.poll();
            List<Cord> possible = findTargets(pipeAt(top, lines), top).stream().filter(c->!visited.contains(c) && isValid(pipeAt(top, lines), top, c, lines)).toList();
            visited.addAll(possible);
            queue.addAll(possible);
        }
        return visited;
    }

    private Set<Cord> countInternal(Set<Cord> pipe, List<String> lines) {
        Set<Cord> internal = new HashSet<>();
        for(int y=0; y<lines.size(); ++y) {
            Set<Cord> found = new HashSet<>();
            String line = lines.get(y);
            boolean inside = false;
            char start = ' ';
            for(int x=0; x<line.length(); ++x) {
                char cAt = line.charAt(x);
                Cord cord = new Cord(x,y);
                if (pipe.contains(cord)) {
                    if (start == ' ') {
                        start = cAt;
                    }
                    if (cAt == '|') {
                        inside = !inside;
                        start = ' ';
                    } else if(cAt == 'J' && start == 'F') {
                        inside = !inside;
                        start = ' ';
                    } else if(cAt == 'J' && start == 'L') {
                        start = ' ';
                    } else if (cAt == '7' && start == 'L'){
                        inside = !inside;
                        start = ' ';
                    } else if (cAt == '7' && start == 'F'){
                        start = ' ';
                    }
                    if (!found.isEmpty()) {
                        internal.addAll(found);
                        found.clear();
                    }
                }else {
                    if (inside) {
                        found.add(cord);
                        start = ' ';
                    }
                }
            }
        }
        return internal;
    }

    private record Cord(int x, int y) {}

    private Cord findStart(List<String> lines) {
        Cord start = null;
        for(int y = 0; y < lines.size(); ++y) {
            int x = lines.get(y).indexOf('S');
            if(0<=x) {
                return new Cord(x, y);
            }
        }
        throw new IllegalStateException();
    }

    private List<String> replace(List<String> lines, Cord start) {
        char startCh =  List.of('|', '-', 'F', 'L', 'J', '7').stream().filter(ch->makesSense(lines, start, ch)).findAny().orElseThrow();
        return IntStream.range(0, lines.size()).mapToObj(y->{
            if(y!= start.y) {
                return lines.get(y);
            } else {
                char[] chs = lines.get(y).toCharArray();
                chs[start.x] = startCh;
                return new String(chs);
            }
        }).toList();
    }

    private boolean makesSense(List<String> lines, Cord start, Character ch) {
        char before = lines.get(start.y).charAt(start.x-1);
        char after = lines.get(start.y).charAt(start.x+1);
        char over = lines.get(start.y-1).charAt(start.x);
        char under = lines.get(start.y+1).charAt(start.x);
        return switch(ch) {
            case '-' -> (before == 'F' || before == '-' || before == 'L') && (after == '-' || after == 'J' || after == '7');
            case '|' -> (under == '|' || under == 'L' || under == 'J') && (over == '|' || over == '7' || over == 'F');
            case 'F' -> (under == '|' || under == 'L' || under == 'J') && (after == '-' || after == 'J' || after == '7');
            case 'L' -> (over == 'F' || over == '7' || over == '|') && (after == '-' || after == 'J' || after == '7');
            case 'J' -> (over == 'F' || over == '7' || over == '|') && (before == '-' || before == 'L' || before == 'F');
            case '7' -> (before == 'F' || before == '-' || before == 'L') && (under == 'L' || under == 'J' || under == '|');
            default -> throw new IllegalStateException();
        };
    }

    private List<Cord> findTargets(char at, Cord c) {
        return switch (at) {
            case '|' ->List.of(new Cord(c.x, c.y+1), new Cord(c.x, c.y-1));
            case '-' -> List.of(new Cord(c.x+1, c.y), new Cord(c.x-1, c.y));
            case 'L' -> List.of(new Cord(c.x, c.y-1), new Cord(c.x+1, c.y));
            case 'J' -> List.of(new Cord(c.x-1, c.y), new Cord(c.x, c.y-1));
            case '7' -> List.of(new Cord(c.x, c.y+1), new Cord(c.x-1, c.y));
            case 'F' -> List.of(new Cord(c.x+1, c.y), new Cord(c.x, c.y+1));
            default -> throw new IllegalArgumentException();
        };
    }

    private char pipeAt(Cord c, List<String> lines) {
        return lines.get(c.y).charAt(c.x);
    }

    private boolean isValid(char currT, Cord currC, Cord nextC, List<String> lines) {
        if(nextC.x<0 || lines.get(0).length() <= nextC.x || nextC.y <0 || lines.size() <= nextC.y) {
            return false;
        }
        char nextT = pipeAt(nextC, lines);
        return switch(currT) {
            case 'S' -> true;
            case '|' -> nextT == '|' || (nextC.y<currC.y && (nextT == 'F' || nextT == '7')) || (nextC.y>currC.y && (nextT == 'L' || nextT == 'J'));
            case '-' -> nextT == '-' || (nextC.x<currC.x && (nextT == 'L' || nextT == 'F')) || (nextC.x>currC.x && (nextT == '7' || nextT == 'J'));
            case 'L' -> (nextC.y<currC.y && (nextT == 'F' || nextT == '7' || nextT == '|')) || (nextC.x>currC.x && (nextT == '7' || nextT == 'J' || nextT=='-'));
            case 'J' -> (nextC.y<currC.y && (nextT == 'F' || nextT == '7' || nextT == '|')) || (nextC.x<currC.x && (nextT == 'L' || nextT == 'F' || nextT=='-'));
            case '7' -> (nextC.x<currC.x && (nextT == 'F' || nextT == 'L' || nextT == '-')) || (nextC.y>currC.y && (nextT == 'L' || nextT == 'J' || nextT=='|'));
            case 'F' -> (currC.x<nextC.x && (nextT == 'J' || nextT == '7' || nextT == '-')) || (nextC.y>currC.y && (nextT == 'L' || nextT == 'J' || nextT=='|'));
            default -> throw new IllegalStateException();
        };
    }
}
