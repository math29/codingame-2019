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
    Coord targetCoord;
    // Miner has cristal on him else go to mine
    if (entity.hasItem()) {
      targetCoord = this.getCloserHeadQuarterCell().coord;
    } else {
      targetCoord = this.getCloserOreCell().coord;
      if (targetCoord.distance(entity.pos) <= 1) {
        return Action.dig(targetCoord);
      }
    }
    return Action.move(targetCoord);
    // @TODO: Set If no ore anymore, set Entity taken as empty
  }
}
