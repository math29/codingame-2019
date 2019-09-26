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
            System.err.println("DIG!");
            int xToDig = entity.pos.x + 1;
            int yToDig = entity.pos.y;
            return Action.dig(new Coord(xToDig, yToDig));
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

    Coord getNextRadarTarget(int startX, int endX, int startY, int endY) {
        Coord coord = getRandomCoord(startX, endX, startY, endY);
        if (isCoordOutsideRadarCoverrage(coord)
            && !isCellBad(board.getCell(coord))){
            return coord;
        } else {
            return getRandomSafeCoord();
        }
    }

    boolean isInsideRadarZone() {
        return this.entity.pos.x >= 5 && this.entity.pos.x <= board.getWidth() - 3 && this.entity.pos.y >= 4
            && this.entity.pos.y <= board.getHeight() - 3;
    }

    boolean isCoordOutsideRadarCoverrage(final Coord coord) {
        return
            this.board.myRadarPos.stream().noneMatch(rCoord -> isInside(rCoord.x, rCoord.y, 4, coord.x, coord.y));
    }
}
