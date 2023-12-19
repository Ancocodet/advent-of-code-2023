
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@AInputData(day = 16, year = 2023)
public class Day16 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        List<String> lines = inputHelper.getInputAsStream().toList();

        return countEnergized(lines, new Beam(new Cord(0,0), Direction.RIGHT)) + "";
    }
    
    @Override
    public String part2(IInputHelper inputHelper) {
        List<String> lines = inputHelper.getInputAsStream().toList();
        List<Beam> starts = collectStartBeams(lines.size(), lines.get(0).length());
        return starts.stream().parallel().mapToLong(start -> countEnergized(lines, start)).max().orElseThrow() + "";
    }

    private List<Beam> collectStartBeams(int height, int width) {
        return Stream.concat(IntStream.range(0, height).mapToObj(y -> List.of(
                                new Beam(new Cord(0, y), Direction.RIGHT),
                                new Beam(new Cord(width - 1, y), Direction.LEFT))),
                        IntStream.range(0, width).mapToObj(x -> List.of(
                                new Beam(new Cord(x, 0), Direction.DOWN),
                                new Beam(new Cord(x, height - 1), Direction.UP))))
                .flatMap(List::stream).toList();
    }

    private long countEnergized(List<String> tiles, Beam start) {
        int height = tiles.size();
        int width = tiles.get(0).length();
        Predicate<Beam> isValid = beam -> 0<= beam.position().x && beam.position().x < width && 0<= beam.position().y() && beam.position().y() < height;
        List<Beam> beams = new ArrayList<>();
        beams.add(start);
        Set<Beam> visited = new HashSet<>();
        while(beams.stream().anyMatch(not(visited::contains))) {
            beams = beams.stream().filter(not(visited::contains)).peek(visited::add).map(beam->{
                char tile = tiles.get(beam.position().y()).charAt(beam.position.x());
                return switch (tile) {
                    case '.' -> List.of(beam.step());
                    case '|' -> verticalSplit(beam);
                    case '-' -> horizontalSplit(beam);
                    case '/' -> rightTurn(beam);
                    case '\\' -> leftTurn(beam);
                    default -> throw new IllegalStateException("Unexpected value: " + tile);
                };
            }).flatMap(List::stream).filter(isValid).toList();
        }
        return visited.stream().map(Beam::position).distinct().count();
    }

    private List<Beam> verticalSplit(Beam beam) {
        return switch (beam.direction) {
            case LEFT, RIGHT -> List.of(new Beam(beam.position(), Direction.UP).step(), new Beam(beam.position(), Direction.DOWN).step());
            case UP, DOWN -> List.of(beam.step());
        };
    }

    private List<Beam> horizontalSplit(Beam beam) {
        return switch (beam.direction) {
            case UP, DOWN -> List.of(new Beam(beam.position(), Direction.LEFT).step(), new Beam(beam.position(), Direction.RIGHT).step());
            case LEFT, RIGHT -> List.of(beam.step());
        };
    }

    private List<Beam> rightTurn(Beam beam) {
        return switch (beam.direction) {
            case LEFT -> List.of(new Beam(beam.position(), Direction.DOWN).step());
            case RIGHT -> List.of(new Beam(beam.position(), Direction.UP).step());
            case UP -> List.of(new Beam(beam.position(), Direction.RIGHT).step());
            case DOWN -> List.of(new Beam(beam.position(), Direction.LEFT).step());
        };
    }

    private List<Beam> leftTurn(Beam beam) {
        return switch (beam.direction) {
            case LEFT -> List.of(new Beam(beam.position(), Direction.UP).step());
            case RIGHT -> List.of(new Beam(beam.position(), Direction.DOWN).step());
            case UP -> List.of(new Beam(beam.position(), Direction.LEFT).step());
            case DOWN -> List.of(new Beam(beam.position(), Direction.RIGHT).step());
        };
    }

    private record Cord(int x, int y) {
        Cord add(Direction direction) {
            return new Cord(x + direction.step.x, y + direction.step.y);
        }
    }

    private enum Direction {
        LEFT(new Cord(-1, 0)), RIGHT(new Cord(1, 0)), UP(new Cord(0, -1)), DOWN(new Cord(0, 1));

        private Cord step;

        private Direction(Cord step) {
            this.step = step;
        }
    }

    private record Beam(Cord position, Direction direction) {
        Beam step() {
            return new Beam(position.add(direction), direction);
        }
    }

}
