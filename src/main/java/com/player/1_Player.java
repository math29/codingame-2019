package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

import java.util.*;
import java.io.*;
import java.math.*;
import java.util.stream.Stream;

/**
 * Deliver more ore to hq (left side of the map) than your opponent. Use radars to find ore but beware of traps!
 **/

class Player {

  public static void main(String args[]) {
    new Player().run();
  }

  final Scanner in = new Scanner(System.in);

  void run() {
    // Parse initial conditions
    Board board = new Board(in);

    while (true) {
      // Parse current state of the game
      board.update(in);

      // Insert your strategy here
      Integer index = 0;
      for (Entity robot : board.myTeam.robots) {
        if (index == 0) {
          if (robot.pos.y == 0 && robot.item == EntityType.NOTHING) {
            robot.action = Action.request(EntityType.RADAR);
          } else if (robot.pos.y == 0 && robot.item == EntityType.RADAR) {
            Random r = new Random();
            int x = r.ints(0, (board.getHeight() + 1)).findFirst().getAsInt();
            int y = r.ints(0, (board.getWidth() + 1)).findFirst().getAsInt();
            robot.action = Action.move(new Coord(x, y));
          } else if (robot.item == EntityType.RADAR
                  && (robot.pos.x >= 0 && robot.pos.x < board.width)
                  && (robot.pos.y >= 0 && robot.pos.y < board.height)) {
            int xToDig = robot.pos.x == (board.width-1) ? robot.pos.x-1 : robot.pos.x+1;
            int yToDig = robot.pos.y == (board.height-1) ? robot.pos.x-1 : robot.pos.x+1;
            robot.action = Action.dig(new Coord(xToDig, yToDig));
          } else {
            robot.action = Action.move(new Coord(0,0));
          }
          robot.action.message = "Java Starter";
        } else if (index == 1) {
          EntityBehaviour behaviour = new MinerBehaviour(robot, board);
          robot.action = behaviour.getNextAction();
          robot.action.message = "Miner Robot";
        } else {
          robot.action = Action.none();
          robot.action.message = "Java Starter";
        }
        index++;
      }

      // Send your actions for this turn
      for (Entity robot : board.myTeam.robots) {
        System.out.println(robot.action);
      }
    }
  }
}
