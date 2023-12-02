package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.List;

@AInputData(day = 2, year = 2023)
public class Day2 implements IAdventDay {

    public record Game(int id, List<Round> rounds){

        int maxRed() {
            return rounds.stream().mapToInt(Round::red).max().orElse(0);
        }

        int maxGreen() {
            return rounds.stream().mapToInt(Round::green).max().orElse(0);
        }

        int maxBlue() {
            return rounds.stream().mapToInt(Round::blue).max().orElse(0);
        }

    }

    public record Round(int red, int green, int blue){ }

    @Override
    public String part1(IInputHelper inputHelper) {
        ArrayList<Game> games = readInput(inputHelper);

        return games.stream()
                .filter(game -> game.maxRed() <= 12 && game.maxGreen() <= 13 && game.maxBlue() <= 14)
                .mapToInt(Game::id).sum() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        ArrayList<Game> games = readInput(inputHelper);

        return games.stream().mapToInt(game -> game.maxRed() * game.maxBlue() * game.maxGreen()).sum() + "";
    }

    public ArrayList<Game> readInput(IInputHelper inputHelper) {
        ArrayList<Game> games = new ArrayList<>();

        inputHelper.getInputAsStream().forEach(line -> {
            String[] split = line.split(": ");
            String[] sets = split[1].split("; ");

            int id = Integer.parseInt(split[0].split(" ")[1]);

            List<Round> rounds = new ArrayList<>();
            for(String set : sets) {
                String[] cubes = set.split(", ");

                int red = 0;
                int green = 0;
                int blue = 0;
                for (String cube : cubes) {
                    String[] colors = cube.split(" ");

                    if(colors[1].equalsIgnoreCase("red")){
                        red = Integer.parseInt(colors[0]);
                    }

                    if(colors[1].equalsIgnoreCase("green")){
                        green = Integer.parseInt(colors[0]);
                    }

                    if(colors[1].equalsIgnoreCase("blue")){
                        blue = Integer.parseInt(colors[0]);
                    }
                }

                rounds.add(new Round(red, green, blue));
            }


            games.add(new Game(id, rounds));
        });

        return games;
    }

}
