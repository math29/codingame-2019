package com.player.model;

import java.util.Scanner;

public class Entity {

    private static final Coord DEAD_POS = new Coord(-1, -1);

    private final int id;
    private final EntityType type;
    private final Coord pos;
    private final EntityType item;

    // History specific properties
    private boolean isTerroristSuspect = false;

    Action action;

    Entity(Scanner in) {
        id = in.nextInt();
        type = EntityType.valueOf(in.nextInt());
        pos = new Coord(in);
        item = EntityType.valueOf(in.nextInt());
    }

    public boolean isAlive() {
        return !DEAD_POS.equals(pos);
    }

    public boolean hasItem() {
        return this.item != EntityType.NOTHING;
    }

    public boolean isAtHeadquarters() {
        return pos.getX() == 0;
    }

    public int getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    public Coord getPos() {
        return pos;
    }

    public EntityType getItem() {
        return item;
    }

    public void tagAsTerroristSuspect() {
        isTerroristSuspect = true;
    }

    public void removeTagAsTerroristSuspect() {
        isTerroristSuspect = false;
    }

    public boolean isTerroristSuspect() {
        return this.isTerroristSuspect;
    }

    public boolean isInsideRadarOrTrapZone(final Board board) {
        return this.getPos().getX() >= 5
                && this.getPos().getX() < board.getWidth() - 3
                && this.getPos().getY() >= 4
                && this.getPos().getY() < board.getHeight() - 3;
    }

    public int getDistanceFromClosestHeadQuarterCell(final Board board) {
        int minDistance = 50;
        for (final Cell cell : board.getHeadQuarterCells()) {
            int distance = cell.getCoord().distance(this.getPos());
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }
}
