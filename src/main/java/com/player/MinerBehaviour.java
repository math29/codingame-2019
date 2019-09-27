package com.player;

import java.util.Arrays;
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

  private static final Coord[] fixedCoord = {
      new Coord(2, 7),
      new Coord(2, 8),
      new Coord(2, 9),
      new Coord(2, 10),
      new Coord(2, 11),
      new Coord(2, 12),
      new Coord(2, 13),
      new Coord(2, 14),
      new Coord(3, 8),
      new Coord(3, 9),
      new Coord(3, 10),
      new Coord(3, 11),
      new Coord(3, 12),
      new Coord(3, 13),
      new Coord(3, 14),
      new Coord(4, 9),
      new Coord(4, 10),
      new Coord(4, 11),
      new Coord(4, 12),
      new Coord(4, 13),
      new Coord(4, 14),
      new Coord(5, 10),
      new Coord(5, 11),
      new Coord(5, 12),
      new Coord(5, 13),
      new Coord(5, 14),
  };

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
    Optional<Cell> closestOre = this.getClosestInterestingOre();

    // Found some!
    if (closestOre.isPresent()) {
      if (closestOre.get().coord.distance(entity.pos) <= 2) {
        return returnAction(Action.dig(closestOre.get().coord));
      } else {
        return returnAction(Action.move(closestOre.get().coord));
      }
    }

    // No Christal found, go fo default mining
    return returnAction(Action.move(getNextFixedCoord()));
  }

  private Optional<Cell> getClosestInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.coord.distance(this.entity.pos);
      if (cell.hasOre() && distance < minDistance && !isCellBad(cell) && isCellAllyZoneSafe(cell)) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }

  private Coord getNextFixedCoord() {
    return Arrays.stream(fixedCoord).filter(coord -> {
      Cell cell = board.getCell(coord);
      return !cell.hasHole() && !isCellBad(cell) && isCellAllyZoneSafe(cell);
    }).findFirst().orElse(new Coord(0, 0));
  }

  private boolean isCellAllyZoneSafe(final Cell cell) {
    return true;
//    return this.board.myTeam.robots.stream().noneMatch(robot -> cell.coord.distance(robot.pos) <= 2);
  }
}
