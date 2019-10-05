package com.player;

import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;

import com.player.behaviours.EntityBehaviour;
import com.player.model.Board;
import com.player.model.History;
import com.player.orchestrator.BehaviourOrchestrator;
import com.player.orchestrator.ClassicBehaviourOrchestrator;

/**
 * Deliver more ore to hq (left side of the map) than your opponent. Use radars to find ore but beware of traps!
 **/

class Player {

  public static void main(String args[]) {
    new Player().run();
  }

  final Scanner in = new Scanner(System.in);

  private BehaviourOrchestrator behaviourOrchestrator = new ClassicBehaviourOrchestrator();

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
      try {
        History.recordNewTurn(board.clone());
      } catch (CloneNotSupportedException e) {
        System.err.println("clone failed: " + e.getMessage());
      }
    }
  }
}
