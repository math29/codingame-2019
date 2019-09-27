package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class BomberBehaviour extends EntityBehaviour {

  BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.NAME = "Bomber";
  }

  @Override Action getNextAction() {
    // If Bomber is at the headquarters and carries nothing, take TRAP
    if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
      return returnAction(Action.request(EntityType.TRAP));
    }

    Cell closestOreCell = this.getCloserJuicyOreCell();

    if (entity.item == EntityType.TRAP
        && closestOreCell.coord.distance(entity.pos) <= 2
        && !isCellBad(board.getCell(closestOreCell.coord))) {
      return returnAction(Action.dig(closestOreCell.coord));
    }

    // move
    if (entity.item == EntityType.TRAP) {
      return returnAction(Action.move(closestOreCell.coord));
    }

    // Return to headquarters for the new RADAR
    return returnAction(Action.move(getCloserHeadQuarterCell().coord));
  }

  private Cell getCloserJuicyOreCell() {
    Cell closerCell = null;
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.coord.distance(this.entity.pos);
      if (cell.ore > 1 && distance < minDistance && !isCellBad(cell) && !cell.hole) {
        closerCell = cell;
        minDistance = distance;
      }
    }
    if (closerCell == null) {
      return this.getClosestOreCell().orElseGet(() -> board.getCell(new Coord(0, 0)));
    }
    return closerCell;
  }
}
