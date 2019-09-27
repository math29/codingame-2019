package com.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

class ScoutBehaviour extends EntityBehaviour {

    private static final List<Coord> scoutCoord = Collections.unmodifiableList(
            new ArrayList<Coord>() {{
                // phase # 1
                add(new Coord(5, 5));
                add(new Coord(10, 9));
                add(new Coord(15, 4));
                add(new Coord(19, 9));
                add(new Coord(24, 5));
                // phase # 2
                add(new Coord(28, 10));
                // phase # 3
                add(new Coord(10, 0));
                add(new Coord(20, 0));
                add(new Coord(14, 14));
                add(new Coord(23, 14));
                add(new Coord(6, 14));
                // phase # 4
                add(new Coord(29, 1));
                add(new Coord(1, 0));
                add(new Coord(1, 10));
                // just in case coordinates
                add(new Coord(8, 3));
                add(new Coord(13, 7));
                add(new Coord(16, 11));
            }});

    ScoutBehaviour(final Entity entity, final Board board) {
        super(entity, board);
        this.NAME = "Scout";
    }

    @Override Action getNextAction() {
        Coord coordToUse = getScoutCoord();

        // If Scout is at the headquarters and carries nothing, take RADAR
        if (entity.isAtHeadquarters() && entity.item == EntityType.NOTHING) {
            return returnAction(Action.request(EntityType.RADAR));
        }

        // If Scout is with RADAR in radar safe zone, dig it in the ground
        if (entity.item == EntityType.RADAR
            && coordToUse.distance(entity.pos) <= 2
            && !isCellBad(board.getCell(coordToUse))) {
            return returnAction(Action.dig(coordToUse));
        }

        // move
        if (entity.item == EntityType.RADAR) {
            return returnAction(Action.move(coordToUse));
        }

        // Return to headquarters for the new RADAR
        return returnAction(Action.move(getCloserHeadQuarterCell().coord));
    }

    private Coord getScoutCoord() {
        // Find the first unknown cell
        return scoutCoord.stream()
                .filter(c -> !board.getCell(c).known)
                .findFirst()
                // Normally, this should not happen
                .orElse(getNextRadarTarget(5,
                        board.getWidth() - 3,
                        4,
                        board.getHeight() - 3,
                        0));
    }

    private Coord getNextRadarTarget(int startX, int endX, int startY, int endY, int deep) {
        Coord coord = getRandomCoord(startX, endX, startY, endY);
        if (isCoordOutsideRadarCoverrage(coord)
            && !isCellBad(board.getCell(coord))){
            return coord;
        } else {
            // Should not happen
            if (deep >= 30) {
                return coord;
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
