package com.player.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Cell {
    private boolean known;

    private int ore;
    private boolean hole;
    private Coord coord;

    // History specific properties
    private boolean potentialEnemyTrap = false;

    Cell(boolean known, int ore, boolean hole) {
        this.known = known;
        this.ore = ore;
        this.hole = hole;
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

    public Coord getCoord() {
        return coord;
    }

    public boolean hasPotentialEnemyTrap() {
        return this.potentialEnemyTrap;
    }

    public void setPotentialEnemyTrap() {
        this.potentialEnemyTrap = true;
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

    public Set<Cell> getNeighbourCells(final Board board) {
        Set<Cell> neighbourhood = new HashSet<>();
        Set<Coord> coords = new HashSet<>();
        coords.add(new Coord(this.coord.getX(), this.coord.getY() + 1));
        coords.add(new Coord(this.coord.getX(), this.coord.getY() - 1));
        coords.add(new Coord(this.coord.getX() - 1, this.coord.getY()));
        coords.add(new Coord(this.coord.getX() + 1, this.coord.getY()));
        coords.forEach(coord -> {
            if (board.cellExist(coord)) {
                neighbourhood.add(board.getCell(coord));
            }
        });
        return neighbourhood;
    }

    // Impacted cells include neighbors and itself
    public Set<Cell> getImpactedCells(final Board board) {
        Set<Cell> impactedCells = getNeighbourCells(board);
        impactedCells.add(this);

        return impactedCells;
    }

    public static boolean isCloseToAllyBombs(Board board, int x, int y) {
        return isCloseToCoord(board.getMyTrapPos(), x, y);
    }

    public static boolean isCloseToCoord(Collection<Coord> coords, int x, int y) {
        return coords.stream().anyMatch(coord -> (
                (coord.getX() == x && coord.getY() == y)
                        || (coord.getX() == x && (coord.getY() == y - 1
                        || coord.getY() == y + 1))
                        || (coord.getY() == y && (coord.getX() == x - 1
                        || coord.getX() == x + 1))
        ));
    }
}
