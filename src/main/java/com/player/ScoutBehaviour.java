package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class ScoutBehaviour extends EntityBehaviour {

    protected static final String NAME = "Scout";

    ScoutBehaviour(final Entity entity, final Board board) {
        super(entity, board);
    }

    @Override Action getNextAction() {
        // If Scout is at the headquarters and carries nothing, take RADAR
        if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
            return returnAction(Action.request(EntityType.RADAR));
        }

        // If Scout is with RADAR in radar safe zone, dig it in the ground
        if (entity.item == EntityType.RADAR
            && isInsideRadarOrTrapZone()
            && isCoordOutsideRadarCoverrage(entity.pos)
            && !isCellBad(board.getCell(entity.pos))) {
            return returnAction(Action.dig(new Coord(entity.pos.x + 1, entity.pos.y)));
        }

        // move
        if (entity.item == EntityType.RADAR) {
            Coord randomFreeCoord = getNextRadarTarget(
                5,
                board.getWidth() - 3,
                4,
                board.getHeight() - 3);
            return returnAction(Action.move(randomFreeCoord));
        }

        // Return to headquarters for the new RADAR
        return returnAction(Action.move(getCloserHeadQuarterCell().coord));
    }

    private Coord getNextRadarTarget(int startX, int endX, int startY, int endY) {
        Coord coord = getRandomCoord(startX, endX, startY, endY);
        if (isCoordOutsideRadarCoverrage(coord)
            && !isCellBad(board.getCell(coord))){
            return coord;
        } else {
            return getNextRadarTarget(startX, endX, startY, endY);
        }
    }

    private boolean isCoordOutsideRadarCoverrage(final Coord coord) {
        return this.board.myRadarPos.stream()
                .noneMatch(rCoord ->
                        isInside(rCoord.x, rCoord.y, 4, coord.x, coord.y));
    }
}
