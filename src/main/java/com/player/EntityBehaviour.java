package com.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

abstract class EntityBehaviour {

  protected Entity entity;
  protected Board board;
  protected String NAME;

  EntityBehaviour(final Entity entity, final Board board) {
    this.entity = entity;
    this.board = board;
  }

  abstract Action getNextAction();

  Action returnAction(final Action action) {
    return action.withMessage(this.NAME);
  }

  // Returns closest Ore Cell, null if none
  Optional<Cell> getClosestOreCell() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.coord.distance(this.entity.pos);
      if (cell.hasOre() && distance < minDistance && !isCellBad(cell)) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }


  Cell getCloserHeadQuarterCell() {
    Cell closerCell = board.getCell(new Coord(0, 0));
    int minDistance = 50;
    for (final Cell cell : board.getHeadQuarterCells()) {
      int distance = cell.coord.distance(this.entity.pos);
      if (distance < minDistance) {
        closerCell = cell;
        minDistance = distance;
      }
    }
    return closerCell;
  }

  Coord getRandomSafeCoord(int startX, int endX, int startY, int endY) {
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

  boolean isInsideRadarOrTrapZone() {
    return this.entity.pos.x >= 5
            && this.entity.pos.x < board.getWidth() - 3
            && this.entity.pos.y >= 4
            && this.entity.pos.y < board.getHeight() - 3;
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
    if(board.myTrapPos.isEmpty()){
      return false;
    }
    Coord cellOptional = board.myTrapPos.parallelStream()
        .filter(trapCoord -> trapCoord.x == cell.coord.x && trapCoord.y == cell.coord.y).findFirst()
        .orElse(null);
    return cellOptional != null;
  }

  boolean isCellRadarFree(final Cell cell) {
    Coord cellOptional = board.myRadarPos.parallelStream()
            .filter(coord -> coord.x == cell.coord.x && coord.y == cell.coord.y).findFirst()
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
