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
import java.util.stream.Collectors;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

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
        switch (index) {
        case 0:
          robot.action = new ScoutBehaviour(robot, board).getNextAction();
          robot.action.message = "Scout Robot";
          break;
        case 1:
          robot.action = new BomberBehaviour(robot, board).getNextAction();
          robot.action.message = "Bomber Robot";
          break;
        case 2:
        case 3:
        case 4:
          robot.action = new MinerBehaviour(robot, board).getNextAction();
          robot.action.message = "Miner Robot";
          break;
        }
        index++;
      }

      // Send your actions for this turn
      for (Entity robot : board.myTeam.robots) {
        System.out.println(robot.action);
      }

      // Run Behaviour assignements
//      BehaviourOrchestrator behaviourOrchestrator = new ClassicBehaviourOrchestrator(board);
//      behaviourOrchestrator.setRobotBehaviours();
//      for (EntityBehaviour behaviour : behaviourOrchestrator.behaviourMap.values()) {
//        System.out.println(behaviour.getNextAction());
//      }
    }
  }
}
