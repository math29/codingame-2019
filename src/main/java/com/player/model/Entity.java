package com.player.model;

import java.util.Scanner;

public class Entity {

    private static final Coord DEAD_POS = new Coord(-1, -1);

    private final int id;

    private final EntityType type;
    private final Coord pos;
    private final EntityType item;
    // Computed for my robots

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
}
