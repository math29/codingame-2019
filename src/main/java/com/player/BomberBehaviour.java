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

  protected static final String NAME = "Bomber";

  BomberBehaviour(final Entity robot, final Board board) {
    super(robot, board);
    this.board = board;
  }

  @Override Action getNextAction() {
    // If Bomber is at the headquarters and carries nothing, take TRAP
    if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
      return returnAction(Action.request(EntityType.TRAP));
    }

    // If Bomer is with TRAP in trap safe zone, dig it in the ground
    if (entity.item == EntityType.TRAP
            && isInsideRadarOrTrapZone()
            && isCoordOutsideTrapCoverrage(entity.pos)
            && !isCellBad(board.getCell(entity.pos))) {
      return returnAction(Action.dig(new Coord(entity.pos.x + 1, entity.pos.y)));
    }

    // move
    if (entity.item == EntityType.TRAP) {
      Coord randomFreeCoord = getNextTrapTarget(
              5,
              board.getWidth() - 3,
              4,
              board.getHeight() - 3);
      return returnAction(Action.move(randomFreeCoord));
    }

    // Return to headquarters for the new RADAR
    return returnAction(Action.move(getCloserHeadQuarterCell().coord));
  }

  private Coord getNextTrapTarget(int startX, int endX, int startY, int endY) {
    Coord coord = getRandomCoord(startX, endX, startY, endY);
    if (isCoordOutsideTrapCoverrage(coord)
            && !isCellBad(board.getCell(coord))){
      return coord;
    } else {
      return getNextTrapTarget(startX, endX, startY, endY);
    }
  }

  private boolean isCoordOutsideTrapCoverrage(final Coord coord) {
    return this.board.myTrapPos.stream()
            .noneMatch(rCoord ->
                    isInside(rCoord.x, rCoord.y, 4, coord.x, coord.y));
  }
}
