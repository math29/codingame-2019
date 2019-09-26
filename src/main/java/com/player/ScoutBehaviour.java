package com.player;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

import java.util.Random;

class ScoutBehaviour extends EntityBehaviour {

    ScoutBehaviour(final Entity entity, final Board board) {
        super(entity, board);
    }

    @Override Action getNextAction() {
        entity.action.message = "Scout Robot";

        if (entity.pos.y == 0 && entity.item == EntityType.NOTHING) {
            return Action.request(EntityType.RADAR);
        }

        if (entity.pos.y == 0 && entity.item == EntityType.RADAR) {
            Random r = new Random();
            int x = r.ints(0, (board.getHeight() + 1)).findFirst().getAsInt();
            int y = r.ints(0, (board.getWidth() + 1)).findFirst().getAsInt();
            return Action.move(new Coord(x, y));
        }

        if (entity.item == EntityType.RADAR
                && (entity.pos.x >= 0 && entity.pos.x < board.width)
                && (entity.pos.y >= 0 && entity.pos.y < board.height)) {
            int xToDig = entity.pos.x == (board.width-1) ? entity.pos.x-1 : entity.pos.x+1;
            int yToDig = entity.pos.y == (board.height-1) ? entity.pos.x-1 : entity.pos.x+1;
            return Action.dig(new Coord(xToDig, yToDig));
        }

        return Action.move(new Coord(0,0));
    }
}
