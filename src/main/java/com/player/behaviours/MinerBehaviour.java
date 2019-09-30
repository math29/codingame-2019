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
    // Miner has cristal on him else go to mine
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

    // No Christal found, go fo default mining
    Coord nextFixedCoord = new Coord(entity.getPos().getX() + 1, entity.getPos().getY());
    if (!this.board.getCell(nextFixedCoord).hasHole()) {
      return returnAction(Action.dig(nextFixedCoord));
    }
    return returnAction(Action.move(nextFixedCoord));
  }

  private Optional<Cell> getClosestSafeInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.hasOre()
              && distance < minDistance
              && !isCellBad(cell)
              && isCellAllyZoneSafe(cell)
              && !cell.hasHole()) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }

  private Optional<Cell> getClosestInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.hasOre()
              && distance < minDistance
              && !isCellBad(cell)
              && isCellAllyZoneSafe(cell)) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }

  private boolean isCellAllyZoneSafe(final Cell cell) {
    return true;
//    return this.board.myTeam.robots.stream().noneMatch(robot -> cell.coord.distance(robot.pos) <= 2);
  }
}