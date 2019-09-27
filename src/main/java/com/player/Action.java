package com.player;

import java.util.HashSet;
import java.util.Set;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class Action {
    final String command;
    final Coord pos;
    final EntityType item;
    String message;

    private static Set<Coord> posDigged = new HashSet<>();

    private Action(String command, Coord pos, EntityType item) {
        this.command = command;
        this.pos = pos;
        this.item = item;
    }

    static Action none() {
        return new Action("WAIT", null, null);
    }

    static Action move(Coord pos) {
        return new Action("MOVE", pos, null);
    }

    static Action dig(Coord pos) {
        posDigged.add(pos);
        System.err.println(String.format("I dig: (%s,%s). Totally digged: %s.", pos.x, pos.y, posDigged.size()));

        return new Action("DIG", pos, null);
    }

    static Action request(EntityType item) {
        return new Action("REQUEST", null, item);
    }

    static boolean IsDiggedByUs(Coord pos) {
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
