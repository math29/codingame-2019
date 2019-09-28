package com.player.model;

import java.util.Scanner;

public class Coord {
    private final int x;

    private final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(Scanner in) {
        this(in.nextInt(), in.nextInt());
    }

    public Coord add(Coord other) {
        return new Coord(x + other.x, y + other.y);
    }

    // Manhattan distance (for 4 directions maps)
    // see: https://en.wikipedia.org/wiki/Taxicab_geometry

    public int distance(Coord other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + x;
        result = PRIME * result + y;
        return result;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coord other = (Coord) obj;
        return (x == other.x) && (y == other.y);
    }

    public String toString() {
        return x + " " + y;
    }
}
