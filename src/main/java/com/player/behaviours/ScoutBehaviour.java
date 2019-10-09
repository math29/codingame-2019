package com.player.behaviours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Coord;
import com.player.model.Entity;
import com.player.model.EntityType;

public class ScoutBehaviour extends EntityBehaviour {

    public static final List<Coord> scoutCoord = Collections.unmodifiableList(
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

  public ScoutBehaviour(final Entity entity, final Board board) {
        super(entity, board);
        this.NAME = "Scout" + entity.getId();
    }

  @Override public Action getNextAction() {
        Coord coordToUse = getScoutCoord();

        // If Scout is at the headquarters and carries nothing, take RADAR
        if (entity.isAtHeadquarters() && entity.getItem() == EntityType.NOTHING) {
            return returnAction(Action.request(EntityType.RADAR));
        }

        // If Scout is with RADAR in radar safe zone, dig it in the ground
        if (entity.getItem() == EntityType.RADAR
            && coordToUse.distance(entity.getPos()) <= 2
            && !board.getCell(coordToUse).hasAllyTrap(board)) {
            return returnAction(Action.dig(coordToUse));
        }

        // Scout should move now
        if (entity.getItem() == EntityType.RADAR) {
            return returnAction(Action.move(coordToUse));
        }

        // Return to headquarters for the new RADAR
        return returnAction(Action.move(getCloserHeadQuarterSafeCell().getCoord()));
    }

    private Coord getScoutCoord() {
        // Find the first unknown cell
        return scoutCoord.stream()
            .filter(c -> !board.getCell(c).isKnown())
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
            && !board.getCell(coord).hasAllyTrap(board)){
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
      return this.board.getMyRadarPos().stream()
                .noneMatch(rCoord ->
                               isInside(rCoord.getX(), rCoord.getY(), 4, coord.getX(), coord.getY()));
    }
}
