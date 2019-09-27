package com.player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class ClassicBehaviourOrchestrator extends BehaviourOrchestrator {

  @Override void setRobotBehaviours() {
    for (final Entity robot : board.myTeam.robots) {
      if (this.behaviourMap.get(robot.id) == null) {
        if (robot.isAlive()
            && this.getNumberOfScouts() < 1
            && board.getCells().stream().filter(cell -> cell.hasOre() && !isCellBad(cell)).count() < board.myTeam
            .getNumberOfRobotAlive()
            && robot.id == getBestMatchNextScoutRobotId().orElse(robot.id)) {
          this.behaviourMap.put(robot.id, new ScoutBehaviour(robot, board));
        } else if (robot.isAlive() && this.getNumberOfSuicideBombers() < 1
            && this.board.myTeam.robots.stream().filter(Entity::isAlive).count() >= 2
            && this.board.opponentTeam.robots.stream().filter(Entity::isAlive).count() >= 2 ) {
          this.behaviourMap.put(robot.id, new SuicideBomberBehaviour(robot, board));
        } else {
          this.behaviourMap.put(robot.id, new MinerBehaviour(robot, board));
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
      Optional<Entity> robot = this.board.myTeam.robots.stream().filter(entity -> entity.id == availableRobotIndex)
          .findFirst();
      if (robot.isPresent()) {
        for (final Cell cell : board.getHeadQuarterCells()) {
          int distance = cell.coord.distance(robot.get().pos);
          if (distance < minDistance) {
            closestRobotIndex = Optional.of(robot.get().id);
            minDistance = distance;
          }
        }
      }
    }
    return closestRobotIndex;
  }

  private boolean isCellBad(final Cell cell) {
    if(board.myTrapPos.isEmpty()){
      return false;
    }
    Coord cellOptional = board.myTrapPos.parallelStream()
        .filter(trapCoord -> trapCoord.x == cell.coord.x && trapCoord.y == cell.coord.y).findFirst()
        .orElse(null);
    return cellOptional != null;
  }
}
