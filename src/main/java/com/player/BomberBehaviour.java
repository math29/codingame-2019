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
    Coord randomCoord = getRandomSafeCoord(0, board.getWidth(), 0, board.getHeight());
    if (entity.isAtHeadquarters() && !entity.hasItem()) {
      return Action.request(EntityType.TRAP);
    } else if (entity.isAtHeadquarters() && entity.item.equals(EntityType.TRAP) && board.getCell(randomCoord).hasOre()) {
      return Action.move(randomCoord);
    } else if (entity.isAtHeadquarters() && entity.item.equals(EntityType.TRAP) && !board.getCell(randomCoord).hasOre()) {
      getNextAction();
    } else if (entity.item == EntityType.TRAP) {
      return Action.dig(entity.pos).withMessage(NAME);
    } else {
      return Action.move(getCloserHeadQuarterCell().coord).withMessage(NAME);
    }
    return null;
  }
}
