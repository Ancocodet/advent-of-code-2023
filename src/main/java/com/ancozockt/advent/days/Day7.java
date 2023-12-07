package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;

@AInputData(day = 7, year = 2023)
public class Day7 implements IAdventDay {

    public enum DeckType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    public record Deck(String cards, Long bits){

        public DeckType getType(Map<Character, Integer> frequencyMap) {
            int maxCount = 0;
            int secondCount = 0;

            for (int value : frequencyMap.values()) {
                if (value >= maxCount) {
                    secondCount = maxCount;
                    maxCount = value;
                } else if (value > secondCount) {
                    secondCount = value;
                }
            }


            return switch (maxCount) {
                case 5 -> DeckType.FIVE_OF_A_KIND;
                case 4 -> DeckType.FOUR_OF_A_KIND;
                case 3 -> secondCount == 2 ? DeckType.FULL_HOUSE : DeckType.THREE_OF_A_KIND;
                case 2 -> secondCount == 2 ? DeckType.TWO_PAIR : DeckType.ONE_PAIR;
                default -> DeckType.HIGH_CARD;
            };
        }

        public DeckType getTypePartTwo(Map<Character, Integer> frequencyMap) {
            int maxCount = 0;
            int secondCount = 0;

            int jcount = frequencyMap.getOrDefault('J',0);
            if(jcount > 0)
                frequencyMap.remove('J');
            for (int value : frequencyMap.values()) {
                if (value >= maxCount) {
                    secondCount = maxCount;
                    maxCount = value;
                } else if (value > secondCount) {
                    secondCount = value;
                }
            }
            maxCount += jcount;

            return switch (maxCount) {
                case 5 -> DeckType.FIVE_OF_A_KIND;
                case 4 -> DeckType.FOUR_OF_A_KIND;
                case 3 -> secondCount == 2 ? DeckType.FULL_HOUSE : DeckType.THREE_OF_A_KIND;
                case 2 -> secondCount == 2 ? DeckType.TWO_PAIR : DeckType.ONE_PAIR;
                default -> DeckType.HIGH_CARD;
            };
        }

    }

    private Map<Deck, DeckType> determineDeckTypes(List<Deck> decks) {
        Map<Deck, DeckType> handTypes = new HashMap<>();
        for (Deck deck : decks) {
            handTypes.put(deck, deck.getType(getFrequencyMap(deck)));
        }
        return handTypes;
    }

    private Map<Deck, DeckType> determineDeckTypesPartTwo(List<Deck> decks) {
        Map<Deck, DeckType> handTypes = new HashMap<>();
        for (Deck deck : decks) {
            handTypes.put(deck, deck.getTypePartTwo(getFrequencyMap(deck)));
        }
        return handTypes;
    }

    private Map<Character, Integer> getFrequencyMap(Deck deck) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char card : deck.cards().toCharArray()) {
            frequencyMap.put(card, frequencyMap.getOrDefault(card, 0) + 1);
        }
        return frequencyMap;
    }

    @Override
    public String part1(IInputHelper inputHelper) {
        List<Deck> decks = new ArrayList<>();

        inputHelper.getInputAsStream().forEach(line -> {
            String cards = line.split(" ")[0];
            Long bits = Long.parseLong(line.split(" ")[1]);
            Deck deck = new Deck(cards, bits);

            decks.add(deck);
        });

        Map<Deck, DeckType> deckTypes = determineDeckTypes(decks);
        String cardOrder = "AKQJT98765432";
        decks.sort(compareHandStrength(cardOrder, deckTypes));

        long sum = 0;
        int k = decks.size();
        for (Deck deck : decks) {
            sum += (deck.bits() * k);
            k--;
        }
        return sum + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        List<Deck> decks = parseDeck(inputHelper);
        Map<Deck, DeckType> deckTypes = determineDeckTypesPartTwo(decks);

        String cardOrder = "AKQT98765432J";
        decks.sort(compareHandStrength(cardOrder, deckTypes));

        long sum = 0;
        int k = decks.size();
        for (Deck deck : decks) {
            sum += (deck.bits() * k);
            k--;
        }
        return sum + "";
    }

    private ArrayList<Deck> parseDeck(IInputHelper inputHelper) {
        ArrayList<Deck> decks = new ArrayList<>();

        inputHelper.getInputAsStream().forEach(line -> {
            String cards = line.split(" ")[0];
            Long bits = Long.parseLong(line.split(" ")[1]);
            Deck deck = new Deck(cards, bits);

            decks.add(deck);
        });

        return decks;
    }

    public Comparator<Deck> compareHandStrength(String cardOrder , Map<Deck, DeckType> deckTypes) {
        return (hand1, hand2) -> {
            DeckType type1 = deckTypes.get(hand1);
            DeckType type2 = deckTypes.get(hand2);

            if (type1 != type2) {
                return Integer.compare(type1.ordinal(), type2.ordinal());
            }

            return compareIndividualCards(cardOrder, hand1, hand2);
        };
    }

    public int compareIndividualCards(String cardOrder, Deck hand1, Deck hand2) {
        for (int i = 0; i < hand1.cards().length(); i++) {
            char card1 = hand1.cards().charAt(i);
            char card2 = hand2.cards().charAt(i);

            if (card1 != card2) {
                return compareCards(cardOrder,card1, card2);
            }
        }

        return 0; // Both hands are equal
    }
    private int compareCards(String cardOrder, char card1, char card2) {
        int index1 = cardOrder.indexOf(card1);
        int index2 = cardOrder.indexOf(card2);

        return Integer.compare(index1, index2);
    }

}
