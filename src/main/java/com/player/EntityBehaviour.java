package com.player;

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

  protected Cell getCloserOreCell() {
    Cell closerCell = board.getCell(new Coord(0, 0));
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = closerCell.coord.distance(this.entity.pos);
      if (closerCell.hasOre() && distance < minDistance) {
        closerCell = cell;
        minDistance = distance;
      }
    }
    return closerCell;
  }
}
