package com.player;

import java.util.Optional;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class MinerBehaviour extends EntityBehaviour {

  MinerBehaviour(final Entity entity, final Board board) {
    super(entity, board);
    this.NAME = "Miner";
  }

  @Override Action getNextAction() {
    // Miner has cristal on him else go to mine
    if (entity.hasItem()) {
      return returnAction(Action.move(this.getCloserHeadQuarterCell().coord));
    }

    // Miner is looking for christal
    Optional<Cell> closestOre = Optional.empty();
    Optional<Cell> closestSafe = getClosestSafeInterestingOre();
    if (!closestSafe.isPresent()) {
      closestOre = this.getClosestInterestingOre();
      System.err.println(String.format("Closest cell: (%s,%s).",
              closestOre.get().coord.x, closestOre.get().coord.y));
    } else {
      closestOre = closestSafe;
      System.err.println(String.format("Closest safe cell: (%s,%s).",
              closestOre.get().coord.x, closestOre.get().coord.y));
    }

    // Found some!
    if (closestOre.isPresent()) {
      if (closestOre.get().coord.distance(entity.pos) <= 2) {
        return returnAction(Action.dig(closestOre.get().coord));
      } else {
        return returnAction(Action.move(closestOre.get().coord));
      }
    }

    // No Christal found, go fo default mining
    Coord nextFixedCoord = new Coord(entity.pos.x + 1, entity.pos.y);
    if (!this.board.getCell(nextFixedCoord).hasHole()) {
      return returnAction(Action.dig(nextFixedCoord));
    }
    return returnAction(Action.move(nextFixedCoord));
  }

  private Optional<Cell> getClosestSafeInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.coord.distance(this.entity.pos);
      if (cell.hasOre()
              && distance < minDistance
              && !isCellBad(cell)
              && isCellAllyZoneSafe(cell)
              && Action.IsDiggedByUs(cell.coord)) {
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
      int distance = cell.coord.distance(this.entity.pos);
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
