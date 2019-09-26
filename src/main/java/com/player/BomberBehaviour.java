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

    Cell closestOreCell = this.getCloserOreCell();

    if (entity.item == EntityType.TRAP
        && (closestOreCell == null || closestOreCell.coord.distance(entity.pos) <= 2)
            && !isCellBad(board.getCell(entity.pos))) {
      return returnAction(Action.dig(closestOreCell.coord));
    }

    // move
    if (entity.item == EntityType.TRAP) {
      return returnAction(Action.move(closestOreCell.coord));
    }

    // Return to headquarters for the new RADAR
    return returnAction(Action.move(getCloserHeadQuarterCell().coord));
  }
}
