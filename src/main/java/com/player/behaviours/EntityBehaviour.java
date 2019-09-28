package com.player.behaviours;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;

public abstract class EntityBehaviour {

  protected Entity entity;
  protected Board board;
  protected String NAME;

  public EntityBehaviour(final Entity entity, final Board board) {
    this.entity = entity;
    this.board = board;
  }

  public abstract Action getNextAction();

  Action returnAction(final Action action) {
    return action.withMessage(this.NAME);
  }

  // Returns closest Ore Cell, null if none
  protected Optional<Cell> getClosestOreCell() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.hasOre() && distance < minDistance && !isCellBad(cell)) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }

  protected Cell getCloserHeadQuarterCell() {
    Cell closerCell = board.getCell(new Coord(0, 0));
    int minDistance = 50;
    for (final Cell cell : board.getHeadQuarterCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (distance < minDistance) {
        closerCell = cell;
        minDistance = distance;
      }
    }
    return closerCell;
  }

  protected Coord getRandomSafeCoord(int startX, int endX, int startY, int endY) {
    Coord coord = getRandomCoord(startX, endX, startY, endY);
    if (!isCellBad(board.getCell(coord))) {
      return coord;
    } else {
      return getRandomSafeCoord(startX, endX, startY, endY);
    }
  }

  protected Coord getRandomCoord(int startX, int endX, int startY, int endY) {
    int x = ThreadLocalRandom.current().nextInt(startX, endX);
    int y = ThreadLocalRandom.current().nextInt(startY, endY);
    return new Coord(x, y);
  }

  protected boolean isInsideRadarOrTrapZone() {
    return this.entity.getPos().getX() >= 5
        && this.entity.getPos().getX() < board.getWidth() - 3
        && this.entity.getPos().getY() >= 4
        && this.entity.getPos().getY() < board.getHeight() - 3;
  }

  protected List<Cell> getBadCells(Board board) {
    List<Cell> badCells = new ArrayList<>();
    for (Cell cell : board.getCells()) {
      if (isCellBad(cell)) {
        badCells.add(cell);
      }
    }
    return badCells;

  }

  protected boolean isCellBad(final Cell cell) {
    if (board.getMyTrapPos().isEmpty()) {
      return false;
    }
    Coord cellOptional = board.getMyTrapPos().parallelStream()
        .filter(trapCoord -> trapCoord.getX() == cell.getCoord().getX() && trapCoord.getY() == cell.getCoord().getY())
        .findFirst()
        .orElse(null);
    return cellOptional != null;
  }

  protected boolean isCellRadarFree(final Cell cell) {
    Coord cellOptional = board.getMyRadarPos().parallelStream()
        .filter(coord -> coord.getX() == cell.getCoord().getX() && coord.getY() == cell.getCoord().getY()).findFirst()
            .orElse(null);
    return cellOptional == null;
  }

  static boolean isInside(int circle_x, int circle_y, int rad, int x, int y) {
    if ((x - circle_x) * (x - circle_x) + (y - circle_y) * (y - circle_y) <= rad * rad) {
      return true;
    } else {
      return false;
    }
  }
}
