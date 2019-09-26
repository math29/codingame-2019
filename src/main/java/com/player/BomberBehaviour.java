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
  private final Entity robot;
  BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.robot = robot;
    this.board = board;
  }

  @Override Action getNextAction() {
    Coord randomCoord = getRandomSafeCoord();
    if (robot.isAtHeadquarters() && !robot.hasItem()) {
      return Action.request(EntityType.TRAP);
    } else if (robot.isAtHeadquarters() && robot.item.equals(EntityType.TRAP) && board.getCell(randomCoord).hasOre()) {
      return Action.move(randomCoord);
    } else if (robot.isAtHeadquarters() && robot.item.equals(EntityType.TRAP) && !board.getCell(randomCoord).hasOre()) {
      getNextAction();
    } else if (robot.item == EntityType.TRAP) {
      return Action.dig(robot.pos);
    } else {
      return Action.move(getCloserHeadQuarterCell().coord);
    }
    return null;
  }
}
