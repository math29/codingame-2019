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

  private static final String NAME = "Bomber";

  BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.board = board;
  }

  @Override Action getNextAction() {
    Coord randomCoord = getRandomSafeCoord();
    if (entity.isAtHeadquarters() && !entity.hasItem()) {
      return Action.request(EntityType.TRAP);
    } else if (entity.isAtHeadquarters() && entity.item.equals(EntityType.TRAP)) {
      return Action.move(randomCoord);
    } else if (entity.item == EntityType.TRAP) {
      return Action.dig(entity.pos);
    } else {
      return Action.move(getCloserHeadQuarterCell().coord);
    }
  }
}
