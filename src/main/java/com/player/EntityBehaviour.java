package com.player;

import java.util.Random;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
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
    int x = randomGenerator.nextInt(board.getHeight()) + 1;
    int y = randomGenerator.nextInt(board.getWidth()) + 1;

    return new Coord(x, y);
  }
}
