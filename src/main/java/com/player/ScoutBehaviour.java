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
        this.NAME = "Scout";
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
            return returnAction(Action.dig(new Coord(entity.pos.x, entity.pos.y)));
        }

        // move
        if (entity.item == EntityType.RADAR) {
            Coord randomFreeCoord = getNextRadarTarget(
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

    private Coord getNextRadarTarget(int startX, int endX, int startY, int endY, int deep) {
        Coord coord = getRandomCoord(startX, endX, startY, endY);
        if (isCoordOutsideRadarCoverrage(coord)
            && !isCellBad(board.getCell(coord))){
            return coord;
        } else {
            // Should not happen
            if (deep >= 30) {
                return new Coord(0, 0);
            }
            return getNextRadarTarget(startX, endX, startY, endY, deep+1);
        }
    }

    private boolean isCoordOutsideRadarCoverrage(final Coord coord) {
        return this.board.myRadarPos.stream()
                .noneMatch(rCoord ->
                        isInside(rCoord.x, rCoord.y, 4, coord.x, coord.y));
    }
}
