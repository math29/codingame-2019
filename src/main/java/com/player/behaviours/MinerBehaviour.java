package com.player.behaviours;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.player.model.Action;
import com.player.model.Board;
import com.player.model.Cell;
import com.player.model.Coord;
import com.player.model.Entity;
import com.player.model.EntityType;

public class MinerBehaviour extends EntityBehaviour {

  public MinerBehaviour(final Entity entity, final Board board) {
    super(entity, board);
    this.NAME = "Miner" + entity.getId();
  }

  @Override public Action getNextAction() {
    // Miner has cristal on him, bring it to headquarters
    if (entity.getItem() == EntityType.AMADEUSIUM) {
      return returnAction(Action.move(this.getCloserHeadQuarterSafeCell().getCoord()));
    }

    // Find a good cell to mine
    Optional<Cell> closestOre = getClosestSafeInterestingOre();

    // Found some! Dig if you are close or, otherwise, move
    if (closestOre.isPresent()) {
      return digOrMove(closestOre.get().getCoord());
    }

    // No christal found, go for default mining
    int i = 0;
    int newX = entity.getPos().getX() == 0 ? entity.getPos().getX() + 4 : entity.getPos().getX();
    int newY = entity.getPos().getY();
    Coord fixedCoord = new Coord(newX, newY);
    int counter = 0;
    while (board.getCell(fixedCoord).isHole()
            || board.getCell(fixedCoord).hasAllyTrap(board)
            || board.getCell(fixedCoord).hasPotentialEnemyTrap()
            || board.isDangerousEnemyInRange(board.getCell(fixedCoord))) {
      int tmpX = newX, tmpY = newY;
      switch (i) {
        case 0:
          newX = fixedCoord.getX() - 1 > 0 ? fixedCoord.getX() - 1 : fixedCoord.getX();
          newY = fixedCoord.getY() < board.getHeight() ? fixedCoord.getY() : fixedCoord.getY() - 1;
          break;
        case 1:
          newX = fixedCoord.getX() + 1 < board.getWidth() ? fixedCoord.getX() + 1 : fixedCoord.getX();
          newY = fixedCoord.getY() - 1 > 0 ? fixedCoord.getY() - 1 : fixedCoord.getY();
          break;
        case 2:
          newX = fixedCoord.getX() + 1 < board.getWidth() ? fixedCoord.getX() + 1 : fixedCoord.getX();
          newY = fixedCoord.getY() + 1 < board.getHeight() ? fixedCoord.getY() + 1 : fixedCoord.getY();
          break;
        case 3:
          newX = fixedCoord.getX() - 1 < board.getWidth() ? fixedCoord.getX() - 1 : fixedCoord.getX();
          newY = fixedCoord.getY() + 1 < board.getHeight() ? fixedCoord.getY() + 1 : fixedCoord.getY();
          break;
        default:
          newX = fixedCoord.getX() + 4 < board.getWidth() ? fixedCoord.getX() + 4 : fixedCoord.getX();
          newY = fixedCoord.getY() + 1 < board.getHeight() ? fixedCoord.getY() + 1 : fixedCoord.getY();

        // Try to make sure we are not stuck
        if (tmpX == newX && tmpY == newY) {
          counter++;
        } else {
          counter = 0;
        }
        // We are stuck, take a random coordinate
        if (counter == 5) {
          newX = ThreadLocalRandom.current().nextInt(5, board.getWidth() - 3);
          newY = ThreadLocalRandom.current().nextInt(4, board.getHeight() - 3);
        }
      }

      fixedCoord = new Coord(newX, newY);
      i++;
    }

    return digOrMove(fixedCoord);
  }

  private Action digOrMove(Coord coord) {
    if (coord.distance(entity.getPos()) <= 2) {
      return returnAction(Action.dig(coord));
    } else {
      return returnAction(Action.move(coord));
    }
  }

  private Optional<Cell> getClosestSafeInterestingOre() {
    Optional<Cell> closerCell = Optional.empty();
    int minDistance = board.getWidth();
    for (final Cell cell : board.getCells()) {
      int distance = cell.getCoord().distance(this.entity.getPos());
      if (cell.hasOre()
              && distance < minDistance
              && !cell.hasAllyTrap(board)
              && !cell.hasPotentialEnemyTrap()
              && !board.isDangerousEnemyInRange(cell)) {
        closerCell = Optional.of(cell);
        minDistance = distance;
      }
    }
    return closerCell;
  }
}
