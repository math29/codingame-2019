package com.player.behaviours;

import java.util.Optional;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;

public class MinerBehaviour extends EntityBehaviour {

  public MinerBehaviour(final Entity entity, final Board board) {
    super(entity, board);
    this.NAME = "Miner";
  }

  @Override public Action getNextAction() {
    // Miner has cristal on him, bring it to headquarters
    if (entity.hasItem()) {
      return returnAction(Action.move(this.getCloserHeadQuarterCell().getCoord()));
    }

    // Miner is looking for christal
    Optional<Cell> closestOre;
    Optional<Cell> closestSafe = getClosestSafeInterestingOre();
    if (!closestSafe.isPresent()) {
      closestOre = this.getClosestInterestingOre();
    } else {
      closestOre = closestSafe;
    }

    // Found some!
    if (closestOre.isPresent()) {
      if (closestOre.get().getCoord().distance(entity.getPos()) <= 2) {
        return returnAction(Action.dig(closestOre.get().getCoord()));
      } else {
        return returnAction(Action.move(closestOre.get().getCoord()));
      }
    }

    if (entity.isAtHeadquarters()) {
      return returnAction(Action.move(new Coord(entity.getPos().getX() + 8, entity.getPos().getY())));
    }

    // No christal found, go for default mining
    Coord fixedCoord = new Coord(entity.getPos().getX(), entity.getPos().getY());
    while (board.getCell(fixedCoord).isHole()) {
      int newX = fixedCoord.getX() + 1 < board.getWidth() ? fixedCoord.getX() + 1 : fixedCoord.getX() - 1;
      int newY = fixedCoord.getY() < board.getHeight() ? fixedCoord.getY() : fixedCoord.getY() - 1;
      fixedCoord = new Coord(newX, newY);
    }
    return returnAction(Action.dig(fixedCoord));
  }

  private Optional<Cell> getClosestSafeInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = board.getWidth();
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.hasOre()
              && distance < minDistance
              && !cell.hasAllyTrap(board)
              && !cell.isHole()) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }

  private Optional<Cell> getClosestInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = board.getWidth();
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.hasOre()
              && distance < minDistance
              && !cell.hasAllyTrap(board)) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }
}
