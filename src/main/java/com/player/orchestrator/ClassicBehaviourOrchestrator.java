package com.player.orchestrator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.player.behaviours.LazyBehaviour;
import com.player.behaviours.MinerBehaviour;
import com.player.behaviours.ScoutBehaviour;
import com.player.behaviours.SuicideBomberBehaviour;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;

public class ClassicBehaviourOrchestrator extends BehaviourOrchestrator {

  @Override public void setRobotBehaviours() {
    for (final Entity robot : board.getMyTeam().getRobots()) {
      if (this.behaviourMap.get(robot.getId()) == null) {
        if (robot.isAlive()) {
          if (this.getNumberOfScouts() < 1
              && board.getCells().stream().filter(cell -> cell.hasOre() && !cell.hasHole() && !isCellBad(cell)).count()
              < board.getMyTeam()
              .getNumberOfRobotAlive()
              && robot.getId() == getBestMatchNextScoutRobotId().orElse(robot.getId())) {
            this.behaviourMap.put(robot.getId(), new ScoutBehaviour(robot, board));
          } else if (robot.isAlive() && this.getNumberOfSuicideBombers() < 1
              && this.board.getMyTeam().getRobots().stream().filter(Entity::isAlive).count() >= 2
              && this.board.getOpponentTeam().getRobots().stream().filter(Entity::isAlive).count() >= 2) {
            this.behaviourMap.put(robot.getId(), new SuicideBomberBehaviour(robot, board));
          } else {
            this.behaviourMap.put(robot.getId(), new MinerBehaviour(robot, board));
          }
        } else {
          this.behaviourMap.put(robot.getId(), new LazyBehaviour(robot, board));
        }
      }
    }
  }

  private Optional<Integer> getBestMatchNextScoutRobotId() {
    List<Integer> availableRobots = this.behaviourMap.keySet().stream().filter(key -> behaviourMap.get(key) == null)
        .collect(Collectors.toList());
    int minDistance = 50;
    Optional<Integer> closestRobotIndex = Optional.empty();
    for (final Integer availableRobotIndex : availableRobots) {
      Optional<Entity> robot = this.board.getMyTeam().getRobots().stream()
          .filter(entity -> entity.getId() == availableRobotIndex)
          .findFirst();
      if (robot.isPresent()) {
        for (final Cell cell : board.getHeadQuarterCells()) {
          int distance = cell.getCoord().distance(robot.get().getPos());
          if (distance < minDistance) {
            closestRobotIndex = Optional.of(robot.get().getId());
            minDistance = distance;
          }
        }
      }
    }
    return closestRobotIndex;
  }

  private boolean isCellBad(final Cell cell) {
    if (board.getMyTrapPos().isEmpty()) {
      return false;
    }
    Coord cellOptional = board.getMyTrapPos().parallelStream()
        .filter(trapCoord -> trapCoord.getX() == cell.getCoord().getX() && trapCoord.getY() == cell.getCoord().getY())
        .findFirst()
        .orElse(null);
    return cellOptional != null;
  }
}
