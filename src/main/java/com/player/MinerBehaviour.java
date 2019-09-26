package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class MinerBehaviour extends EntityBehaviour {

  MinerBehaviour(final Entity entity, final Board board) {
    super(entity, board);
  }

  @Override Action getNextAction() {
    Coord targetCell = this.getCloserOreCell().coord;
    if (targetCell.distance(entity.pos) == 1) {
      return Action.dig(targetCell);
    }
    return Action.move(targetCell);
  }
}
