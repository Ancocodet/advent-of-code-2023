
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AInputData(day = 12, year = 2023)
public class Day12 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        AtomicLong sum = new AtomicLong();

        inputHelper.getInputAsStream().forEach(line -> {
            SpringHistory springHistory = new SpringHistory(line);

            for(List<Integer> commas : getVariations(springHistory.commas(), springHistory.unknownParts())) {
                if(springHistory.isValid(commas))
                    sum.getAndIncrement();
            }
        });

        return sum.get() + "";
    }
    
    @Override
    public String part2(IInputHelper inputHelper) {
        AtomicLong sum = new AtomicLong();

        inputHelper.getInputAsStream().forEach(line -> {
            SpringHistory springHistory = new SpringHistory(unFold(line));

            System.out.println(getVariations(springHistory.commas(), springHistory.unknownParts()).size());

            /*
            for(List<Integer> commas : getVariations(springHistory.commas(), springHistory.unknownParts())) {
                if(springHistory.isValid(commas))
                    sum.getAndIncrement();
            }
             */
        });

        return sum.get() + "";
    }

    private String unFold(String line) {
        String springs = line.split(" ")[0];
        StringBuilder lineBuilder = new StringBuilder(springs);
        for(int i = 0; i < 4; i++) {
            lineBuilder.append("?").append(springs);
        }

        String reqs = line.split(" ")[1];
        lineBuilder.append(" ").append(reqs);
        for(int i = 0; i < 4; i++) {
            lineBuilder.append(",")
                    .append(reqs);
        }

        return lineBuilder.toString();
    }

    private List<List<Integer>> getVariations(int commas, int maxSize) {
        List<List<Integer>> commaVariations = new ArrayList<>();
        if(commas == 0) {
            commaVariations.add(new ArrayList<>());
            return commaVariations;
        }
        for(int i = 0; i <= maxSize - commas; i++) {
            if(commas == 1) {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                commaVariations.add(list);
                continue;
            }
            List<List<Integer>> subVariations = getVariations(commas - 1, maxSize - i - 1);
            for(List<Integer> subVariation : subVariations) {
                List<Integer> correctedVariation = new ArrayList<>();
                correctedVariation.add(i);
                for(int value : subVariation) {
                    correctedVariation.add(value + i + 1);
                }
                commaVariations.add(correctedVariation);
            }
        }
        return commaVariations;
    }

    private class SpringHistory {
        List<Integer> requirements = new ArrayList<>();
        List<HistoryPart> historyParts = new ArrayList<>();

        public SpringHistory(String line) {
            String[] parts = line.split(" ");
            for (String requirement : parts[1].split(",")) {
                requirements.add(Integer.parseInt(requirement));
            }
            HistoryPart historyPart = new HistoryPart();
            for (char status : parts[0].toCharArray()) {
                if (status == '.') {
                    historyParts.add(historyPart);
                    historyPart = new HistoryPart();
                    continue;
                }
                if (status == '#') {
                    historyPart.addSpringStatus(SpringStatus.WORKING);
                }
                if (status == '?') {
                    historyPart.addSpringStatus(SpringStatus.UNKNOWN);
                }
            }
            historyParts.add(historyPart);
            historyParts.removeIf(HistoryPart::isEmpty);
        }

        public String toString() {
            return historyParts.toString();
        }

        public int size() {
            int sum = 0;
            for (HistoryPart part : historyParts) {
                sum += part.size();
            }
            return sum;
        }

        public int unknownWorkingSprings() {
            int sum = 0;
            for (int springs : requirements) {
                sum += springs;
            }
            return sum;
        }

        public int commas() {
            return this.size() - this.unknownWorkingSprings();
        }

        public boolean isValid(List<Integer> commas) {
            int maxValue = 0;
            List<Integer> matches = new ArrayList<>();
            for (HistoryPart historyPart : historyParts) {
                matches.addAll(historyPart.fulfill(commas, maxValue));
                maxValue += historyPart.unknownParts();
            }
            return matches.equals(requirements);
        }

        public int unknownParts() {
            int unknownParts = 0;
            for(HistoryPart historyPart: historyParts) {
                unknownParts += historyPart.unknownParts;
            }
            return unknownParts;
        }
    }


    private class HistoryPart {
        public List<SpringStatus> springStatuses = new ArrayList<>();
        private int unknownParts = 0;
        private int[] commaIndexes = new int[0];

        public void addSpringStatus(SpringStatus status) {
            springStatuses.add(status);
            if (status == SpringStatus.UNKNOWN) unknownParts++;
            commaIndexes = new int[unknownParts];
            int commaIndex = 0;
            for(int i = 0; i < springStatuses.size(); i++) {
                if(springStatuses.get(i) == SpringStatus.WORKING) continue;
                commaIndexes[commaIndex] = i;
                commaIndex++;
            }

        }

        public boolean isEmpty() {
            return springStatuses.isEmpty();
        }

        public String toString() {
            return springStatuses.toString();
        }

        public int size() {
            return springStatuses.size();
        }

        public int unknownParts() {
            return unknownParts;
        }

        public List<Integer> fulfill(List<Integer> commas, int position) {
            List<SpringStatus> fulfilledStatuses = new ArrayList<>();
            for (int i = 0; i < springStatuses.size(); i++) {
                fulfilledStatuses.add(i, SpringStatus.WORKING);
            }
            for (int comma : commas) {
                if(comma < position) continue;
                if(comma - position >= commaIndexes.length) continue;
                fulfilledStatuses.set(commaIndexes[comma - position], SpringStatus.BROKEN);
            }
            List<Integer> partSizes = new ArrayList<>();
            int currentSize = 0;
            for (SpringStatus springStatus : fulfilledStatuses) {
                if (springStatus == SpringStatus.WORKING) {
                    currentSize++;
                    continue;
                }
                if (currentSize != 0) {
                    partSizes.add(currentSize);
                    currentSize = 0;
                }
            }
            if (currentSize != 0) partSizes.add(currentSize);
            return partSizes;
        }
    }

    private enum SpringStatus {
        WORKING,
        UNKNOWN,
        BROKEN
    }

}
