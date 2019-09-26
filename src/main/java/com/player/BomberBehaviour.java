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
      Coord randomFreeCoord = getNextTrapTarget(
              5,
              board.getWidth() - 3,
              4,
              board.getHeight() - 3,
              0);
      return returnAction(Action.move(randomFreeCoord));
    }

    // Return to headquarters for the new RADAR
    return returnAction(Action.move(getCloserHeadQuarterCell().coord));
  }

  private Coord getNextTrapTarget(int startX, int endX, int startY, int endY, int deep) {
    Coord coord = getRandomCoord(startX, endX, startY, endY);
    if (isCoordOutsideTrapCoverrage(coord)
            && !isCellBad(board.getCell(coord)) && board.getCell(coord).hasOre()){
        return coord;
    } else {
      // Should not happen
      if (deep >= 30) {
        return coord;
      }
    }
    return getNextTrapTarget(startX, endX, startY, endY, deep + 1);
  }

  private boolean isCoordOutsideTrapCoverrage(final Coord coord) {
    return this.board.myTrapPos.stream()
            .noneMatch(rCoord ->
                    isInside(rCoord.x, rCoord.y, 2, coord.x, coord.y));
  }
}
