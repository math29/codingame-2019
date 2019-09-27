package com.player;

import java.util.ArrayList;
import java.util.List;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class SuicideBomberBehaviour extends EntityBehaviour {

  SuicideBomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.NAME = "Friendly Bomber";
  }

  @Override Action getNextAction() {

    System.err.println("enemiesInRange(): " + enemiesInRange().size());
    System.err.println("alliesInRange(): " + alliesInRange().size());
    if (enemiesInRange().size() > alliesInRange().size()) {
      Coord coordToBomb = getClosestExplodingCell().coord;
      return returnAction(Action.dig(coordToBomb));
    }

    if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
      return returnAction(Action.request(EntityType.TRAP));
    }

    if (entity.item == EntityType.TRAP) {
      for (int i = 1; i < board.height - 2; i++) {
        if (!isCellBad(board.getCell(new Coord(1, i)))) {
          return returnAction(Action.dig(new Coord(1, i)));
        }
      }
    }

    // Return to headquarters for the new TRAP
    return returnAction(Action.move(getCloserHeadQuarterCell().coord));
  }

  private List<Entity> enemiesInRange() {
    List<Entity> enemyCloseToBombs = new ArrayList<>();

    board.opponentTeam.robots.forEach(enemyRobot -> {
      if (isCloseToBombs(enemyRobot)) {
        enemyCloseToBombs.add(enemyRobot);
      }
    });

    return enemyCloseToBombs;
  }

  private List<Entity> alliesInRange() {
    List<Entity> alliesInRange = new ArrayList<>();

    board.myTeam.robots.forEach(robot -> {
      if (isCloseToBombs(robot)) {
        alliesInRange.add(robot);
      }
    });

    return alliesInRange;
  }

  private boolean isCloseToBombs(final Entity robot) {
    return board.myTrapPos.stream().anyMatch(trapPosition -> (
        (trapPosition.x == robot.pos.x && trapPosition.y == robot.pos.y)
            || (trapPosition.x == robot.pos.x && (trapPosition.y == robot.pos.y - 1
            || trapPosition.y == robot.pos.y + 1))
            || (trapPosition.y == robot.pos.y && (trapPosition.x == robot.pos.x - 1
            || trapPosition.x == robot.pos.x + 1))
    ));
  }


  private Cell getClosestExplodingCell() {
    Cell closerCell = null;
    int minDistance = 10;
    for (final Coord myTrapPo : this.board.myTrapPos) {
      int distance = myTrapPo.distance(entity.pos);
      if (distance < minDistance) {
        minDistance = distance;
        closerCell = board.getCell(myTrapPo);
      }
    }
    return closerCell;
  }
}
