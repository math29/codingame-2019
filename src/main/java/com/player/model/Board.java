package com.player.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board implements Cloneable {
    private int width;
    private int height;

    private Team myTeam = new Team();
    private Team opponentTeam = new Team();
    private Cell[][] cells;

    private int myRadarCooldown;
    private int myTrapCooldown;

    private Map<Integer, Entity> entitiesById;
    private Collection<Coord> myRadarPos;
    private Set<Coord> myTrapPos;
    private static Set<Coord> potentialEnemyTrapPos = new HashSet<>();

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
        myTrapPos = new HashSet<>();
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
        this.updateFromHistory();
        this.analyseEnemyBehaviour();
    }

    private void updateFromHistory() {
        Optional<Board> previousTurn = History.getPreviousTurn();
        if (previousTurn.isPresent()) {
            previousTurn.get().getCells().stream()
                .filter(Cell::hasPotentialEnemyTrap)
                .forEach(cell -> this.getCell(cell.getCoord()).setPotentialEnemyTrap());
            previousTurn.get().getOpponentTeam().getRobotsAlive().stream()
                .filter(Entity::isTerroristSuspect)
                .forEach(entity -> this.getOpponentTeam().getRobot(entity.getId()).get().tagAsTerroristSuspect());
        }
    }

    private void analyseEnemyBehaviour() {
        /*List<String> terroristsBefore = this.getOpponentTeam().getRobotsAlive().stream()
                .filter(Entity::isTerroristSuspect)
                .flatMap(r -> Stream.of(String.valueOf(r.getId()), r.getPos().toString()))
                .collect(Collectors.toList());
        System.err.println("before: " + terroristsBefore.toString());*/

        this.setTerroristTags();
        this.setEnemyTrapCellTags();
        this.removeTerroristTags();

        /*List<Coord> potTraps = this.getCells().stream()
                .filter(c -> c.hasPotentialEnemyTrap())
                .map(c -> c.getCoord())
                .collect(Collectors.toList());
        System.err.println("PotTraps:" + potTraps.toString());*/

        /*List<String> terroristsAfter = this.getOpponentTeam().getRobotsAlive().stream()
                .filter(Entity::isTerroristSuspect)
                .flatMap(r -> Stream.of(String.valueOf(r.getId()), r.getPos().toString()))
                .collect(Collectors.toList());
        System.err.println("after: " + terroristsAfter.toString());*/
    }

    private void setTerroristTags() {
        this.getOpponentTeam().getRobotsAlive().forEach(entity -> {
            Optional<Board> previousTurn = History.getPreviousTurn();
            if (previousTurn.isPresent()) {
                Optional<Entity> previousEntityState = previousTurn.get().getOpponentTeam().getRobot(entity.getId());
                if (previousEntityState.isPresent()
                    && previousEntityState.get().isAtHeadquarters()
                    && entity.isAtHeadquarters()) {
                    /*System.err.println(String.format("TerroristTag: %s [%s,%s]", entity.getId(), entity.getPos().getX(),
                            entity.getPos().getY()));*/
                    entity.tagAsTerroristSuspect();
                }
            }
        });
    }

    private void setEnemyTrapCellTags() {
        this.getOpponentTeam().getRobotsAlive().forEach(entity -> {
            Optional<Board> previousTurn = History.getPreviousTurn();
            if (previousTurn.isPresent()) {
                Optional<Entity> previousEntityState = previousTurn.get().getOpponentTeam().getRobot(entity.getId());
                if (previousEntityState.isPresent()
                    && entity.isTerroristSuspect()
                    && !entity.isAtHeadquarters()
                    && entity.getPos().equals(previousEntityState.get().getPos())) {
                    this.cellEnemyTrapNeighbourhoodAnalysis(this.getCell(entity.getPos()), previousTurn.get());
                }
            }
        });
    }

    private void removeTerroristTags() {
        this.getOpponentTeam().getRobotsAlive().forEach(entity -> {
            Optional<Board> previousTurn = History.getPreviousTurn();
            if (previousTurn.isPresent()) {
                Optional<Entity> previousEntityState = previousTurn.get().getOpponentTeam().getRobot(entity.getId());
                if (previousEntityState.isPresent()
                        && !previousEntityState.get().isAtHeadquarters()
                        && entity.isAtHeadquarters()) {
                    // We should not remove suspect tag immediately (as enemy might be 'faking' and just waiting)
                    entity.removeTagAsTerroristSuspect();
                }
            }
        });
    }

    private void cellEnemyTrapNeighbourhoodAnalysis(final Cell cell, final Board previousTurn) {
        Set<Cell> impactedCells = cell.getImpactedCells(this);
        Set<Cell> impactedHoles = impactedCells.stream()
                .filter(Cell::isHole)
                .collect(Collectors.toSet());
        Set<Cell> previouslyHoles = impactedCells.stream()
                .map(c -> previousTurn.getCell(c.getCoord()))
                .filter(Cell::isHole)
                .collect(Collectors.toSet());

        if (impactedHoles.size() > previouslyHoles.size()) {
            impactedHoles.forEach(c -> {
                if (!previouslyHoles.contains(c)) {
                    c.setPotentialEnemyTrap();
                    potentialEnemyTrapPos.add(c.getCoord());
                }
            });
        } else {
            impactedHoles.forEach(c -> {
                c.setPotentialEnemyTrap();
                potentialEnemyTrapPos.add(c.getCoord());
            });
        }
    }

    boolean cellExist(Coord pos) {
        return (pos.getX() >= 0) && (pos.getY() >= 0) && (pos.getX() < width) && (pos.getY() < height);
    }

    public boolean isDangerousEnemyInRange(Cell cell) {
        Set<Coord> allCloseToCellTraps = cell.getAllCloseToCellTraps(this);
        if (allCloseToCellTraps.isEmpty()) {
            return false;
        }

        for (Coord trapCoord : allCloseToCellTraps) {
            // Consider that impacted cells are the cells within the range of 4 (as enemy can cross 4 cells in 1 turn)
            Set<Cell> impactedCells = getImpactedCells(new HashSet<>(Collections.singletonList(this.getCell(trapCoord))));
            for (int i = 0; i < 3; i++) {
                impactedCells = getImpactedCells(impactedCells);
            }
            Set<Cell> finalImpactedCells = impactedCells;
            long enemyCount = this.getOpponentTeam().getRobotsAlive().stream()
                    .filter(r -> finalImpactedCells.contains(this.getCell(r.getPos())))
                    .count();

            if (enemyCount > 0) {
                return true;
            }
        }

        return false;
    }

    public Set<Cell> getImpactedCells(Set<Cell> cells) {
        Set<Cell> impactedCells = new HashSet<>();
        for(Cell cell : cells) {
            Set<Cell> cellTmp = cell.getImpactedCells(this);
            impactedCells.addAll(cellTmp);
        }
        return impactedCells;
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

    public Set<Coord> getMyTrapPos() {
        return myTrapPos;
    }

    public Set<Coord> getPotentialEnemyTrapPos() {
        return potentialEnemyTrapPos;
    }

    public Set<Coord> getAllTrapPos() {
        Set<Coord> allTraps = new HashSet<>(getMyTrapPos());
        allTraps.addAll(getPotentialEnemyTrapPos());
        return allTraps;
    }

    public Board clone() throws CloneNotSupportedException {
        Board clone = (Board) super.clone();
        clone.myTeam = this.getMyTeam().clone();
        clone.opponentTeam = this.getOpponentTeam().clone();
        clone.cells = deepCloneCells(this.cells);
        clone.entitiesById = new HashMap<>(this.getEntitiesById());
        clone.myRadarPos = new ArrayList<>(this.getMyRadarPos());
        clone.myTrapPos = new HashSet<>(this.getMyTrapPos());
        return clone;
    }

    private static Cell[][] deepCloneCells(Cell[][] input) {
        if (input == null)
            return null;
        Cell[][] result = new Cell[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
}
