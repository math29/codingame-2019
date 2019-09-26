package com.player;

import java.util.*;
import java.util.stream.Stream;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class Board {
    // Given at startup
    final int width;
    final int height;

    // Updated each turn
    final Team myTeam = new Team();
    final Team opponentTeam = new Team();
    private Cell[][] cells;
    int myRadarCooldown;
    int myTrapCooldown;
    Map<Integer, Entity> entitiesById;
    Collection<Coord> myRadarPos;
    Collection<Coord> myTrapPos;

    Board(Scanner in) {
        width = in.nextInt();
        height = in.nextInt();
    }

    void update(Scanner in) {
        // Read new data
        myTeam.readScore(in);
        opponentTeam.readScore(in);
        cells = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new Cell(in, new Coord(x, y));
            }
        }
        int entityCount = in.nextInt();
        myRadarCooldown = in.nextInt();
        myTrapCooldown = in.nextInt();
        entitiesById = new HashMap<>();
        myRadarPos = new ArrayList<>();
        myTrapPos = new ArrayList<>();
        for (int i = 0; i < entityCount; i++) {
            Entity entity = new Entity(in);
            entitiesById.put(entity.id, entity);
            if (entity.type == EntityType.ALLY_ROBOT) {
                myTeam.robots.add(entity);
            } else if (entity.type == EntityType.ENEMY_ROBOT) {
                opponentTeam.robots.add(entity);
            } else if (entity.type == EntityType.RADAR) {
                myRadarPos.add(entity.pos);
            } else if (entity.type == EntityType.TRAP) {
                myTrapPos.add(entity.pos);
            }
        }
    }

    boolean cellExist(Coord pos) {
        return (pos.x >= 0) && (pos.y >= 0) && (pos.x < width) && (pos.y < height);
    }

    Cell getCell(Coord pos) {
        return cells[pos.y][pos.x];
    }

    List<Cell> getCells() {
        return Arrays.asList(flattenStream(this.cells).toArray(Cell[]::new));
    }

    List<Cell> getHeadQuarterCells() {
        return Arrays.asList(this.cells[0]);
    }

    private static <T> Stream<T> flattenStream(T[][] arrays) {
        List<T> list = new ArrayList<>();
        for (T[] array : arrays) {
            Arrays.stream(array).forEach(list::add);
        }
        return list.stream();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
