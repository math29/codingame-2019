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
import java.util.ArrayList;
import java.util.List;

/**
 * Deliver more ore to hq (left side of the map) than your opponent. Use radars to find ore but beware of traps!
 **/

class Player {

  public static void main(String args[]) {
    new Player().run();
  }

  final Scanner in = new Scanner(System.in);

  BehaviourOrchestrator behaviourOrchestrator = new ClassicBehaviourOrchestrator();

  void run() {
    // Parse initial conditions
    Board board = new Board(in);

    while (true) {
      // Parse current state of the game
      board.update(in);
      behaviourOrchestrator.initializeBehaviours(board);
      behaviourOrchestrator.setRobotBehaviours();
      for (EntityBehaviour behaviour : behaviourOrchestrator.behaviourMap.values()) {
        System.out.println(behaviour.getNextAction());
      }
    }
  }
}
