
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayDeque;
import java.util.Queue;

@AInputData(day = 10, year = 2023)
public class Day10 implements IAdventDay {

    private final char[] PIPES     = {'|', '-', 'L', 'J', '7', 'F'};
    private final int[] DIRECTIONS = {-1, 0, 0, 1, 1, 0, 0, -1};

    @Override
    public String part1(IInputHelper inputHelper) {
        char[][] map = parseMap(inputHelper);
        int[] start = findStartPoint(map);

        int rows = map.length;
        int cols = map[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] distance = new int[rows][cols];

        queue.offer(new int[]{start[0], start[1]});
        visited[start[0]][start[1]] = true;

        int maxDistance = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];
            for (int i = 0; i < DIRECTIONS.length; i += 2) {
                int newRow = currentRow + DIRECTIONS[i];
                int newCol = currentCol + DIRECTIONS[i + 1];

                if (isValid(newRow, newCol, rows, cols) && !visited[newRow][newCol] && isPipe(map[newRow][newCol])) {
                    char currentPipe = map[currentRow][currentCol];
                    char newPipe = map[newRow][newCol];
                    if (canConnect(currentPipe, newPipe, i)) {
                        queue.offer(new int[]{newRow, newCol});
                        visited[newRow][newCol] = true;
                        distance[newRow][newCol] = distance[currentRow][currentCol] + 1;
                        maxDistance = Math.max(maxDistance, distance[newRow][newCol]);
                    }
                }
            }
        }

        return maxDistance + "";
    }
    
    @Override
    public String part2(IInputHelper inputHelper) {
        char[][] map = parseMap(inputHelper);
        int[] start = findStartPoint(map);

        int rows = map.length;
        int cols = map[0].length;

        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] distance = new int[rows][cols];

        queue.offer(new int[]{start[0], start[1]});
        visited[start[0]][start[1]] = true;

        long notVisited = 0;
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];
            for (int i = 0; i < DIRECTIONS.length; i += 2) {
                int newRow = currentRow + DIRECTIONS[i];
                int newCol = currentCol + DIRECTIONS[i + 1];
                if (isValid(newRow, newCol, rows, cols) && !visited[newRow][newCol] && isPipe(map[newRow][newCol])) {
                    char currentPipe = map[currentRow][currentCol];
                    char newPipe = map[newRow][newCol];
                    if (canConnect(currentPipe, newPipe, i)) {
                        queue.offer(new int[]{newRow, newCol});
                        visited[newRow][newCol] = true;
                        distance[newRow][newCol] = distance[currentRow][currentCol] + 1;
                    } else {
                        notVisited++;
                    }
                }
            }
        }

        return notVisited + "";
    }

    private int[] findStartPoint(char[][] map) {
        int[] start = new int[2];
        for (int y = 0; y < map.length; y++) {
            char[] row = map[y];
            for (int x = 0; x < row.length; x++) {
                char c = row[x];
                if(c == 'S') {
                    start[0] = y;
                    start[1] = x;
                    return start;
                }
            }
        }
        return null;
    }

    private char[][] parseMap(IInputHelper inputHelper) {
        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        int rows = lines.length;
        int cols = lines[0].length();

        char[][] map = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            map[i] = lines[i].toCharArray();
        }

        return map;
    }

    private boolean canConnect(char currentPipe, char newPipe, int idx) {
        return switch (currentPipe) {
            case '|' ->
                    ((idx == 0 && (newPipe == '|' || newPipe == '7' || newPipe == 'F')) || (idx == 4 && (newPipe == '|' || newPipe == 'J' || newPipe == 'L')));
            case '-' ->
                    ((idx == 2 && (newPipe == '-' || newPipe == '7' || newPipe == 'J')) || (idx == 6 && (newPipe == '-' || newPipe == 'F' || newPipe == 'L')));
            case 'L' ->
                    ((idx == 0 && (newPipe == '|' || newPipe == '7' || newPipe == 'F')) || (idx == 2 && (newPipe == '-' || newPipe == '7' || newPipe == 'J')));
            case 'J' ->
                    ((idx == 0 && (newPipe == '|' || newPipe == '7' || newPipe == 'F')) || (idx == 6 && (newPipe == '-' || newPipe == 'F' || newPipe == 'L')));
            case '7' ->
                    ((idx == 4 && (newPipe == '|' || newPipe == 'J' || newPipe == 'L')) || (idx == 6 && (newPipe == '-' || newPipe == 'F' || newPipe == 'L')));
            case 'F' ->
                    ((idx == 2 && (newPipe == '-' || newPipe == '7' || newPipe == 'J')) || (idx == 4 && (newPipe == '|' || newPipe == 'J' || newPipe == 'L')));
            case 'S' ->
                    ((idx == 0 && (newPipe == '|' || newPipe == '7' || newPipe == 'F')) || (idx == 2 && (newPipe == '-' || newPipe == 'J' || newPipe == '7')) || (idx == 4 && (newPipe == '|' || newPipe == 'J' || newPipe == 'L')) || (idx == 6 && (newPipe == '-' || newPipe == 'F' || newPipe == 'L')));
            default -> false;
        };
    }

    private boolean isValid(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private boolean isPipe(char c) {
        for (char pipe : PIPES) {
            if (c == pipe) {
                return true;
            }
        }
        return false;
    }

}
