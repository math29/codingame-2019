package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class BomberBehaviour extends EntityBehaviour {

  BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.NAME = "Bomber";
  }

  @Override Action getNextAction() {
    // If Bomber is at the headquarters and carries nothing, take TRAP
    if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
      return returnAction(Action.request(EntityType.TRAP));
    }

    // If Bomber is with TRAP in trap safe zone, dig it in the ground
    if (entity.item == EntityType.TRAP
            && isInsideRadarOrTrapZone()
            && isCoordOutsideTrapCoverrage(entity.pos)
            && !isCellBad(board.getCell(entity.pos))) {
      return returnAction(Action.dig(new Coord(entity.pos.x, entity.pos.y)));
    }

    // move
    if (entity.item == EntityType.TRAP) {
      Cell closestOreCell = this.getClosestInZoneOreCell();
      return returnAction(Action.move(closestOreCell.coord));
    }

    // Return to headquarters for the new RADAR
    return returnAction(Action.move(getCloserHeadQuarterCell().coord));
  }
  
  private boolean isCoordOutsideTrapCoverrage(final Coord coord) {
    return this.board.myTrapPos.stream()
            .noneMatch(rCoord ->
                    isInside(rCoord.x, rCoord.y, 2, coord.x, coord.y));
  }

  Cell getClosestInZoneOreCell() {
    Cell closerCell = board.getCell(this.getRandomSafeCoord(0, board.getWidth(), 0, board.getHeight()));
    int minDistance = 50;
    for (final Cell cell : board.getCells()) {
      int distance = cell.coord.distance(this.entity.pos);
      if (cell.hasOre() && distance < minDistance && !isCellBad(cell)
          && isInsideRadarOrTrapZone()) {
        closerCell = cell;
        minDistance = distance;
      }
    }
    return closerCell;
  }
}
