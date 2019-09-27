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

  abstract void setRobotBehaviours();

  void initializeBehaviours(final Board board) {
    this.board = board;
    behaviourMap = new HashMap<>();
    for (final Entity robot : board.myTeam.robots) {
      if (robot.hasItem() && robot.item == EntityType.RADAR && robot.isAlive()) {
        this.behaviourMap.put(robot.id, new ScoutBehaviour(robot, board));
      } else if (robot.hasItem() && robot.item == EntityType.TRAP && robot.isAlive()) {
        this.behaviourMap.put(robot.id, new SuicideBomberBehaviour(robot, board));
      } else if (robot.hasItem() && robot.item == EntityType.AMADEUSIUM && robot.isAlive()) {
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

  protected int getNumberOfSuicideBombers() {
    return this.behaviourMap.values().stream().filter(entityBehaviour -> entityBehaviour instanceof SuicideBomberBehaviour)
        .toArray().length;
  }
}
