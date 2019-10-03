package com.player.model;

import java.util.HashMap;
import java.util.Map;

public class History {

    private static int numberOfTurns = 0;

    private static Map<Integer, Board> turns = new HashMap<>();

    public static void recordNewTurn(final Board board) {
        turns.put(numberOfTurns, board);
    }

    public static int getNumberOfTurns() {
        return numberOfTurns;
    }

    public static Map<Integer, Board> getTurns() {
        return turns;
    }
}
