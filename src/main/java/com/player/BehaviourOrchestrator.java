package com.player;

import java.util.HashMap;
import java.util.Map;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

abstract class BehaviourOrchestrator {

  Map<Integer, EntityBehaviour> behaviourMap;

  protected Board board;

  BehaviourOrchestrator(final Board board) {
    this.board = board;
    this.initializeBehaviours();
  }

  abstract void setRobotBehaviours();

  private void initializeBehaviours() {
    behaviourMap = new HashMap<>();
    for (final Entity robot : board.myTeam.robots) {
      if (robot.hasItem() && robot.item == EntityType.RADAR) {
        this.behaviourMap.put(robot.id, new ScoutBehaviour(robot, board));
      } else if (robot.hasItem() && robot.item == EntityType.TRAP) {
        this.behaviourMap.put(robot.id, new BomberBehaviour(robot, board));
      } else if (robot.hasItem() && robot.item == EntityType.AMADEUSIUM) {
        this.behaviourMap.put(robot.id, new MinerBehaviour(robot, board));
      } else {
        this.behaviourMap.put(robot.id, null);
      }
    }
  }

  protected int getNumberOfMiners() {
    return this.behaviourMap.values().stream().filter(entityBehaviour -> entityBehaviour instanceof MinerBehaviour)
        .toArray().length;
  }

  protected int getNumberOfScouts() {
    return this.behaviourMap.values().stream().filter(entityBehaviour -> entityBehaviour instanceof ScoutBehaviour)
        .toArray().length;
  }

  protected int getNumberOfBombers() {
    return this.behaviourMap.values().stream().filter(entityBehaviour -> entityBehaviour instanceof BomberBehaviour)
        .toArray().length;
  }
}
