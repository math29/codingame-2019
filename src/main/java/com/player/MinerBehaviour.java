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
    return Action.move(this.getCloserOreCell().coord);
  }
}
