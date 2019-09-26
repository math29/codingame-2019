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
    Random randomGenerator = new Random();
    int x = randomGenerator.nextInt(board.getWidth());
    int y = randomGenerator.nextInt(board.getHeight());

    Coord coord = new Coord(x, y);
    while (!isCellBad(board.getCell(coord))){
      return coord;
    }
    return null;
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
