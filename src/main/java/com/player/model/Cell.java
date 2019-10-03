package com.player.model;

import java.util.Scanner;

public class Cell {
    private boolean known;

    private int ore;
    private boolean hole;
    // @TODO: use this property
    private boolean safe;
    private Coord coord;

    Cell(boolean known, int ore, boolean hole, boolean safe) {
        this.known = known;
        this.ore = ore;
        this.hole = hole;
        this.safe = safe;
    }

    Cell(Scanner in, final Coord coord) {
        this.coord = coord;
        String oreStr = in.next();
        if (oreStr.charAt(0) == '?') {
            known = false;
            ore = 0;
        } else {
            known = true;
            ore = Integer.parseInt(oreStr);
        }
        String holeStr = in.next();
        hole = (holeStr.charAt(0) != '0');
    }

    public boolean hasOre() {
        return this.ore > 0;
    }

    public boolean isKnown() {
        return known;
    }

    public int getOre() {
        return ore;
    }

    public boolean isHole() {
        return hole;
    }

    public boolean isSafe() {
        return safe;
    }

    public Coord getCoord() {
        return coord;
    }

    public boolean hasAllyTrap(final Board board) {
        if (board.getMyTrapPos().isEmpty()) {
            return false;
        }
        Coord cellOptional = board.getMyTrapPos().parallelStream()
                .filter(trapCoord -> trapCoord.getX() == this.getCoord().getX() && trapCoord.getY() == this.getCoord().getY())
                .findFirst()
                .orElse(null);
        return cellOptional != null;
    }

    public boolean isRadarFree(final Board board) {
        Coord cellOptional = board.getMyRadarPos().parallelStream()
                .filter(coord -> coord.getX() == this.getCoord().getX() && coord.getY() == this.getCoord().getY()).findFirst()
                .orElse(null);
        return cellOptional == null;
    }
}
