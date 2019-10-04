package com.player.behaviours;

import com.player.model.*;

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

    // Returns closest Ore Cell, null if none
    Optional<Cell> getClosestOreCell() {
        Optional<Cell> closerCell = Optional.empty();
        int minDistance = board.getWidth();
        for (final Cell cell : board.getCells()) {
            int distance = cell.getCoord().distance(this.entity.getPos());
            if (cell.hasOre() && distance < minDistance && !cell.hasAllyTrap(board)) {
                closerCell = Optional.of(cell);
                minDistance = distance;
            }
        }
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
