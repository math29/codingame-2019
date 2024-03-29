package com.player.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class History {

    private static int numberOfTurns = 0;

    private static Map<Integer, Board> turns = new HashMap<>();

    public static void recordNewTurn(final Board board) {
        turns.put(numberOfTurns, board);
        numberOfTurns = numberOfTurns + 1;
    }

    public static int getNumberOfTurns() {
        return numberOfTurns;
    }

    public static Map<Integer, Board> getTurns() {
        return turns;
    }

    public static Optional<Board> getPreviousTurn() {
        if (numberOfTurns == 0) {
            return Optional.empty();
        }
        return Optional.of(turns.get(numberOfTurns - 1));
    }

    // For test purpose only
    public static void reset() {
        numberOfTurns = 0;
        turns = new HashMap<>();
    }

    private History() { }
}
