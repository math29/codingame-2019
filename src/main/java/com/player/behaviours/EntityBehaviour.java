package com.player.behaviours;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public abstract class EntityBehaviour {

    protected Entity entity;
    protected Board board;
    protected String NAME;

    EntityBehaviour(final Entity entity, final Board board) {
        this.entity = entity;
        this.board = board;
    }

    public abstract Action getNextAction();

    Action returnAction(final Action action) {
        return action.withMessage(this.NAME);
    }

    // Returns closest Ore Cell, Optional.empty() if none
    Optional<Cell> getClosestOreCell() {
        Optional<Cell> closerCell = Optional.empty();
        int minDistance = board.getWidth();
        for (final Cell cell : board.getCells()) {
            int distance = cell.getCoord().distance(this.entity.getPos());
            if (cell.hasOre()
                    && distance < minDistance
                    && !cell.hasAllyTrap(board)) {
                closerCell = Optional.of(cell);
                minDistance = distance;
            }
        }
        return closerCell;
    }

    Cell getCloserHeadQuarterSafeCell() {
        Cell closerCell = null;
        int minDistance = board.getWidth();

        // Enemy is dangerous if he is close to ally or enemy trap
        boolean isDangerousEnemyNearHeadQuarters = board.getOpponentTeam().getRobots().stream()
                .anyMatch(e -> {
                    int x = e.getPos().getX(), y = e.getPos().getY();

                    return Cell.isCloseToAllyBombs(board, x, y)
                            || Cell.isCloseToEnemyBombs(board, x, y);
                });

        for (final Cell cell : board.getHeadQuarterCells()) {

            // If there is an enemy at headquarters, try to not be blown by a Trap with (potentially) other allies
            if (isDangerousEnemyNearHeadQuarters) {
                int x = cell.getCoord().getX(), y = cell.getCoord().getY();

                // Try to avoid cells in the impact area of ally or enemy traps
                if (Cell.isCloseToAllyBombs(board, x, y)
                        || Cell.isCloseToAllyBombs(board, x + 1, y)
                        || Cell.isCloseToAllyBombs(board, x - 1, y)
                        || Cell.isCloseToEnemyBombs(board, x, y)) {
                    continue;
                }

                // Do not go on the cell where you already have ally
                boolean isAllyAtHeadQuarters = board.getMyTeam().getRobots().stream()
                        .filter(Entity::isAtHeadquarters)
                        .anyMatch(e -> e.getPos().equals(new Coord(x, y))
                                || e.getPos().equals(new Coord(x, y + 1))
                                || e.getPos().equals(new Coord(x, y - 1)));
                if (isAllyAtHeadQuarters) {
                    //System.err.println(String.format("Hello buddy! [%s,%s]", x, y));
                    continue;
                }
            }

            int distance = cell.getCoord().distance(this.entity.getPos());
            if (distance < minDistance) {
                closerCell = cell;
                minDistance = distance;
            }
        }

        // Fall bac in default behaviour
        if (closerCell == null) {
            //System.err.println("No safe close cell was found!");
            closerCell = getCloserHeadQuarterCell();
        } /*else {
            System.err.println("getCloserHeadQuarterSafeCell: " + closerCell.getCoord().toString());
        }*/

        return closerCell;
    }

    Cell getCloserHeadQuarterCell() {
        Cell closerCell = board.getCell(new Coord(0, 0));
        int minDistance = board.getWidth();
        for (final Cell cell : board.getHeadQuarterCells()) {
            int distance = cell.getCoord().distance(this.entity.getPos());
            if (distance < minDistance) {
                closerCell = cell;
                minDistance = distance;
            }
        }

        //System.err.println("getCloserHeadQuarterCell: " + closerCell.getCoord().toString());

        return closerCell;
    }

    Coord getRandomCoord(int startX, int endX, int startY, int endY) {
        int x = ThreadLocalRandom.current().nextInt(startX, endX);
        int y = ThreadLocalRandom.current().nextInt(startY, endY);
        return new Coord(x, y);
    }

    static boolean isInside(int circle_x, int circle_y, int rad, int x, int y) {
        return (x - circle_x) * (x - circle_x) + (y - circle_y) * (y - circle_y) <= rad * rad;
    }
}
