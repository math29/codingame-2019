package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

class SuicideBomberBehaviour extends EntityBehaviour {

  SuicideBomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.NAME = "Friendly Bomber";
  }

  @Override Action getNextAction() {

    if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
      return returnAction(Action.request(EntityType.TRAP));
    }

    if (enemiesInRange().size() >= 3) {
      Coord coordToBomb = getClosestExplodingCell().coord;
      return returnAction(Action.dig(coordToBomb));
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
      if (enemyCloseToBombs(enemyRobot)) {
        enemyCloseToBombs.add(enemyRobot);
      }
    });

    return enemyCloseToBombs;

  }

  private boolean enemyCloseToBombs(final Entity robot) {
    return  board.myTrapPos.stream().anyMatch(trapPosition -> (trapPosition.x == robot.pos.x + 1 || trapPosition.x == robot.pos.x - 1 || trapPosition.x == robot.pos.x) && (
        trapPosition.y == robot.pos.y + 1 || trapPosition.y == robot.pos.y - 1 || trapPosition.y == robot.pos.y));

  }


  private Cell getClosestExplodingCell() {
    Cell closerCell = null;
    int minDistance = 3;
    for (int i = 1; i < this.entity.pos.x+2; i++) {
      for (final Cell cell : board.getCellsOnColumn(this.entity.pos.x)) {
        int distance = cell.coord.distance(this.entity.pos);
        if (isCellBad(cell) && distance < minDistance && cell.hole) {
          closerCell = cell;
          minDistance = distance;
        }
      }
    }
    if (closerCell == null) {
      return board.getCell(new Coord(1, 1));
    }
    return closerCell;
  }
}
