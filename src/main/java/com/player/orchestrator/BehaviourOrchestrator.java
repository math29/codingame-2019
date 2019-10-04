package com.player.orchestrator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.player.behaviours.BomberBehaviour;
import com.player.behaviours.EntityBehaviour;
import com.player.behaviours.MinerBehaviour;
import com.player.behaviours.ScoutBehaviour;
import com.player.behaviours.SuicideBomberBehaviour;
import com.player.model.Board;
import com.player.model.Entity;
import com.player.model.EntityType;

public abstract class BehaviourOrchestrator {

  public Map<Integer, EntityBehaviour> behaviourMap;

  protected Board board;

  public abstract void setRobotBehaviours();

  public void initializeBehaviours(final Board board) {
    this.board = board;
    behaviourMap = new HashMap<>();
    for (final Entity robot : board.getMyTeam().getRobots()) {
      if (robot.hasItem() && robot.getItem() == EntityType.RADAR && robot.isAlive()) {
        this.behaviourMap.put(robot.getId(), new ScoutBehaviour(robot, board));
      } else if (robot.hasItem() && robot.getItem() == EntityType.TRAP && robot.isAlive()) {
        this.behaviourMap.put(robot.getId(), new SuicideBomberBehaviour(robot, board));
      } else if (robot.hasItem() && robot.getItem() == EntityType.AMADEUSIUM && robot.isAlive()) {
        this.behaviourMap.put(robot.getId(), new MinerBehaviour(robot, board));
      } else {
        this.behaviourMap.put(robot.getId(), null);
      }
    }
  }

  List<Integer> getAvailableRobotIndexes() {
    return this.behaviourMap.keySet().stream()
            .filter(key -> behaviourMap.get(key) == null)
            .collect(Collectors.toList());
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
