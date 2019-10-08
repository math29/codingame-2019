package com.player.orchestrator;

import com.player.behaviours.MinerBehaviour;
import com.player.behaviours.ScoutBehaviour;
import com.player.behaviours.SuicideBomberBehaviour;
import com.player.behaviours.ZombieBehaviour;
import com.player.model.Entity;
import com.player.model.History;

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
                continue;
            }

            // If we don't have a Scout
            if (this.getNumberOfScouts() == 0
                    // and we don't have much gold left
                    && board.getCells().stream()
                        .filter(cell -> cell.hasOre()
                                && !cell.hasPotentialEnemyTrap()
                                && !cell.hasAllyTrap(board))
                        .count() < board.getMyTeam().getNumberOfRobotAlive() * 2
                    // and we have a best match for our scout
                    && robot.getId() == getBestMatchNextScoutRobotId().orElse(robot.getId())) {
                this.behaviourMap.put(robot.getId(), new ScoutBehaviour(robot, board));
                continue;
            }

            // If we don't have a Bomber
            if (this.getNumberOfSuicideBombers() == 0
                    // and we have more than 2 robots alive
                    && this.board.getMyTeam().getRobots().stream().filter(Entity::isAlive).count() >= 2
                    // and the enemy has more than 2 robots alive
                    && this.board.getOpponentTeam().getRobots().stream().filter(Entity::isAlive).count() > 2) {
                this.behaviourMap.put(robot.getId(), new SuicideBomberBehaviour(robot, board));
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
                // If it's the 1st RADAR, it should be the closest to the coord of 1st RADAR
                if (History.getNumberOfTurns() == 0) {
                    distance = robot.get().getPos().distance(ScoutBehaviour.scoutCoord.get(0));
                }

                if (distance < minDistance) {
                    closestRobotIndex = Optional.of(robot.get().getId());
                    minDistance = distance;
                }
            }
        }
        return closestRobotIndex;
    }
}
