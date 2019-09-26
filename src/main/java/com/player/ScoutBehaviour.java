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

    ScoutBehaviour(final Entity entity, final Board board) {
        super(entity, board);
    }

    @Override Action getNextAction() {

        // If Scout is at the headquarters and carries nothing, take RADAR
        if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
            return Action.request(EntityType.RADAR);
        }

        // If Scout is with RADAR on the map in radar safe zone, dig it in the ground
        if (entity.item == EntityType.RADAR
            && isInsideRadarZone()
            && isCoordOutsideRadarCoverrage(entity.pos)) {
            return Action.dig(new Coord(entity.pos.x + 1, entity.pos.y));
        }

        // move
        if (entity.item == EntityType.RADAR) {
            Coord randomFreeCoord = getNextRadarTarget(
                5,
                board.getWidth() - 3,
                4,
                board.getHeight() - 3);
            return Action.move(randomFreeCoord);
        }

        // Return to headquarters for the new RADAR
        return Action.move(getCloserHeadQuarterCell().coord);
    }

    private Coord getNextRadarTarget(int startX, int endX, int startY, int endY) {
        Coord coord = getRandomCoord(startX, endX, startY, endY);
        if (isCoordOutsideRadarCoverrage(coord)
            && !isCellBad(board.getCell(coord))){
            return coord;
        } else {
            return getRandomCoord(startX, endX, startY, endY);
        }
    }

    private boolean isInsideRadarZone() {
        return this.entity.pos.x >= 5
                && this.entity.pos.x < board.getWidth() - 3
                && this.entity.pos.y >= 4
                && this.entity.pos.y < board.getHeight() - 3;
    }

    private boolean isCoordOutsideRadarCoverrage(final Coord coord) {
        return
            this.board.myRadarPos.stream()
                    .noneMatch(rCoord ->
                            isInside(rCoord.x, rCoord.y, 4, coord.x, coord.y));
    }
}
