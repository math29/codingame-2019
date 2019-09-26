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

public class BomberBehaviour extends EntityBehaviour {
  private final Random random;
  private final Entity robot;
  private final Board board;
  public BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.robot = robot;
    this.board = board;
    this.random = new Random();
  }

  @Override Action getNextAction() {
    if (robot.pos.y == 0 && robot.item == EntityType.NOTHING) {
      return Action.request(EntityType.TRAP);
    } else if (robot.pos.y == 0 && robot.item == EntityType.TRAP) {
      int x = random.ints(0, (board.getHeight() + 1)).findFirst().getAsInt();
      int y = random.ints(0, (board.getWidth() + 1)).findFirst().getAsInt();
      return Action.move(new Coord(x, y));
    } else if (robot.item == EntityType.TRAP
        && (robot.pos.x >= 0 && robot.pos.x < board.width)
        && (robot.pos.y >= 0 && robot.pos.y < board.height)) {
      int xToDig = robot.pos.x == (board.width-1) ? robot.pos.x-1 : robot.pos.x+1;
      int yToDig = robot.pos.y == (board.height-1) ? robot.pos.x-1 : robot.pos.x+1;
      return Action.dig(new Coord(xToDig, yToDig));
    } else {
      return Action.move(new Coord(random.ints(0, (board.getHeight() -1)).findFirst().getAsInt(),0));
    }
  }
}
