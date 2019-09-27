package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

import java.util.Optional;

class MinerBehaviour extends EntityBehaviour {

  MinerBehaviour(final Entity entity, final Board board) {
    super(entity, board);
    this.NAME = "Miner";
  }

  @Override Action getNextAction() {
    Coord targetCoord;
    // Miner has cristal on him else go to mine
    if (entity.hasItem()) {
      return returnAction(Action.move(this.getCloserHeadQuarterCell().coord));
    }

    // Miner is looking for christal
    Optional<Cell> closestOre = this.getClosestOreCell();
    if (!closestOre.isPresent()) {

    }
    else {
      targetCoord = this.getClosestOreCell().coord;
      if (targetCoord.distance(entity.pos) <= 2) {
        board.getCell(targetCoord).ore = board.getCell(targetCoord).ore - 1;
        return returnAction(Action.dig(targetCoord));
      }
    }
    return returnAction(Action.move(targetCoord));
  }

  private Boolean isCellTooCloseToAlly() {

  }
}
