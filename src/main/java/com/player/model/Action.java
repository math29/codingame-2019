package com.player.model;

import java.util.HashSet;
import java.util.Set;

public class Action {
    private final String command;
    private final Coord pos;
    private final EntityType item;
    private String message;

    private static Set<Coord> posDigged = new HashSet<>();

    private Action(String command, Coord pos, EntityType item) {
        this.command = command;
        this.pos = pos;
        this.item = item;
    }

    public static Action none() {
        return new Action("WAIT", null, null);
    }

    public static Action move(Coord pos) {
        return new Action("MOVE", pos, null);
    }

    public static Action dig(Coord pos) {
        posDigged.add(pos);
        return new Action("DIG", pos, null);
    }

    public static Action request(EntityType item) {
        return new Action("REQUEST", null, item);
    }

    public static boolean IsDiggedByUs(Coord pos) {
        return posDigged.contains(pos);
    }

    public Action withMessage(final String message) {
        this.message = message;
        return this;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(command);
        if (pos != null) {
            builder.append(' ').append(pos);
        }
        if (item != null) {
            builder.append(' ').append(item);
        }
        if (message != null) {
            builder.append(' ').append(message);
        }
        return builder.toString();
    }
}
