package com.player;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

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
    Coord coord = getRandomCoord();
    if(!isCellBad(board.getCell(coord))){
      return coord;
    } else {
      return getRandomSafeCoord();
    }
  }

  private Coord getRandomCoord() {
    Random randomGenerator = new Random();
    int x = randomGenerator.nextInt(board.getHeight());
    int y = randomGenerator.nextInt(board.getWidth());
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

  private boolean isCellBad(final Cell cell) {
    return !cell.known && board.myTrapPos.contains(cell);
  }

}
