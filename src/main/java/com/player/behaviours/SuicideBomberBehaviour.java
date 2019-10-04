package com.player.behaviours;

import java.util.ArrayList;
import java.util.List;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;
import com.player.model.EntityType;

public class SuicideBomberBehaviour extends EntityBehaviour {

  public SuicideBomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.NAME = "Friendly Bomber";
  }

  @Override public Action getNextAction() {
    // If there are more enemies than allies in a range of a TRAP(s), suicide for good!
    if (enemiesInRange().size() > alliesInRange().size()) {
      Coord coordToBomb = getClosestExplodingCell().getCoord();
      return returnAction(Action.dig(coordToBomb));
    }

    // If Bomber is at the headquarters and carries nothing, take TRAP
    if (entity.isAtHeadquarters() && entity.getItem() == EntityType.NOTHING) {
      return returnAction(Action.request(EntityType.TRAP));
    }

    // Bomber should put a TRAP
    if (entity.getItem() == EntityType.TRAP) {
      for (int i = 5; i < board.getHeight() - 2; i++) {
        if (!board.getCell(new Coord(1, i)).hasAllyTrap(board)) {
          return returnAction(Action.dig(new Coord(1, i)));
        }
      }
    }

    // Return to headquarters for the new TRAP
    return returnAction(Action.move(getCloserHeadQuarterCell().getCoord()));
  }

  private List<Entity> enemiesInRange() {
    List<Entity> enemyCloseToBombs = new ArrayList<>();

    board.getOpponentTeam().getRobots().forEach(enemyRobot -> {
      if (isCloseToBombs(enemyRobot)) {
        enemyCloseToBombs.add(enemyRobot);
      }
    });

    return enemyCloseToBombs;
  }

  private List<Entity> alliesInRange() {
    List<Entity> alliesInRange = new ArrayList<>();

    board.getMyTeam().getRobots().forEach(robot -> {
      if (isCloseToBombs(robot)) {
        alliesInRange.add(robot);
      }
    });

    return alliesInRange;
  }

  private boolean isCloseToBombs(final Entity robot) {
    return board.getMyTrapPos().stream().anyMatch(trapPosition -> (
        (trapPosition.getX() == robot.getPos().getX() && trapPosition.getY() == robot.getPos().getY())
            || (trapPosition.getX() == robot.getPos().getX() && (trapPosition.getY() == robot.getPos().getY() - 1
                || trapPosition.getY() == robot.getPos().getY() + 1))
            || (trapPosition.getY() == robot.getPos().getY() && (trapPosition.getX() == robot.getPos().getX() - 1
                || trapPosition.getX() == robot.getPos().getX() + 1))
    ));
  }

  private Cell getClosestExplodingCell() {
    Cell closerCell = null;

    int minDistance = board.getWidth();
    for (final Coord myTrapPos : this.board.getMyTrapPos()) {
      int distance = myTrapPos.distance(entity.getPos());
      if (distance < minDistance) {
        minDistance = distance;
        closerCell = board.getCell(myTrapPos);
      }
    }

    return closerCell;
  }
}
