package com.player.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private final int width;
    private final int height;

    private final Team myTeam = new Team();
    private final Team opponentTeam = new Team();
    private Cell[][] cells;

    private int myRadarCooldown;

    private int myTrapCooldown;
    private Map<Integer, Entity> entitiesById;
    private Collection<Coord> myRadarPos;
    private Collection<Coord> myTrapPos;

    public Board(Scanner in) {
        width = in.nextInt();
        height = in.nextInt();
    }

    public void update(Scanner in) {
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
            entitiesById.put(entity.getId(), entity);
            if (entity.getType() == EntityType.ALLY_ROBOT) {
                myTeam.getRobots().add(entity);
            } else if (entity.getType() == EntityType.ENEMY_ROBOT) {
                opponentTeam.getRobots().add(entity);
            } else if (entity.getType() == EntityType.RADAR) {
                myRadarPos.add(entity.getPos());
            } else if (entity.getType() == EntityType.TRAP) {
                myTrapPos.add(entity.getPos());
            }
        }
    }

    boolean cellExist(Coord pos) {
        return (pos.getX() >= 0) && (pos.getY() >= 0) && (pos.getX() < width) && (pos.getY() < height);
    }

    public Cell getCell(Coord pos) {
        return cells[pos.getY()][pos.getX()];
    }

    public List<Cell> getCells() {
        return Arrays.asList(flattenStream(this.cells).toArray(Cell[]::new));
    }

    public List<Cell> getCellsOnColumn(int i) {
        return Arrays.asList(getColumn(this.cells, i));
    }

    public Cell[] getColumn(Cell[][] matrix, int index) {
        Cell[] column = new Cell[matrix[0].length];
        for(int i=0; i<column.length; i++){
            column[i] = matrix[i][index];
        }
        return column;
    }

    public List<Cell> getHeadQuarterCells() {
        return Arrays.stream(this.cells).map(xCells -> xCells[0]).collect(Collectors.toList());
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

    public Team getMyTeam() {
        return this.myTeam;
    }

    public Team getOpponentTeam() {
        return this.opponentTeam;
    }

    public int getMyRadarCooldown() {
        return myRadarCooldown;
    }

    public int getMyTrapCooldown() {
        return myTrapCooldown;
    }

    public Map<Integer, Entity> getEntitiesById() {
        return entitiesById;
    }

    public Collection<Coord> getMyRadarPos() {
        return myRadarPos;
    }

    public Collection<Coord> getMyTrapPos() {
        return myTrapPos;
    }
}
