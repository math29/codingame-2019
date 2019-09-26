package com.player;

import java.util.Random;
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class BomberBehaviour extends EntityBehaviour {
  private final Random random;
  private final Entity robot;
  private final Board board;
  BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.robot = robot;
    this.board = board;
    this.random = new Random();
  }

  @Override Action getNextAction() {
    if (robot.pos.y == 0 && robot.item == EntityType.NOTHING) {
      return Action.request(EntityType.TRAP);
    } else if (robot.pos.y == 0 && robot.item == EntityType.TRAP) {
      return Action.move(getRandomSafeCoord());
    } else if (robot.item == EntityType.TRAP) {
      return Action.dig(getRandomSafeCoord());
    } else {
      return Action.move(new Coord(random.ints(0, (board.getHeight() -1)).findFirst().getAsInt(),0));
    }
  }
}
