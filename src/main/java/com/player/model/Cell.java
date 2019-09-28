package com.player.model;

import java.util.Scanner;

public class Cell {
    private boolean known;

    private int ore;
    private boolean hole;
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

    public boolean hasHole() {
        return this.hole;
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
}
