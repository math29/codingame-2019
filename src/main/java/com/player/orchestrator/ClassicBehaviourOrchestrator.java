package com.player.orchestrator;

import com.player.behaviours.BomberBehaviour;
import com.player.behaviours.ZombieBehaviour;
import com.player.behaviours.MinerBehaviour;
import com.player.behaviours.ScoutBehaviour;
import com.player.behaviours.SuicideBomberBehaviour;
import com.player.model.Entity;

import java.util.Optional;

public class ClassicBehaviourOrchestrator extends BehaviourOrchestrator {

    @Override
    public void setRobotBehaviours() {

        for (final Entity robot : board.getMyTeam().getRobots()) {
            if (behaviourMap.get(robot.getId()) != null) {
                continue;
            }

            if (!robot.isAlive()) {
                this.behaviourMap.put(robot.getId(), new ZombieBehaviour(robot, board));
            }

            // If we don't have a Scout
            if (this.getNumberOfScouts() == 0
                    // and we waited for 5 turns
                    && board.getMyRadarCooldown() == 0
                    // and we don't have much gold left
                    && board.getCells().stream()
                        .filter(cell -> cell.hasOre() && !cell.isHole() && !cell.hasAllyTrap(board))
                        .count() < board.getMyTeam().getNumberOfRobotAlive()
                    // ...?
                    && robot.getId() == getBestMatchNextScoutRobotId().orElse(robot.getId())) {
                this.behaviourMap.put(robot.getId(), new ScoutBehaviour(robot, board));
                continue;
            }

            // If we don't have a Bomber
            if (this.getNumberOfBombers() == 0
                    // and we waited for 5 turns
                    && board.getMyTrapCooldown() == 0
                    // and we have more than 2 robots alive
                    && this.board.getMyTeam().getRobots().stream().filter(Entity::isAlive).count() > 2
                    // and the enemy has more than 2 robots alive
                    && this.board.getOpponentTeam().getRobots().stream().filter(Entity::isAlive).count() >= 2) {
                this.behaviourMap.put(robot.getId(), new BomberBehaviour(robot, board));
                continue;
            }

            // Otherwise, it should be a Scout
            this.behaviourMap.put(robot.getId(), new MinerBehaviour(robot, board));
        }
    }

    private Optional<Integer> getBestMatchNextScoutRobotId() {
        int minDistance = board.getWidth();
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
