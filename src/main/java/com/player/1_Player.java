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
          ScoutBehaviour scout = new ScoutBehaviour(robot, board);
          robot.action = scout.getNextAction();
        } else if (index == 1) {
          EntityBehaviour behaviour = new MinerBehaviour(robot, board);
          robot.action = behaviour.getNextAction();
          robot.action.message = "Miner Robot";
        } else if (index == 2) {
          EntityBehaviour behaviour = new BomberBehaviour(robot, board);
          robot.action = behaviour.getNextAction();
          robot.action.message = "Bomber Robot";
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
