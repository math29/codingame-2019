package com.player.behaviours;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;
import com.player.model.EntityType;

public class BomberBehaviour extends EntityBehaviour {

  BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.NAME = "Bomber";
  }

  @Override public Action getNextAction() {
    // If Bomber is at the headquarters and carries nothing, take TRAP
    if (entity.isAtHeadquarters() && entity.getItem() == EntityType.NOTHING) {
      return returnAction(Action.request(EntityType.TRAP));
    }

    Cell closestOreCell = this.getCloserJuicyOreCell();

    if (entity.getItem() == EntityType.TRAP
        && closestOreCell.getCoord().distance(entity.getPos()) <= 2
        && !board.getCell(closestOreCell.getCoord()).hasAllyTrap(board)) {
      return returnAction(Action.dig(closestOreCell.getCoord()));
    }

    // move
    if (entity.getItem() == EntityType.TRAP) {
      return returnAction(Action.move(closestOreCell.getCoord()));
    }

    // Return to headquarters for the new RADAR
    return returnAction(Action.move(getCloserHeadQuarterCell().getCoord()));
  }

  private Cell getCloserJuicyOreCell() {
    Cell closerCell = null;
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.getOre() > 1 && distance < minDistance && !cell.hasAllyTrap(board) && !cell.isHole()) {
        closerCell = cell;
        minDistance = distance;
      }
    }
    if (closerCell == null) {
      return this.getClosestOreCell().orElseGet(() -> board.getCell(new Coord(0, 0)));
    }
    return closerCell;
  }
}
