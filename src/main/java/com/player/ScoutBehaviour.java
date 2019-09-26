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
        System.err.println("board.getCell(entity.pos): " + board.getCell(entity.pos));
        if (entity.item == EntityType.RADAR && isCellRadarFree(board.getCell(entity.pos))) {
            int xToDig = entity.pos.x == (board.width-1) ? entity.pos.x-1 : entity.pos.x+1;
            int yToDig = entity.pos.y == (board.height-1) ? entity.pos.x-1 : entity.pos.x+1;
            return Action.dig(new Coord(xToDig, yToDig));
        }

        // move
        if (entity.item == EntityType.RADAR) {
            Coord randomFreeCoord = getNextRadarTarget(
                5,
                board.getWidth() - 3,
                4,
                board.getHeight() - 3);
            System.err.println("Scout coord: [" + randomFreeCoord.x + ", " + randomFreeCoord.y + "]");
            return Action.move(randomFreeCoord);
        }

        // Return to headquarters for the new RADAR
        return Action.move(getCloserHeadQuarterCell().coord);
    }

    Coord getNextRadarTarget(int startX, int endX, int startY, int endY) {
        Coord coord = getRandomCoord(startX, endX, startY, endY);
        if(!isCellRadarFree(board.getCell(coord))
            && !isCellBad(board.getCell(coord))){
            return coord;
        } else {
            return getRandomSafeCoord();
        }
    }
}
