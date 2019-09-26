package com.player;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
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

  EntityBehaviour(final Entity entity, final Board board) {
    this.entity = entity;
    this.board = board;
  }

  abstract Action getNextAction();

  Cell getCloserOreCell() {
    Cell closerCell = board.getCell(this.getRandomSafeCoord());
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.coord.distance(this.entity.pos);
      if (cell.hasOre() && distance < minDistance) {
        closerCell = cell;
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

  Coord getRandomSafeCoord() {
    return getRandomSafeCoord(0, board.getWidth(), 0, board.getHeight());
  }

  Coord getRandomSafeCoord(int startX, int endX, int startY, int endY) {
    Coord coord = getRandomCoord(startX, endX, startY, endY);
    if(!isCellBad(board.getCell(coord))){
      return coord;
    } else {
      return getRandomSafeCoord();
    }
  }

  Coord getRandomSafeAndRadarFreeCoord(int startX, int endX, int startY, int endY) {
    Coord coord = getRandomCoord(startX, endX, startY, endY);
    if(!isCellRadarFree(board.getCell(coord))
            && !isCellBad(board.getCell(coord))){
      return coord;
    } else {
      return getRandomSafeCoord();
    }
  }

  private Coord getRandomCoord(int startX, int endX, int startY, int endY) {
    int x = ThreadLocalRandom.current().nextInt(startX, endX);
    int y = ThreadLocalRandom.current().nextInt(startY, endY);
    return new Coord(x, y);
  }

  protected List<Cell> getBadCells(Board board){
    List<Cell> badCells = new ArrayList<>();
    for (Cell cell: board.getCells()) {
      if(isCellBad(cell)){
        badCells.add(cell);
      }
    }
    return badCells;

  }

  protected boolean isCellBad(final Cell cell) {
    return !cell.known && board.myTrapPos.contains(cell);
  }

  private boolean isCellRadarFree(final Cell cell) {
    return board.myRadarPos.contains(cell);
  }
}
