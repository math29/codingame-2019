package com.player;

import java.util.Random;

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

        // If Scout is at the headquarters and carries RADAR, move
        if (entity.isAtHeadquarters() && entity.item == EntityType.RADAR) {
            Coord randomFreeCoord = getRandomSafeAndRadarFreeCoord(
                    5,
                    board.getWidth() - 3,
                    4,
                    board.getHeight() - 3);
            System.err.println("Scout coord: [" + randomFreeCoord.x + ", " + randomFreeCoord.y + "]");
            return Action.move(randomFreeCoord);
        }

        // If Scout is with RADAR on the map, dig it in the ground
        if (entity.item == EntityType.RADAR
                && (entity.pos.x >= 0 && entity.pos.x < board.width)
                && (entity.pos.y >= 0 && entity.pos.y < board.height)) {
            int xToDig = entity.pos.x == (board.width-1) ? entity.pos.x-1 : entity.pos.x+1;
            int yToDig = entity.pos.y == (board.height-1) ? entity.pos.x-1 : entity.pos.x+1;
            return Action.dig(new Coord(xToDig, yToDig));
        }

        // Return to headquarters for the new RADAR
        return Action.move(getCloserHeadQuarterCell().coord);
    }
}
