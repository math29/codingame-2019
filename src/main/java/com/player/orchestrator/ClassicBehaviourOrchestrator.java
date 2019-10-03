package com.player.orchestrator;

import com.player.behaviours.LazyBehaviour;
import com.player.behaviours.MinerBehaviour;
import com.player.behaviours.ScoutBehaviour;
import com.player.behaviours.SuicideBomberBehaviour;
import com.player.model.Entity;

import java.util.Optional;

public class ClassicBehaviourOrchestrator extends BehaviourOrchestrator {

    @Override
    public void setRobotBehaviours() {
        for (final Entity robot : board.getMyTeam().getRobots()) {
            if (this.behaviourMap.get(robot.getId()) == null) {
                if (robot.isAlive()) {
                    if (this.getNumberOfScouts() < 1
                            && board.getCells().stream().filter(cell -> cell.hasOre()
                            && !cell.isHole()
                            && !cell.hasAllyTrap(board)).count() < board.getMyTeam().getNumberOfRobotAlive()
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
        int minDistance = 50;
        Optional<Integer> closestRobotIndex = Optional.empty();
        for (final Integer availableRobotIndex : getAvailableRobotIndexes()) {
            Optional<Entity> robot = this.board.getMyTeam().getRobot(availableRobotIndex);
            if (robot.isPresent()) {
                int distance = robot.get().getDistanceFromClosestHeadQuarterCell(board);
                if (distance < minDistance) {
                    closestRobotIndex = Optional.of(robot.get().getId());
                    minDistance = distance;
                }
            }
        }
        return closestRobotIndex;
    }
}
